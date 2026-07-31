package com.emir201.dualshare.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.emir201.dualshare.ui.fragment.InputCodeFragment;
import com.emir201.dualshare.ui.fragment.ShareCodeFragment;

public class ViewPagerAdapterShare extends FragmentStateAdapter {

    public ViewPagerAdapterShare(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new InputCodeFragment();
            case 1:
                return new ShareCodeFragment();

            default:
                return new InputCodeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}