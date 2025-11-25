package com.emir201.dualshare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class VoiceRecorderActivity extends AppCompatActivity {

    private ImageButton btnRecord;
    private ImageButton btnTrash, btnChecked, btn_play_audio;
    private MediaRecorder recorder;
    private String audioPath;
    private Handler handler = new Handler();
    private int seconds = 0;
    private MediaPlayer player;
    private TextView textViewCount;


    private final int REQUEST_AUDIO_PERMISSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recorder);

        requestPermission();

        btnRecord = findViewById(R.id.btnRecord);
        btnTrash = findViewById(R.id.btnTrash);
        btnChecked = findViewById(R.id.btnChecked);
        btn_play_audio = findViewById(R.id.btn_play_audio);
        textViewCount = findViewById(R.id.textViewCount);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        window.setNavigationBarColor(getColor(R.color.white));


        btnRecord.setOnTouchListener(new View.OnTouchListener() {

            float startX;
            boolean isRecording = false;
            boolean isCanceled = false;
            boolean isChecked = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getRawX();
                        isRecording = true;
                        isCanceled = false;
                        isChecked = false;

                        Glide.with(VoiceRecorderActivity.this)
                                .asGif()
                                .load(R.drawable.voice_black)
                                .into(btnRecord);

                        showIcons();
                        startRecording();
                        enlargeButton();
                        return true;


                    case MotionEvent.ACTION_MOVE:
                        float currentX = event.getRawX();
                        float deltaX = currentX - startX;

                        // LEFT — CANCEL
                        if (deltaX < -140 && !isCanceled && isRecording) {
                            isCanceled = true;
                            cancelRecording();
                            animateTrashCancel();
                            resetButton();
                        }

                        // RIGHT — SEND
                        if (deltaX > 140 && !isChecked && isRecording) {
                            isChecked = true;
                            send();
                            animateCheckConfirm();
                            stopRecording();
                            stopTimer();
                            resetButton();
                        }

                        return true;


                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        if (!isChecked) {
                            btnRecord.setImageResource(R.drawable.recorder);
                        }

                        resetButton();

                        if (isRecording && !isCanceled) {
                            stopRecording();
                        }

                        hideIcons();
                        return true;
                }
                return false;
            }
        });

        btn_play_audio.setOnClickListener(e -> playAudio());
    }

    // ---------------------- ANIMATIONS -----------------------

    private void enlargeButton() {
        btnRecord.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).start();
    }

    private void resetButton() {
        btnRecord.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
    }


    // ---------------------- LOGIC OF SYSTEM -----------------------

    public void startRecording() {
        try {
            audioPath = getExternalFilesDir(null).getAbsolutePath() + "/grabacion_" + System.currentTimeMillis() + ".mp3";

            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.setOutputFile(audioPath);

            recorder.prepare();
            recorder.start();

            startTimer();

            Log.d("AUDIO", "Grabando...");
        } catch (
                Exception e) {
            e.printStackTrace();
        }


    }

    private void startTimer() {
        seconds = 0;
        handler.postDelayed(timerRunnable, 1000);
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            seconds++;

            int min = seconds / 60;
            int sec = seconds % 60;

            String tiempo = String.format("%02d:%02d", min, sec);
            textViewCount.setText(tiempo);

            handler.postDelayed(this, 1000);
        }
    };

    public void cancelRecording() {
        Log.d("GRABAR", "Grabación cancelada");
    }

    private void stopRecording() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;

            stopTimer();

            Log.d("AUDIO", "Audio guardado en: " + audioPath);
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    private void playAudio() {
        try {
            player = new MediaPlayer();
            player.setDataSource(audioPath);
            player.prepare();
            player.start();

        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    public void send() {
        btnRecord.setImageResource(R.drawable.send);
        btnRecord.setScaleType(ImageView.ScaleType.FIT_CENTER);
        showPlayIcon();
    }


    // ---------------------- ICONS -----------------------

    private void showIcons() {
        btnTrash.setAlpha(0f);
        btnTrash.setVisibility(View.VISIBLE);
        btnTrash.animate().alpha(1f).setDuration(200).start();

        btnChecked.setAlpha(0f);
        btnChecked.setVisibility(View.VISIBLE);
        btnChecked.animate().alpha(1f).setDuration(200).start();
    }

    private void hideIcons() {
        btnTrash.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(() -> btnTrash.setVisibility(View.GONE))
                .start();

        btnChecked.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction(() -> btnChecked.setVisibility(View.GONE))
                .start();
    }

    private void showPlayIcon() {
        btn_play_audio.setVisibility(View.VISIBLE);
    }

    private void animateTrashCancel() {
        btnTrash.animate()
                .scaleX(1.4f)
                .scaleY(1.4f)
                .setDuration(120)
                .withEndAction(() ->
                        btnTrash.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                ).start();
    }

    private void animateCheckConfirm() {
        btnChecked.animate()
                .scaleX(1.4f)
                .scaleY(1.4f)
                .setDuration(120)
                .withEndAction(() ->
                        btnChecked.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                ).start();
    }

    // ---------------------- PERMISSIONS -----------------------

    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO_PERMISSION);
        }
    }


}
