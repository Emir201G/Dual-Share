package com.emir201.dualshare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private AppCompatButton btnName, btnGoogle, btnPhotoProfile;
    private CircleImageView imgProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        btnName = findViewById(R.id.btnName);

    }
}
