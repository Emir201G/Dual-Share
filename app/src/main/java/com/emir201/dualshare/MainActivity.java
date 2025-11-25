package com.emir201.dualshare;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.ultra_ligth_gray));
        window.setNavigationBarColor(getColor(R.color.ultra_ligth_gray));
    }


}
