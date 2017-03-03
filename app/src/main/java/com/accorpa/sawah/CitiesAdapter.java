package com.accorpa.sawah;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.models.City;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 23/01/17.
 */
public class CitiesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private City[] mDataSource;

    public CitiesAdapter(Context mContext, City[] mDataSource) {

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
    public Object getItem(int position) {
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
        CityView holder;

        City recipe = (City) getItem(position);

        if(convertView == null) {

            // 2
            convertView = mInflater.inflate(R.layout.city_list_item, parent, false);

            // 3
            holder = new CityView();
            holder.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title_ar);
            holder.titleEnglish = (TextView) convertView.findViewById(R.id.title_en);


            // 4
            convertView.setTag(holder);
        }
        else{
            // 5
            holder = (CityView) convertView.getTag();
        }

        holder.titleTextView.setText(recipe.getCityNameAR());
        holder.titleEnglish.setText(recipe.getCityNameEN());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= recipe.getImageLocation().replaceAll(" ", "%20");

//        mImageLoader.get(imageUrl, getImageListener(holder.userPhotoImageView, R.drawable.sawah_logo, R.drawable.gplus_login_logo));
        holder.mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        LayerDrawable layer = (LayerDrawable) holder.mNetworkImageView.getBackground();
        AnimationDrawable frameAnimation = (AnimationDrawable) layer.getDrawable(0);

        frameAnimation.start();

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);

        return convertView;
    }

    public void updateDataSource(City[] dataSource) {
        this.mDataSource = dataSource;
        notifyDataSetChanged();
    }

    public static class CityView{

        public TextView titleTextView;
        public TextView titleEnglish;
        public NetworkImageView mNetworkImageView;

    }

}
