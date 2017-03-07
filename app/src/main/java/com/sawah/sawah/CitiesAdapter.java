package com.sawah.sawah;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sawah.sawah.Handlers.ServiceHandler;
import com.sawah.sawah.models.City;

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

        City city = (City) getItem(position);

        if(convertView == null) {

            // 2
            convertView = mInflater.inflate(R.layout.city_list_item, parent, false);

            // 3
            holder = new CityView();
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.title_ar);
            holder.titleEnglish = (TextView) convertView.findViewById(R.id.title_en);


            // 4
            convertView.setTag(holder);
        }
        else{
            // 5
            holder = (CityView) convertView.getTag();
        }

        holder.titleTextView.setText(city.getCityNameAR());
        holder.titleEnglish.setText(city.getCityNameEN());

//        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
//        String imageUrl= recipe.getImageLocation().replaceAll(" ", "%20");

//        mImageLoader.get(imageUrl, getImageListener(holder.userPhotoImageView, R.drawable.sawah_logo, R.drawable.gplus_login_logo));
        holder.imageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        LayerDrawable layer = (LayerDrawable) holder.imageView.getBackground();
        final AnimationDrawable frameAnimation = (AnimationDrawable) layer.getDrawable(0);

        frameAnimation.start();

//        holder.imageView.setImageUrl(imageUrl, mImageLoader);


        Log.d("Glide", city.getImageLocation());

        Glide.with(mContext)
                .load(city.getImageLocation().replace(" ", "%20"))
                .error(R.drawable.demoitem)
                .centerCrop()
//                .skipMemoryCache(true)
                .crossFade().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        frameAnimation.stop();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        frameAnimation.stop();
                        return false;
                    }
                })
                .into(holder.imageView);



        return convertView;
    }

    public void updateDataSource(City[] dataSource) {
        this.mDataSource = dataSource;
        notifyDataSetChanged();
    }

    public static class CityView{

        public TextView titleTextView;
        public TextView titleEnglish;
        public ImageView imageView;

    }

}
