package com.emir201.dualshare;

import android.os.Bundle;
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
            } else if (itemId == R.id.history) {
                selectedFragment = new HistoryFragment();
            } else if (itemId == R.id.perfil) {
                selectedFragment = new ProfileFragment();
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
