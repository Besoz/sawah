package com.accorpa.sawah;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.models.Category;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

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

        Picasso.with(mContext)
                .load(category.getImageLocation())
                .into(holder.mNetworkImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        holder.stopAnimation();
                    }

                    @Override
                    public void onError() {

                    }
                });
        holder.startAnimation();


//        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);

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
            frameAnimation = (AnimationDrawable) mNetworkImageView.getBackground();
            this.titleTextView = (TextView) v.findViewById(R.id.category_title);
        }

        public void startAnimation() {
            frameAnimation.start();
        }

        public void stopAnimation() {
            frameAnimation.stop();
        }
    }

}
