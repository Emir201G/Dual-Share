package com.emir201.dualshare.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.emir201.dualshare.fragment.HistoryFragment;
import com.emir201.dualshare.fragment.ProfileFragment;

public class ViewPagerAdapterHome extends FragmentStateAdapter {

    public ViewPagerAdapterHome(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: {
                return new HistoryFragment();
            }
            case 1: {
                return new ProfileFragment();
            }
            default:
                return new HistoryFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 2;
    }
}
