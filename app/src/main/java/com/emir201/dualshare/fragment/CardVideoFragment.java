package com.emir201.dualshare.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import com.emir201.dualshare.R;

public class CardVideoFragment extends Fragment {

    private static final String ARG_URI = "video_uri";
    private String uri;
    private ExoPlayer player;

    public static CardVideoFragment newInstance(String uri) {
        CardVideoFragment fragment = new CardVideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uri = getArguments().getString(ARG_URI);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedIntenceState) {

        View view = inflater.inflate(R.layout.fragment_card_video, container, false);
        PlayerView playerView = view.findViewById(R.id.videoPlayerCard);
        player = new ExoPlayer.Builder(requireContext()).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(Uri.parse(uri));
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (player != null)
            player.setPlayWhenReady(false);
    }

    @Override
    public void onDestroy() {
        if (player != null)
            player.release();
        super.onDestroy();
    }
}
