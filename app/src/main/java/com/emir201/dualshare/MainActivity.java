package com.emir201.dualshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    // --- Botones UI ---
    private AppCompatButton btnGoogle, btnFacebook;
    // --- Cliente Google y Firebase ---
    private GoogleSignInClient googleClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Cambiar colores de barra ---
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.ultra_ligth_gray));
        window.setNavigationBarColor(getColor(R.color.ultra_ligth_gray));

        // --- Referencias UI ---
        btnGoogle = findViewById(R.id.btnGoogle);

        // --- Inicializar FirebaseAuth ---
        firebaseAuth = FirebaseAuth.getInstance();

        // --- Configuración Google Sign-In ---
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // TOKEN para Firebase
                .requestEmail() // Pedimos email del usuario
                .build();

        // --- Inicializar GoogleSignInClient ---
        googleClient = GoogleSignIn.getClient(this, gso);

        // --- Listener botón Google ---
        btnGoogle.setOnClickListener(v -> signInGoogle());
    }

    // --- Abrir pantalla de login de Google ---
    private void signInGoogle() {
        Intent signInIntent = googleClient.getSignInIntent();
        launcher.launch(signInIntent);
    }

    // --- Recibir resultado de login ---
    private ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Task<GoogleSignInAccount> task =
                                    GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                            handleSignIn(task);
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(this, "Login cancelado o fallido", Toast.LENGTH_SHORT).show();
                        }
                    });

    // --- Convertir cuenta de Google a usuario Firebase ---
    private void handleSignIn(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult();

            if (account == null) {
                Toast.makeText(this, "Fallo al obtener cuenta de Google", Toast.LENGTH_LONG).show();
                return;
            }

            // Obtenemos token de Google
            String token = account.getIdToken();

            if (token == null) {
                Toast.makeText(this, "Token de Google nulo", Toast.LENGTH_LONG).show();
                return;
            }

            // Creamos credencial de Firebase
            AuthCredential credential = GoogleAuthProvider.getCredential(token, null);

            // Iniciamos sesión en Firebase
            firebaseAuth.signInWithCredential(credential)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(this, "Login correcto!", Toast.LENGTH_SHORT).show();
                        // Aquí puedes ir a otra actividad, ej: MainAppActivity
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error Firebase: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });

        } catch (Exception e) {
            Toast.makeText(this, "Fallo: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
