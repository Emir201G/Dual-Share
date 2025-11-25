package com.emir201.dualshare.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emir201.dualshare.R;

public class CardImageFragment extends Fragment {

    private static final String ARG_URI = "image_uri";
    private String uri;

    public static CardImageFragment newInstance(String uri) {
        CardImageFragment fragment = new CardImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URI, uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card_image, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewCard);
        imageView.setImageURI(Uri.parse(uri));

        return view;
    }

}
