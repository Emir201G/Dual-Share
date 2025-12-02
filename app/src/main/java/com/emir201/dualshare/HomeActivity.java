package com.emir201.dualshare;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.emir201.dualshare.fragment.CreateFragment;
import com.emir201.dualshare.fragment.HistoryFragment;
import com.emir201.dualshare.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        window.setNavigationBarColor(getColor(R.color.white));


        bottomNavigationView.setBackgroundColor(getColor(R.color.white));
        // Fragment inicial al abrir la app
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new HistoryFragment())
                .commit();

        // Escuchar selección de ítems del BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.create) {
                selectedFragment = new CreateFragment();
                bottomNavigationView.setBackgroundColor(getColor(R.color.black));
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);


            } else if (itemId == R.id.history) {
                selectedFragment = new HistoryFragment();
                bottomNavigationView.setBackgroundColor(getColor(R.color.white));
                window.setStatusBarColor(getColor(R.color.white));
                window.setNavigationBarColor(getColor(R.color.white));
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


            } else if (itemId == R.id.perfil) {
                selectedFragment = new ProfileFragment();
                bottomNavigationView.setBackgroundColor(getColor(R.color.white));
                window.setStatusBarColor(getColor(R.color.white));
                window.setNavigationBarColor(getColor(R.color.white));
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, selectedFragment)
                        .commit();
            }
            return true;
        });


    }
}
