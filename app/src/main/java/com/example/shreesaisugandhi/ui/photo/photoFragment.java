package com.example.shreesaisugandhi.ui.photo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.shreesaisugandhi.R;
import com.github.chrisbanes.photoview.PhotoView;

public class photoFragment extends Fragment {
    PhotoView photoView;
    String image;
    public photoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = bundle.getString("image");
        }
        photoView = view.findViewById(R.id.photo_view);
        Glide.with(getContext()).load(image).into(photoView);

        return view;
    }
}