package com.emir201.dualshare.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.emir201.dualshare.fragment.CardImageFragment;
import com.emir201.dualshare.fragment.CardVideoFragment;
import com.emir201.dualshare.model.CardItem;

import java.util.List;

public class CardStackAdapter extends FragmentStateAdapter {

    private List<CardItem> cardItemList;

    public CardStackAdapter(@NonNull FragmentActivity fragmentActivity, List<CardItem> cardItemList) {
        super(fragmentActivity);
        this.cardItemList = cardItemList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        CardItem item = cardItemList.get(position);
        switch (item.getType()) {
            case IMAGE:
                return CardImageFragment.newInstance(item.getUri());

            case VIDEO:
                return CardVideoFragment.newInstance(item.getUri());


            default:
                return CardImageFragment.newInstance(item.getUri());
        }
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }
}
