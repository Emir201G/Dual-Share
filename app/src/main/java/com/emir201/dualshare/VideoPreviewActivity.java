package com.emir201.dualshare;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPreviewActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        videoView = findViewById(R.id.video_view);

        String uriString = getIntent().getStringExtra("video_uri");
        Uri videoUri = Uri.parse(uriString);

        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(false);
            videoView.start();
        });
    }
}
