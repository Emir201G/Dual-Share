package com.emir201.dualshare.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.CameraActivity;
import com.emir201.dualshare.R;
import com.emir201.dualshare.VoiceRecorderActivity;
import com.google.android.material.card.MaterialCardView;

import android.os.Build; // Importar Build
import android.view.View; // Importar View
import android.widget.ImageButton;
// ... (otras importaciones)

public class CreateFragment extends Fragment {

    private ImageButton buttonDual;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        Window window = requireActivity().getWindow();

        window.setNavigationBarColor(getResources().getColor(R.color.black));
        window.setStatusBarColor(getResources().getColor(R.color.black));

        buttonDual = view.findViewById(R.id.imageButtonDual);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int currentFlags = window.getDecorView().getSystemUiVisibility();
            currentFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.getDecorView().setSystemUiVisibility(currentFlags);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int currentNavFlags = window.getDecorView().getSystemUiVisibility();
            currentNavFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            window.getDecorView().setSystemUiVisibility(currentNavFlags);
        }

        float[] scaleValues = {1.0f, 0.9f, 1.1f, 1.0f};
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(buttonDual, "scaleX", scaleValues);
        scaleX.setDuration(500);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(buttonDual, "scaleY", scaleValues);
        scaleX.setDuration(500);


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });

        animatorSet.start();
        return view;
    }
}
