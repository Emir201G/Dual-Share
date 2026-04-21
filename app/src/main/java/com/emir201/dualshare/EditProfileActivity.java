package com.emir201.dualshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private AppCompatButton btnName, btnLogOut, btnPhotoProfile;
    private TextView txtNameUser, txtGmail;
    private CircleImageView imgProfile;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleClient;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnName = findViewById(R.id.btnName);
        btnLogOut = findViewById(R.id.btnLogOut);
        txtNameUser = findViewById(R.id.txtNameUser);
        txtGmail = findViewById(R.id.txtGmail);
        btnPhotoProfile = findViewById(R.id.btnPhoto);
        imgProfile = findViewById(R.id.imgProfile);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        // Recibir datos del Intent
        Intent intent = getIntent();
        String receivedName = intent.getStringExtra("name");
        Uri receivedPhoto = intent.getParcelableExtra("photo");
        String receivedEmail = intent.getStringExtra("email");

        txtNameUser.setText(receivedName);
        txtGmail.setText(receivedEmail != null ? receivedEmail : "Email no disponible");

        if (receivedPhoto != null) {
            Glide.with(this).load(receivedPhoto).into(imgProfile);
        }

        // Animaciones
        applyTouchAnimation(btnName);
        applyTouchAnimation(btnPhotoProfile);
        applyTouchAnimation(btnLogOut);

        // BottomSheet ejemplo
        btnName.setOnClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(EditProfileActivity.this);
            View view1 = LayoutInflater.from(EditProfileActivity.this)
                    .inflate(R.layout.botton_sheet_layout, null);
            bottomSheetDialog.setContentView(view1);
            bottomSheetDialog.show();
        });

        // Cerrar sesión
        btnLogOut.setOnClickListener(view -> showLogoutDialog());

        // Cambiar la foto de perfil
        btnPhotoProfile.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
    }

    // Diálogo para confirmar logout
    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    dialog.dismiss();
                    logout();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Logout real Google + Firebase
    private void logout() {
        firebaseAuth.signOut();
        googleClient.signOut().addOnCompleteListener(task -> {
            Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    // Animación
    private void applyTouchAnimation(View v) {
        v.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(80).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
                    break;
            }
            return false;
        });
    }

    // Lanzador de galería
    private ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imageUri = uri;
                    imgProfile.setImageURI(uri);
                    uploadImageToFirebase(uri);
                }
            });

    // ✔ CORREGIDO: subir imagen a Firebase Storage
    private void uploadImageToFirebase(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "No hay usuario logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("profile_images/" + user.getUid() + ".jpg");

        storageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot ->
                        storageRef.getDownloadUrl().addOnSuccessListener(this::updateUserPhoto))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al subir imagen", Toast.LENGTH_SHORT).show());
    }

    // Actualizar foto de perfil en FirebaseAuth
    private void updateUserPhoto(Uri downloadUri) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        UserProfileChangeRequest profileUpdates =
                new UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUri)
                        .build();

        user.updateProfile(profileUpdates)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Foto actualizada", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al actualizar perfil", Toast.LENGTH_SHORT).show());
    }
}
