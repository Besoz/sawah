package com.accorpa.sawah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

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
            convertView = mInflater.inflate(R.layout.category_list_item, parent, false);

            // 3
            holder = new PlacesAdapter.ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.firstLine);
//            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.secondLine);


            // 4
            convertView.setTag(holder);
        }
        else{
            // 5
            holder = (PlacesAdapter.ViewHolder) convertView.getTag();
        }

// 6
        TextView titleTextView = holder.titleTextView;
//        TextView subtitleTextView = holder.subtitleTextView;
        TextView detailTextView = holder.detailTextView;
        ImageView thumbnailImageView = holder.thumbnailImageView;

        titleTextView.setText(recipe.getPalceNameEng());
//        subtitleTextView.setText(recipe.getPalceNameArb());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= recipe.getImageLocation().replaceAll(" ", "%20");
        mImageLoader.get(imageUrl, ImageLoader.getImageListener(holder.thumbnailImageView,
                R.drawable.sawah_logo, R.drawable.gplus_login_logo));

        return convertView;
    }

    private static class ViewHolder {
        public TextView titleTextView;
//        public TextView subtitleTextView;
        public TextView detailTextView;
        public ImageView thumbnailImageView;
    }
}
