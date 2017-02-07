package com.accorpa.sawah;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.models.Category;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 18/01/17.
 */

public class CategoriesAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private Category[] mDataSource;

    public CategoriesAdapter(Context mContext, Category[] mDataSource) {
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
        CategotyView holder;

        Category recipe = (Category) getItem(position);

        if(convertView == null) {

            // 2
            convertView = mInflater.inflate(R.layout.category_list_item, parent, false);

            // 3
            holder = new CategotyView();
            holder.mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.icon);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.firstLine);
//            holder.subtitleTextView = (TextView) convertView.findViewById(R.id.secondLine);


            // 4
            convertView.setTag(holder);
        }
        else{
            // 5
            holder = (CategotyView) convertView.getTag();
        }

        holder.titleTextView.setText(recipe.getName());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= recipe.getImageLocation().replaceAll(" ", "%20");

//        mImageLoader.get(imageUrl, getImageListener(holder.userPhotoImageView, R.drawable.sawah_logo, R.drawable.gplus_login_logo));
        holder.mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
        AnimationDrawable frameAnimation = (AnimationDrawable) holder.mNetworkImageView.getBackground();
        frameAnimation.start();

        holder.mNetworkImageView.setImageUrl(imageUrl, mImageLoader);

        return convertView;
    }

    public static class CategotyView{

        public TextView titleTextView;
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
                    CategoriesAdapter.this.notifyDataSetChanged();
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }
        };
    }

}
