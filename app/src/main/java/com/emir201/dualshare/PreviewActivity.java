package com.emir201.dualshare;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PreviewActivity extends AppCompatActivity {

    private ImageView imagePreview;
    private PlayerView videoPreview;
    private ExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imagePreview = findViewById(R.id.imagenPreview);
        videoPreview = findViewById(R.id.videoPreview);

        String image = getIntent().getStringExtra("image");
        String video = getIntent().getStringExtra("video");

        if (image != null) {

            imagePreview.setVisibility(View.VISIBLE);
            videoPreview.setVisibility(View.GONE);

            Uri uri = Uri.parse(image);
            imagePreview.setImageURI(uri);

        } else if (video != null) {

            imagePreview.setVisibility(View.GONE);
            videoPreview.setVisibility(View.VISIBLE);

            player = new ExoPlayer.Builder(this).build();

            videoPreview.setPlayer(player);

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(video));
            player.setMediaItem(mediaItem);

            player.prepare();
            player.setPlayWhenReady(true);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (player != null) {
            player.release();
            player = null;
        }
    }
}