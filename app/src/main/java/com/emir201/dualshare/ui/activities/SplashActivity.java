package com.emir201.dualshare.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

// 💡 IMPORTANTE: Asegurate de importar tu SessionManager
import com.emir201.dualshare.R;
import com.emir201.dualshare.data.local.prefs.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window = getWindow();
        window.setNavigationBarColor(getColor(R.color.black));
        window.setStatusBarColor(getColor(R.color.black));

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

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionManager sessionManager = new SessionManager(SplashActivity.this);

                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1000);
    }
}