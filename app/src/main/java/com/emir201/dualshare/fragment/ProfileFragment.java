package com.emir201.dualshare.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView textView;
    private CircleImageView circleImageView;

    private ImageButton imageButtonText,imageButtonImg;
    private static final int PICK_IMAGE_REQUEST = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Referencias
        circleImageView= view.findViewById(R.id.imgProfile);
        textView = view.findViewById(R.id.tvName);
        imageButtonText = view.findViewById(R.id.imageButtonText);
        imageButtonImg = view.findViewById(R.id.imageButtonImg);
        // Cambiar nombre al presionar el TextView
        imageButtonText.setOnClickListener(v -> {
            final EditText input = new EditText(getContext());
            input.setHint("Escribe tu nuevo nombre");

            new AlertDialog.Builder(getContext())
                    .setTitle("Cambiar nombre")
                    .setView(input)
                    .setPositiveButton("Guardar", (dialog, which) -> {
                        String nuevoNombre = input.getText().toString().trim();
                        if (!nuevoNombre.isEmpty()) {
                            textView.setText(nuevoNombre);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // Cambiar imagen al presionar el CircleImageView
        imageButtonImg.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        return view;
    }

    // Recibir resultado de la galería
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            circleImageView.setImageURI(imageUri);
        }
    }
}
