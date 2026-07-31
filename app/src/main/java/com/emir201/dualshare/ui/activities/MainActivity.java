package com.emir201.dualshare.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.emir201.dualshare.R;
import com.emir201.dualshare.data.local.entity.UserEntity;
import com.emir201.dualshare.data.local.prefs.SessionManager;
import com.emir201.dualshare.data.remote.client.RetrofitClient;
import com.emir201.dualshare.data.remote.dto.TokenRequestDTO;
import com.emir201.dualshare.data.remote.dto.UserResponseDTO;
import com.emir201.dualshare.mapper.UserMapper;
import com.emir201.dualshare.viewmodel.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AUTH";

    private MaterialButton btnGoogle;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleClient;
    private SessionManager sessionManager;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitClient.init(this);

        firebaseAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);

        // Inicializar ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        Log.d(TAG, "Firebase: " + (firebaseAuth.getCurrentUser() != null));
        Log.d(TAG, "Session: " + sessionManager.isLoggedIn());

        if (isUserLoggedIn()) {
            Log.d(TAG, "Usuario con sesión activa");
            openHome();
            return;
        }

        initView();
        initGoogleSignIn();
        setupListeners();
    }

    private boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null
                && sessionManager.isLoggedIn();
    }

    private void initView() {

        setContentView(R.layout.activity_main);

        btnGoogle = findViewById(R.id.btnGoogle);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.ultra_ligth_gray));
        window.setNavigationBarColor(getColor(R.color.ultra_ligth_gray));
    }

    private void initGoogleSignIn() {

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupListeners() {
        btnGoogle.setOnClickListener(v -> signInGoogle());
    }

    private void signInGoogle() {
        launcher.launch(googleClient.getSignInIntent());
    }

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {

                        if (result.getResultCode() != RESULT_OK) {
                            showToast("Login cancelado");
                            return;
                        }

                        try {

                            GoogleSignInAccount account =
                                    GoogleSignIn.getSignedInAccountFromIntent(result.getData())
                                            .getResult();

                            firebaseLogin(account);

                        } catch (Exception e) {
                            Log.e(TAG, "Error Google SignIn", e);
                            showToast(e.getMessage());
                        }
                    });

    private void firebaseLogin(GoogleSignInAccount account) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(result -> {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    if (user != null) {
                        getFirebaseToken(user);
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Firebase Login", e);
                    showToast(e.getMessage());
                });
    }

    private void getFirebaseToken(FirebaseUser user) {

        user.getIdToken(false)
                .addOnSuccessListener(result -> {

                    String token = result.getToken();

                    authenticateBackend(token);

                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Token Firebase", e);
                    showToast("Error obteniendo token");
                });
    }

    private void authenticateBackend(String firebaseToken) {

        TokenRequestDTO dto = new TokenRequestDTO(firebaseToken);

        RetrofitClient.getAuthApiService()
                .verifyAuthenticate(dto)
                .enqueue(new Callback<UserResponseDTO>() {

                    @Override
                    public void onResponse(@NonNull Call<UserResponseDTO> call,
                                           @NonNull Response<UserResponseDTO> response) {

                        if (!response.isSuccessful() || response.body() == null) {

                            Log.e(TAG, "Código HTTP: " + response.code());
                            showToast("Error autenticando usuario");
                            return;
                        }

                        sessionManager.saveSession(firebaseToken);

                        UserEntity user =
                                UserMapper.toEntity(response.body());

                        userViewModel.saveUser(user);

                        openHome();
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserResponseDTO> call,
                                          @NonNull Throwable t) {

                        Log.e(TAG, "Retrofit", t);
                        showToast(t.getMessage());
                    }
                });
    }

    private void openHome() {

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}