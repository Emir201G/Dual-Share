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

        // Cambiar nombre al presionar el TextView


        return view;
    }

    // Recibir resultado de la galería

}
