package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomCheckBox;
import com.accorpa.sawah.models.Place;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 18/01/17.
 */

public class PlacesAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;

    private Place[] mDataSource;

    public PlacesAdapter(Context mContext, Place[] mDataSource) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mDataSource = mDataSource;

    }


    //1
    @Override
    public int getCount() {
        return mDataSource.length;
    }

    //2
    @Override
    public Place getItem(int position) {
        return mDataSource[position];
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        final PlacesAdapter.ViewHolder holder;

        final Place place = (Place) getItem(position);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.place_list_item, parent, false);

            holder = new PlacesAdapter.ViewHolder();
            holder.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.icon);
            holder.titleArabic = (TextView) convertView.findViewById(R.id.place_title_ar);
            holder.titleEnglish = (TextView) convertView.findViewById(R.id.place_title_en);
            holder.customCheckBox = (CustomCheckBox) convertView.findViewById(R.id.like_button);
            holder.customCheckBox.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);

            convertView.setTag(holder);
        }
        else{
            holder = (PlacesAdapter.ViewHolder) convertView.getTag();
        }

// 6
        TextView titleTextView = holder.titleArabic;
//        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.titleEnglish;
        ImageView mNetworkImageView = holder.mNetworkImageView;

        titleTextView.setText(place.getPalceNameArb());
        detailTextView.setText(place.getPalceNameEng());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");
//        mImageLoader.get(imageUrl, ImageLoader.getImageListener(holder.thumbnailImageView,
//                R.drawable.sawah_logo, R.drawable.gplus_login_logo));

        holder.mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        AnimationDrawable frameAnimation = (AnimationDrawable) holder.mNetworkImageView.getBackground();
        frameAnimation.start();

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);



        if(place.isFavourite()){
            holder.customCheckBox.setChecked();
        }else{
            holder.customCheckBox.setUnChecked();
        }

        holder.customCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                holder.customCheckBox.toggleState();
            }
        });


        return convertView;
    }

    public void setDataSource(Place[] dataSource) {
        this.mDataSource = dataSource;
    }

    public Place[] getDataSource() {
        return mDataSource;
    }

    private static class ViewHolder {
        public TextView titleArabic;
//        public TextView subtitleTextView;
        public TextView titleEnglish;
        public NetworkImageView mNetworkImageView;
        public CustomCheckBox customCheckBox;
    }

    public ImageLoader.ImageListener getImageListener(final ImageView view,
                                                      final int defaultImageResId, final int errorImageResId) {
        return new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                    PlacesAdapter.this.notifyDataSetChanged();
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }
}
