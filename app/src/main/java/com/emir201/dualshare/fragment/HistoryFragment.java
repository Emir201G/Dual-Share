package com.emir201.dualshare.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.R;
import com.emir201.dualshare.model.CardItem;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private List<CardItem> items = new ArrayList<>();
    private int delay = 4000;
    private Runnable autoSwipeRunnable;
    private CardStackLayoutManager layoutManager;
    private CardStackView cardStackViewHistory;
    private LinearLayout layoutNoFriends, layoutNoStories;
    private Handler handler = new Handler(Looper.getMainLooper());


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        cardStackViewHistory = view.findViewById(R.id.cardStackViewHistory);
        layoutNoFriends = view.findViewById(R.id.layoutNoFriends);
        layoutNoStories = view.findViewById(R.id.layoutNoStories);


        return view;
    }

    // ------------------ AUTO SWIPE ------------------
    private void startAutoSwipe() {

        autoSwipeRunnable = new Runnable() {
            @Override
            public void run() {

                SwipeAnimationSetting setting =
                        new SwipeAnimationSetting.Builder()
                                .setDirection(Direction.Left)
                                .setDuration(Duration.Normal.ordinal())
                                .build();

                layoutManager.setSwipeAnimationSetting(setting);

                cardStackViewHistory.swipe();

                handler.postDelayed(this, delay);
            }
        };

        handler.postDelayed(autoSwipeRunnable, delay);
    }

    // ------------------ SHOW CASE 1: NO FRIENDS ------------------
    private void showCase1() {
        layoutNoFriends.setVisibility(View.VISIBLE);
        layoutNoStories.setVisibility(View.GONE);
        cardStackViewHistory.setVisibility(View.GONE);
    }

    // ------------------ SHOW CASE 2: NO STORIES ------------------
    private void showCase2() {
        layoutNoFriends.setVisibility(View.GONE);
        layoutNoStories.setVisibility(View.VISIBLE);
        cardStackViewHistory.setVisibility(View.GONE);
    }

    // ------------------ SHOW CASE 3: STORY ------------------
    private void showCase3() {
        layoutNoFriends.setVisibility(View.GONE);
        layoutNoStories.setVisibility(View.GONE);
        cardStackViewHistory.setVisibility(View.VISIBLE);
    }

    private void setupCardStackView() {

    }
}
