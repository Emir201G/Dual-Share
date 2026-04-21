package com.emir201.dualshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnGoogle;
    private GoogleSignInClient googleClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.ultra_ligth_gray));
        window.setNavigationBarColor(getColor(R.color.ultra_ligth_gray));

        btnGoogle = findViewById(R.id.btnGoogle);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        btnGoogle.setOnClickListener(v -> signInGoogle());
    }

    private void signInGoogle() {
        Intent signInIntent = googleClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    private ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Task<GoogleSignInAccount> task =
                                    GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleSignIn(task);
                        } else {
                            Toast.makeText(this, "Login cancelado o fallido", Toast.LENGTH_SHORT).show();
                        }
                    });

    private void handleSignIn(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult();

            if (account == null) {
                Toast.makeText(this, "No se pudo obtener la cuenta", Toast.LENGTH_LONG).show();
                return;
            }

            String name = account.getDisplayName();
            Uri photo = account.getPhotoUrl();
            String gmail = account.getEmail();
            String token = account.getIdToken();

            // 1. Preparamos los Intents fuera del bloque asíncrono
            Intent intentHome = new Intent(MainActivity.this, HomeActivity.class);
            intentHome.putExtra("name", name);
            intentHome.putExtra("email", gmail);
            intentHome.putExtra("photo", photo);

            // 2. Iniciamos el proceso de autenticación de Firebase
            AuthCredential credential = GoogleAuthProvider.getCredential(token, null);

            firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        // 3. SOLO SI EL LOGIN CON FIREBASE ES EXITOSO, LANZAMOS LAS ACTIVITIES
                        Toast.makeText(this, "Login correcto!", Toast.LENGTH_SHORT).show();

                        // 👉 LANZAMOS LAS ACTIVITIES AQUÍ:

                        startActivity(intentHome);
                        finish(); // Cerrar MainActivity
                    })
                    .addOnFailureListener(e -> {
                        // 4. Si falla la autenticación de Firebase, mostramos el error y no lanzamos las activities
                        Toast.makeText(this, "Error Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

            // ⚠️ Eliminamos las llamadas a startActivity() y finish() de aquí.

        } catch (Exception e) {
            Toast.makeText(this, "Fallo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}
