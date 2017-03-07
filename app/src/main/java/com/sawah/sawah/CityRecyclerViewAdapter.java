package com.sawah.sawah;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sawah.sawah.models.Category;
import com.sawah.sawah.models.City;

/**
 * Created by root on 07/03/17.
 */

public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.CityViewHolder>{

    private Context mContext;
    private City[] mDataSource;
    private RecycleAdapterListener mListener;


    public CityRecyclerViewAdapter(Context mContext, RecycleAdapterListener mListener, City[] citiesList) {
        this.mContext = mContext;
        mDataSource = citiesList;
        this.mListener = mListener;
    }


    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_list_item, parent, false);

        CityViewHolder vh = new CityViewHolder(convertView);

        return vh;
    }

    @Override
    public void onBindViewHolder(final CityViewHolder holder, int position) {

        final City city = (City) mDataSource[position];

        holder.titleTextView.setText(city.getCityNameAR());
        holder.titleEnglish.setText(city.getCityNameEN());

        holder.startAnimation();


        Log.d("Glide", city.getImageLocation());

        Glide.with(mContext)
                .load(city.getImageLocation().replace(" ", "%20"))
                .error(R.drawable.demoitem)
                .centerCrop()
//                .skipMemoryCache(true)
                .crossFade().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.stopAnimation();
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        holder.startAnimation();
                        return false;
                    }
                }).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.itemSelected(city);

            }
        });

    }

    public void updateDataSource(City[] dataSource) {
        this.mDataSource = dataSource;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataSource.length;
    }

    @Override
    public void onViewRecycled(CityViewHolder holder) {
        super.onViewRecycled(holder);

        Glide.clear(holder.imageView);
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {


        public TextView titleTextView;
        public TextView titleEnglish;
        public ImageView imageView;

        private AnimationDrawable frameAnimation;

        public CityViewHolder(View itemView) {
            super(itemView);

            // 3
            titleTextView = (TextView) itemView.findViewById(R.id.title_ar);
            titleEnglish = (TextView) itemView.findViewById(R.id.title_en);

            imageView = (ImageView) itemView.findViewById(R.id.icon);
            imageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);

            LayerDrawable layer = (LayerDrawable) imageView.getBackground();
            frameAnimation = (AnimationDrawable) layer.getDrawable(0);

        }

        public void startAnimation() {
            frameAnimation.start();
        }

        public void stopAnimation() {
            frameAnimation.stop();
        }
    }

}


