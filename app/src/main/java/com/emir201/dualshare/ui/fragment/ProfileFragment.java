package com.emir201.dualshare.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emir201.dualshare.data.local.entity.UserEntity;
import com.emir201.dualshare.ui.activities.MainActivity;
import com.emir201.dualshare.R;
import com.emir201.dualshare.ui.adapter.ProfileAdapter;
import com.emir201.dualshare.model.ProfileItem;
import com.emir201.dualshare.data.local.prefs.SessionManager;
import com.emir201.dualshare.viewmodel.UserViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements ProfileAdapter.OnItemClickListener {

    private RecyclerView rvOptions;
    private ProfileAdapter profileAdapter;
    private List<ProfileItem> profileDataList;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleClient;
    private ActivityResultLauncher<String> pickImageLauncher;
    private SessionManager sessionManager;
    private UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        rvOptions = view.findViewById(R.id.rvOptions);
        rvOptions.setLayoutManager(new LinearLayoutManager(requireContext()));

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        profileDataList = new ArrayList<>();

        profileDataList.add(new ProfileItem(ProfileItem.TYPE_HEADER, "Header"));

        profileDataList.add(new ProfileItem(ProfileItem.TYPE_TITLE, "Cuenta"));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Cambiar foto de perfil", R.drawable.profile));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Cambiar nombre", R.drawable.edit_icon));

        profileDataList.add(new ProfileItem(ProfileItem.TYPE_TITLE, "Red de amigos"));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Amigos", R.drawable.friend_icon));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Solicitudes", R.drawable.add_friend_icon));

        profileDataList.add(new ProfileItem(ProfileItem.TYPE_TITLE, "Configuración"));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Notificaciones", R.drawable.notification_icon));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Tema", R.drawable.theme_icon));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Idioma", R.drawable.language_icon));

        profileDataList.add(new ProfileItem(ProfileItem.TYPE_TITLE, "Sesión"));
        profileDataList.add(new ProfileItem(ProfileItem.TYPE_OPTION, "Cerrar sesión", R.drawable.logout_icon));

        profileDataList.add(new ProfileItem(ProfileItem.TYPE_FOOTER, "Footer"));

        profileAdapter = new ProfileAdapter(profileDataList, this);
        rvOptions.setAdapter(profileAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(requireContext(), gso);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        uploadImageToBackend(uri);
                    }
                });

        userViewModel.getCurrentUser()
                .observe(getViewLifecycleOwner(), user -> {
                    if (user != null) {
                        profileAdapter.updateUserData(user);
                    }
                });

        return view;
    }

    @Override
    public void onOptionClick(String optionName) {
        switch (optionName.trim()) {
            case "Cambiar foto de perfil":
                pickImageLauncher.launch("image/*");
                break;
            case "Cambiar nombre":
                showEditNameBottomSheet();
                break;
            case "Amigos":
                Toast.makeText(requireContext(), "Abriendo lista de amigos...", Toast.LENGTH_SHORT).show();
                break;
            case "Solicitudes":
                Toast.makeText(requireContext(), "Abriendo solicitudes de amistad...", Toast.LENGTH_SHORT).show();
                break;
            case "Notificaciones":
                Toast.makeText(requireContext(), "Abriendo panel de notificaciones...", Toast.LENGTH_SHORT).show();
                break;
            case "Tema":
                Toast.makeText(requireContext(), "Configurando apariencia visual...", Toast.LENGTH_SHORT).show();
                break;
            case "Idioma":
                Toast.makeText(requireContext(), "Cargando selector de idioma...", Toast.LENGTH_SHORT).show();
                break;
            case "Cerrar sesión":
                showLogoutDialog();
                break;
        }
    }

    private void showEditNameBottomSheet() {

        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.botton_sheet_layout, null);

        TextInputEditText editText = view.findViewById(R.id.textInputEditText);
        MaterialButton btnSave = view.findViewById(R.id.materialButton);

        UserEntity user = userViewModel.getCurrentUser().getValue();

        if (user != null) {
            editText.setText(user.getUsername());
            editText.setSelection(user.getUsername().length());
        }

        btnSave.setOnClickListener(v -> {

            String username = editText.getText() != null
                    ? editText.getText().toString().trim()
                    : "";

            if (username.isEmpty()) {
                editText.setError("Ingrese un nombre de usuario");
                return;
            }

            if (user == null) {
                Toast.makeText(requireContext(),
                        "No se pudo obtener el usuario",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (username.equals(user.getUsername())) {
                dialog.dismiss();
                return;
            }

            userViewModel.updateUsername(username);
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void showLogoutDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cerrar sesión")
                .setMessage("¿Seguro que deseas cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> logout())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void logout() {
        sessionManager = new SessionManager(requireContext());
        sessionManager.logout();

        firebaseAuth.signOut();
        googleClient.signOut().addOnCompleteListener(task -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    private void uploadImageToBackend(Uri uri) {
        // TODO: En el futuro, acá harás un envío Multipart a tu API de Spring Boot
        // para subir la foto a Cloudinary y persistir la URL en MySQL.
        Toast.makeText(requireContext(), "Subiendo imagen al servidor...", Toast.LENGTH_SHORT).show();
    }

}