package com.emir201.dualshare;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.emir201.dualshare.adapter.ViewPagerAdapterShare;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ShareActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapterShare adapter;

    @Override
    protected void onCreate(Bundle savedIntanceState) {
        super.onCreate(savedIntanceState);
        setContentView(R.layout.activity_share);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.ultra_ligth_gray));
        window.setNavigationBarColor(getColor(R.color.ultra_ligth_gray));

        adapter = new ViewPagerAdapterShare(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0)
                tab.setText("Ingresar Código");
            else if (position == 1)
                tab.setText("Compartir Código");
        }).attach();
    }
}
