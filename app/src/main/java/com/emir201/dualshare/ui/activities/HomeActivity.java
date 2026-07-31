package com.emir201.dualshare.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.R;
import com.emir201.dualshare.ui.fragment.CreateFragment;
import com.emir201.dualshare.ui.fragment.HistoryFragment;
import com.emir201.dualshare.ui.fragment.ProfileFragment;
import com.emir201.dualshare.data.remote.client.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitClient.init(this);

        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        window.setNavigationBarColor(getColor(R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        bottomNavigationView.setBackgroundColor(getColor(R.color.white));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new CreateFragment())
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.create);
        }

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

                bottomNavigationView.setBackgroundColor(getColor(R.color.white));
                window.setStatusBarColor(getColor(R.color.white));
                window.setNavigationBarColor(getColor(R.color.white));
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            return true;
        });
    }
}