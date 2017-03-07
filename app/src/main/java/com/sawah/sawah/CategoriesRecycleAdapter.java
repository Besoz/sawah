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
import com.sawah.sawah.Handlers.ServiceHandler;
import com.sawah.sawah.models.Category;

/**
 * Created by root on 26/02/17.
 */

public class CategoriesRecycleAdapter extends  RecyclerView.Adapter<CategoriesRecycleAdapter.CategoryViewHolder> {

    private Context mContext;
//    private LayoutInflater mInflater;

    private Category[] mDataSource;

    private RecycleAdapterListener mListener;


    public CategoriesRecycleAdapter(Context mContext, RecycleAdapterListener mListener, Category[] categoryList) {
        this.mContext = mContext;
        mDataSource = categoryList;
        this.mListener = mListener;
    }

    @Override
    public CategoriesRecycleAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        CategoryViewHolder vh = new CategoryViewHolder(convertView);

        return vh;
    }

    @Override
    public void onBindViewHolder(final CategoriesRecycleAdapter.CategoryViewHolder holder, int position) {

        final Category category = mDataSource[position];

        holder.titleTextView.setText(category.getName());

//        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= category.getImageLocation().replace(" ", "%20");

//        Picasso.with(mContext)
//                .load(category.getImageLocation())
//                .into(holder.mNetworkImageView, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        holder.stopAnimation();
//                    }
//
//                    @Override
//                    public void onError() {
//
//                    }
//                });

        Log.d("Glide", category.getImageLocation());

        Glide.with(mContext)
                .load(category.getImageLocation().replace(" ", "%20"))
                .error(R.drawable.demoitem)
                .centerCrop()
//                .skipMemoryCache(true)
                .crossFade().listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        holder.stopAnimation();
                        return false;
                    }
                })
                .into(holder.mNetworkImageView);

        holder.startAnimation();


//        holder.imageView.setImageUrl(imageUrl, mImageLoader);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.itemSelected(category);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSource.length;
    }

//    @Override
//    public double aspectRatioForIndex(int i) {
//        return 1;
//    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleTextView;
        public ImageView mNetworkImageView;
        private AnimationDrawable frameAnimation;

        public CategoryViewHolder(View v) {
            super(v);

            this.mNetworkImageView = (ImageView) v.findViewById(R.id.icon);
            mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
            LayerDrawable layer = (LayerDrawable) mNetworkImageView.getBackground();
            frameAnimation = (AnimationDrawable) layer.getDrawable(0);
            frameAnimation.start();
            this.titleTextView = (TextView) v.findViewById(R.id.category_title);
        }

        public void startAnimation() {
            frameAnimation.start();
        }

        public void stopAnimation() {
            frameAnimation.stop();
        }
    }
    public void updateDataSource(Category[] dataSource) {
        this.mDataSource = dataSource;
        notifyDataSetChanged();
    }

}
