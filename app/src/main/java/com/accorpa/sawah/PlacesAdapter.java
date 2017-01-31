package com.accorpa.sawah;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        PlacesAdapter.ViewHolder holder;

        Place recipe = (Place) getItem(position);

// 2


// 1
        if(convertView == null) {

            // 2
            convertView = mInflater.inflate(R.layout.place_list_item, parent, false);

            // 3
            holder = new PlacesAdapter.ViewHolder();
            holder.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.icon);
            holder.titleArabic = (TextView) convertView.findViewById(R.id.place_title_ar);
            holder.titleEnglish = (TextView) convertView.findViewById(R.id.place_title_en);


            // 4
            convertView.setTag(holder);
        }
        else{
            // 5
            holder = (PlacesAdapter.ViewHolder) convertView.getTag();
        }

// 6
        TextView titleTextView = holder.titleArabic;
//        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.titleEnglish;
        ImageView mNetworkImageView = holder.mNetworkImageView;

        titleTextView.setText(recipe.getPalceNameArb());
        detailTextView.setText(recipe.getPalceNameEng());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= recipe.getImageLocation().replaceAll(" ", "%20");
//        mImageLoader.get(imageUrl, ImageLoader.getImageListener(holder.thumbnailImageView,
//                R.drawable.sawah_logo, R.drawable.gplus_login_logo));

        holder.mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        AnimationDrawable frameAnimation = (AnimationDrawable) holder.mNetworkImageView.getBackground();
        frameAnimation.start();

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleArabic;
//        public TextView subtitleTextView;
        public TextView titleEnglish;
        public NetworkImageView mNetworkImageView;
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
