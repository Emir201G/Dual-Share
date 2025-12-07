package com.emir201.dualshare.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.CameraActivity;
import com.emir201.dualshare.R;
import com.emir201.dualshare.ShareActivity;
import com.google.android.material.button.MaterialButton;

public class CreateFragment extends Fragment {

    private ImageButton buttonDual;
    private MaterialButton btnShareCode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create, container, false);

        // PULSE ANIMATION (estilo Shazam)
        View pulse = view.findViewById(R.id.pulseView);

        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse);
        pulse.startAnimation(animation);

        // Referencias
        buttonDual = view.findViewById(R.id.imageButtonDual);
        btnShareCode = view.findViewById(R.id.btnShareCode);

        // Abrir actividad para compartir código
        btnShareCode.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ShareActivity.class);
            startActivity(intent);
        });

        // Efecto shrink + rebound cuando se presiona
        buttonDual.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(100)
                    .withEndAction(() ->
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(150)
                    )
                    .start();
        });

        return view;
    }
}
