package com.emir201.dualshare.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.emir201.dualshare.R;
import com.emir201.dualshare.adapter.CardStackAdapter;
import com.emir201.dualshare.model.CardItem;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private List<CardItem> items = new ArrayList<>();
    private int delay = 4000;
    private Runnable autoSwipeRunnable;
    private CardStackLayoutManager layoutManager;
    private CardStackView cardStackView;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        cardStackView = view.findViewById(R.id.cardStackView);

        // ------------------ LISTENER CORRECTO ------------------
        layoutManager = new CardStackLayoutManager(
                requireContext(),
                new CardStackListener() {

                    @Override public void onCardDragging(Direction direction, float ratio) {}

                    @Override
                    public void onCardSwiped(Direction direction) {}

                    @Override public void onCardRewound() {}

                    @Override public void onCardCanceled() {}

                    @Override
                    public void onCardAppeared(View view, int position) {

                        RecyclerView.ViewHolder holder =
                                cardStackView.findViewHolderForAdapterPosition(position);

                        if (holder instanceof CardStackAdapter.ImageViewHolder) {
                            ((CardStackAdapter.ImageViewHolder) holder).startProgressBar(4000);
                        }
                    }

                    @Override public void onCardDisappeared(View view, int position) {}
                }
        );

        layoutManager.setStackFrom(StackFrom.Top);
        layoutManager.setVisibleCount(2);
        layoutManager.setTranslationInterval(8.0f);
        layoutManager.setScaleInterval(0.95f);
        layoutManager.setSwipeThreshold(0.3f);
        layoutManager.setMaxDegree(20.0f);
        layoutManager.setDirections(Direction.HORIZONTAL);
        layoutManager.setCanScrollHorizontal(true);
        layoutManager.setCanScrollVertical(true);

        cardStackView.setLayoutManager(layoutManager);

        // ------------------ DATA ------------------
        String uriImage = "android.resource://" + requireActivity().getPackageName() + "/" + R.drawable.dog;
        String uriVideo = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.basquet;

        items.add(new CardItem(CardItem.Type.IMAGE, uriImage));
        items.add(new CardItem(CardItem.Type.IMAGE, uriImage));
        items.add(new CardItem(CardItem.Type.IMAGE, uriImage));
        items.add(new CardItem(CardItem.Type.IMAGE, uriImage));
        items.add(new CardItem(CardItem.Type.IMAGE, uriImage));
        items.add(new CardItem(CardItem.Type.VIDEO, uriVideo));

        CardStackAdapter adapter = new CardStackAdapter(items);
        cardStackView.setAdapter(adapter);

        startAutoSwipe();

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

                cardStackView.swipe();

                handler.postDelayed(this, delay);
            }
        };

        handler.postDelayed(autoSwipeRunnable, delay);
    }
}
