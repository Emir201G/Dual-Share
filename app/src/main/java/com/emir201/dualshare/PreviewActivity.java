package com.emir201.dualshare;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PreviewActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imageView = findViewById(R.id.imagenPreview);

        String imageURI = getIntent().getStringExtra("image");

        if (imageURI != null) {
            Uri uri = Uri.parse(imageURI);
            imageView.setImageURI(uri);
        } else {
            Toast.makeText(this, "⚠️ No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }
}
