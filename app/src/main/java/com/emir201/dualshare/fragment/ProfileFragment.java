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

import com.bumptech.glide.Glide;
import com.emir201.dualshare.R;

import de.hdodenhof.circleimageview.CircleImageView;
public class ProfileFragment extends Fragment {

    private TextView textView;
    private CircleImageView circleImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        circleImageView = view.findViewById(R.id.imgProfile);
        textView = view.findViewById(R.id.tvName);

        Intent intent = getActivity().getIntent();

        String name = intent.getStringExtra("name");
        Uri photo = intent.getParcelableExtra("photo");

        textView.setText(name);

        updateImagen(photo, circleImageView);

        return view;
    }

    private void updateImagen(Uri uri, CircleImageView circleImageView) {
        if (uri != null) {
            Glide.with(circleImageView.getContext())
                    .load(uri)
                    .into(circleImageView);
        }
    }
}

