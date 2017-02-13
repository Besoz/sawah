package com.accorpa.sawah.place;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 13/02/17.
 */

public class ScreenSlidePlaceFragment extends Fragment {

    public String titleArabic;
    public String titleEnglish;
    public String rating;
    public String imageURL;

    public ScreenSlidePlaceFragment(Place place) {
        titleArabic = place.getPalceNameArb();
        titleEnglish = place.getPalceNameEng();
        rating = place.getRatingID();
        imageURL = place.getImageLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_place, container, false);

        TextView titleArabic = (TextView) rootView.findViewById(R.id.place_title_ar);
        titleArabic.setText(this.titleArabic);

        TextView titleEnglish = (TextView) rootView.findViewById(R.id.place_title_en);
        titleArabic.setText(this.titleEnglish);

        TextView rating = (TextView) rootView.findViewById(R.id.rating);
        titleArabic.setText(this.rating);

        NetworkImageView mNetworkImageView = (NetworkImageView) rootView.findViewById(R.id.icon);

        ImageLoader mImageLoader = ServiceHandler.getInstance(container.getContext()).getImageLoader();
        String imageUrl= imageURL.replaceAll(" ", "%20");
//        mImageLoader.get(imageUrl, ImageLoader.getImageListener(holder.thumbnailImageView,
//                R.drawable.sawah_logo, R.drawable.gplus_login_logo));

        mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        AnimationDrawable frameAnimation = (AnimationDrawable) mNetworkImageView.getBackground();
        frameAnimation.start();

        mNetworkImageView.setImageUrl(imageUrl, mImageLoader);

        return rootView;
    }
}
