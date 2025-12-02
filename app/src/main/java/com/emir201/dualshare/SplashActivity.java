package com.emir201.dualshare;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Window window= getWindow();
        window.setNavigationBarColor(getColor(R.color.black));
        window.setStatusBarColor(getColor(R.color.black));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Obtener las banderas de visibilidad actuales
            int currentFlags = window.getDecorView().getSystemUiVisibility();

            // Remover el flag de contenido CLARO (LIGHT_STATUS_BAR) para forzar contenido OSCURO
            // El contenido OSCURO (negro) de la barra es lo que hace que los iconos sean BLANCOS
            currentFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

            window.getDecorView().setSystemUiVisibility(currentFlags);
        }

        // Si usas Android 8.0 (API 26) o superior, puedes hacer lo mismo para la barra de navegación:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int currentNavFlags = window.getDecorView().getSystemUiVisibility();
            currentNavFlags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
            window.getDecorView().setSystemUiVisibility(currentNavFlags);
        }


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 1000);
    }
}
