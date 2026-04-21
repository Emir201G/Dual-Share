package com.emir201.dualshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraInfo;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    private PreviewView previewView;
    private ImageButton btnCapture;
    private ImageCapture imageCapture;

    private ImageButton btnFlash;
    private androidx.camera.core.Camera camera;
    private boolean isFrontCamera = false;
    private boolean isFlashOn = false;
    private boolean isVideoMode = false;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ProgressBar progressTimer;
    private CountDownTimer countDownTimer;

    private boolean isRecording = false;
    private ImageButton btnVideo;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        hideSystemUI();


        previewView = findViewById(R.id.previewView);
        btnCapture = findViewById(R.id.btnCapture);
        ImageButton btnInverse = findViewById(R.id.btnInverse);
        btnFlash = findViewById(R.id.btnFlash);
        btnVideo = findViewById(R.id.btnVideo);
        progressTimer = findViewById(R.id.progress_timer);


        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.black));
        window.setNavigationBarColor(getColor(R.color.black));

        checkAndRequestPermissions();

        btnCapture.setOnClickListener(v -> {
            if (isVideoMode) {
                toggleVideoRecording();
            } else {
                takePhoto();
            }
        });

        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (camera != null) {
                    CameraControl control = camera.getCameraControl();
                    CameraInfo info = camera.getCameraInfo();

                    float currentZoom = info.getZoomState().getValue().getZoomRatio();
                    float scale = detector.getScaleFactor();

                    float minZoom = info.getZoomState().getValue().getMinZoomRatio();
                    float maxZoom = info.getZoomState().getValue().getMaxZoomRatio();
                    float newZoom = Math.max(minZoom, Math.min(currentZoom * scale, maxZoom));

                    control.setZoomRatio(newZoom);
                }
                return true;
            }
        });

        previewView.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });


        btnInverse.setOnClickListener(v -> {
            isFrontCamera = !isFrontCamera;
            startCamera();
        });

        btnFlash.setOnClickListener(v -> toggleFlash());

        btnVideo.setOnClickListener(v -> switchCameraMode());
    }

    private void hideSystemUI() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            getWindow().getInsetsController().hide(
                    WindowInsets.Type.statusBars() |
                            WindowInsets.Type.navigationBars()
            );

            getWindow().getInsetsController().setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );

        } else {

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void checkAndRequestPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        } else {
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "⚠️ Permisos requeridos", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                cameraProvider.unbindAll();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(isFrontCamera ? CameraSelector.LENS_FACING_FRONT : CameraSelector.LENS_FACING_BACK).build();

                if (isVideoMode) {
                    Recorder recorder = new Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HD)).build();
                    videoCapture = VideoCapture.withOutput(recorder);
                    camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture);

                } else {
                    imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

                    camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                }


            } catch (
                    ExecutionException |
                    InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null)
            return;

        File photoDir = new File(getExternalFilesDir(null), "photos");
        if (!photoDir.exists())
            photoDir.mkdirs();

        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        File photoFile = new File(photoDir, fileName);

        ImageCapture.OutputFileOptions outputOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Uri savedUri = Uri.fromFile(photoFile);

                Toast.makeText(CameraActivity.this, "📸 Foto guardada", Toast.LENGTH_SHORT).show();

                System.out.println("✅ Ruta de la imagen: " + savedUri);

                if (photoFile.exists()) {
                    Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                    intent.putExtra("image", savedUri.toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(CameraActivity.this, "❌ No se encontró la foto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                exception.printStackTrace();
                Toast.makeText(CameraActivity.this, "❌ Error: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void toggleFlash() {
        if (camera == null) {
            Toast.makeText(this, "Camara no inicializada", Toast.LENGTH_SHORT).show();
            return;
        }

        CameraControl cameraControl = camera.getCameraControl();
        isFlashOn = !isFlashOn;
        cameraControl.enableTorch(isFlashOn);
        if (isFlashOn) {
            btnFlash.setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_IN);
        } else {
            btnFlash.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }
    }

    private void switchCameraMode() {
        isVideoMode = !isVideoMode;

        if (isVideoMode) {
            btnVideo.setImageResource(R.drawable.camara);
            btnCapture.setImageResource(R.drawable.rec);
            btnCapture.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
        } else {
            stopVideoRecording();
            btnVideo.setImageResource(R.drawable.video);
            btnCapture.setImageResource(R.drawable.photo);
            btnCapture.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        startCamera();
    }

    private void toggleVideoRecording() {
        if (isRecording) {
            stopVideoRecording();
        } else {
            startVideoRecording();
        }
    }

    private void startVideoRecording() {

        if (videoCapture == null)
            return;

        isRecording = true;
        btnCapture.setImageResource(R.drawable.stop);

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        progressTimer.setProgress(0);

        countDownTimer = new CountDownTimer(15000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                int elapsed = 15000 - (int) millisUntilFinished;
                progressTimer.setProgress(elapsed);
            }

            @Override
            public void onFinish() {
                stopVideoRecording();
            }
        }.start();

        handler.postDelayed(() -> {
            if (isRecording)
                stopVideoRecording();
        }, 15000);


        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "video_" + System.currentTimeMillis());
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        MediaStoreOutputOptions options = new MediaStoreOutputOptions
                .Builder(getContentResolver(), MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        recording = videoCapture.getOutput()
                .prepareRecording(this, options)
                .withAudioEnabled()
                .start(ContextCompat.getMainExecutor(this), event -> {

                    if (event instanceof VideoRecordEvent.Finalize) {

                        VideoRecordEvent.Finalize finalize = (VideoRecordEvent.Finalize) event;

                        if (!finalize.hasError()) {

                            Uri savedUri = finalize.getOutputResults().getOutputUri();

                            Toast.makeText(CameraActivity.this, "Video Guardado", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                            intent.putExtra("video", savedUri.toString());
                            startActivity(intent);

                        } else {
                            Toast.makeText(CameraActivity.this, "Error Video ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void stopVideoRecording() {
        if (recording != null) {
            recording.stop();
            recording = null;
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        progressTimer.setProgress(0);

        isRecording = false;
        btnCapture.setImageResource(R.drawable.photo);

        handler.removeCallbacksAndMessages(null);
    }

}
