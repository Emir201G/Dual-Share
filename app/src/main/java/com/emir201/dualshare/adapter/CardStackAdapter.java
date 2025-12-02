package com.emir201.dualshare.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emir201.dualshare.R;
import com.emir201.dualshare.model.CardItem;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_VIDEO = 1;
    private List<CardItem> cardItems;

    public CardStackAdapter(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @Override
    public int getItemViewType(int position) {
        return cardItems.get(position).getType() == CardItem.Type.IMAGE
                ? TYPE_IMAGE
                : TYPE_VIDEO;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_IMAGE) {
            View view = inflater.inflate(R.layout.item_card_image, parent, false);
            return new ImageViewHolder(view);

        } else {
            View view = inflater.inflate(R.layout.item_card_video, parent, false);
            return new VideoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CardItem item = cardItems.get(position);

        if (holder instanceof ImageViewHolder) {
            ((ImageViewHolder) holder).bind(item);
        }
        else if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).bind(item);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

        if (holder instanceof VideoViewHolder) {
            ((VideoViewHolder) holder).releasePlayer();
        }
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }


    /* -------- IMAGE HOLDER -------- */

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private ProgressBar progressBarImage;
        private ObjectAnimator animator;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageCard);
            progressBarImage = itemView.findViewById(R.id.storyProgress);
        }

        void bind(CardItem item) {
            imageView.setImageURI(Uri.parse(item.getUri()));
        }

        public void startProgressBar(int duration) {

            if (animator != null)
                animator.cancel();

            progressBarImage.setMax(duration);
            progressBarImage.setProgress(0);

            animator = ObjectAnimator.ofInt(progressBarImage, "progress", 0, duration);
            animator.setDuration(duration);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
        }
    }


    /* -------- VIDEO HOLDER -------- */

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        PlayerView playerView;
        ExoPlayer player;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            playerView = itemView.findViewById(R.id.videoPlayer);
        }

        void bind(CardItem item) {
            player = new ExoPlayer.Builder(itemView.getContext()).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(item.getUri()));
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);
        }

        void releasePlayer() {
            if (player != null) {
                player.release();
                player = null;
            }
        }
    }
}
