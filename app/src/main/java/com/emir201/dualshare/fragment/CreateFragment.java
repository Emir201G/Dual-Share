package com.emir201.dualshare.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.CameraActivity;
import com.emir201.dualshare.R;
import com.emir201.dualshare.VoiceRecorderActivity;
import com.google.android.material.card.MaterialCardView;

public class CreateFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        MaterialCardView cardCamera = view.findViewById(R.id.cardCamera);
        MaterialCardView cardVideo = view.findViewById(R.id.cardVideo);
        MaterialCardView cardRecorder = view.findViewById(R.id.cardRecorder);


        cardCamera.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CameraActivity.class);
            startActivity(intent);
        });

        cardRecorder.setOnClickListener(v->{
            Intent intent= new Intent(requireActivity(), VoiceRecorderActivity.class);
            startActivity(intent);
        });


        return view;
    }
}
