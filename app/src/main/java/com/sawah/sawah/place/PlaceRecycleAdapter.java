package com.sawah.sawah.place;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.ServiceHandler;
import com.sawah.sawah.R;
import com.sawah.sawah.RecycleAdapterListener;
import com.sawah.sawah.custom_views.CustomCheckBox;
import com.sawah.sawah.custom_views.CustomTextView;
import com.sawah.sawah.models.Place;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;


import java.net.PortUnreachableException;
import java.util.ArrayList;

/**
 * Created by root on 26/02/17.
 */

public class PlaceRecycleAdapter extends  RecyclerView.Adapter<PlaceRecycleAdapter.ViewHolder> {


    private Context mContext;
//    private LayoutInflater mInflater;
    private View convertView;
    private ArrayList<Place> mDataSource;

    private RecycleAdapterListener mListener;

    private boolean addLikeButton, addDeleteButton, specialPlaceLayout;

    private Animation rotation;

    public PlaceRecycleAdapter(Context mContext, RecycleAdapterListener mListener,
                               boolean addLikeButton,
                               boolean specialPlaceLayout,
                               boolean addDeleteButton) {
        this.mContext = mContext;

        this.mDataSource = new ArrayList<>();
        this.mListener =  mListener;

        this.addLikeButton = addLikeButton;
        this.specialPlaceLayout = specialPlaceLayout;
        this.addDeleteButton = addDeleteButton;

        rotation = AnimationUtils.loadAnimation(mContext, R.anim.shake);
    }

    @Override
    public PlaceRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        convertView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_list_item, parent, false);
        ViewHolder vh = new ViewHolder(convertView, addLikeButton);
        return vh;
    }

    private void loadSpecialCard(final PlaceRecycleAdapter.ViewHolder holder, int position, final Place place)
    {
        holder.specialCard.setVisibility(View.VISIBLE);
        holder.notSpecialCard.setVisibility(View.GONE);

        holder.titleArabic.setText(place.getPalceNameArb());
        holder.titleEnglish.setText(place.getPalceNameEng());

        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");


        LayerDrawable layer = (LayerDrawable) holder.mNetworkImageView.getBackground();
        if(layer != null) {
            AnimationDrawable frameAnimation = (AnimationDrawable) layer.getDrawable(0);
            frameAnimation.start();
        }

        if(imageUrl == "")
            imageUrl = "http://sawahapp.com/Uploads/ssss.ssss";

        Log.d("Glide", imageUrl);

        Glide.with(mContext)
                .load(imageUrl)
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
        }).into(holder.mNetworkImageView);

        holder.specialTag.setVisibility(place.isSpecial() && specialPlaceLayout ?
                View.VISIBLE : View.GONE);

        if(addLikeButton){
            if(place.isFavourite()){
                holder.likeButton.setChecked();
            }else{
                holder.likeButton.setUnChecked();
            }
            holder.likeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                    holder.likeButton.toggleState();
                }
            });
        }
        holder.specialTag.setVisibility(specialPlaceLayout && place.isSpecial() ? View.VISIBLE : View.GONE);
    }

    private void loadCard(final PlaceRecycleAdapter.ViewHolder holder, int position, final Place place)
    {
        holder.specialCard.setVisibility(View.GONE);
        holder.notSpecialCard.setVisibility(View.VISIBLE);

        holder.titleArabicSpecial.setText(place.getPalceNameArb());
        holder.titleEnglishSpecial.setText(place.getPalceNameEng());

        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");
        LayerDrawable layer = (LayerDrawable) holder.mNetworkImageViewSpecial.getBackground();
        final AnimationDrawable frameAnimation;
        if(layer != null) {
            frameAnimation = (AnimationDrawable) layer.getDrawable(0);
            frameAnimation.start();
        }

        if(imageUrl == "")
            imageUrl = "http://sawahapp.com/Uploads/ssss.ssss";

        Log.d("Glide", imageUrl);
        Glide.with(mContext)
                .load(imageUrl)
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

                holder.stopSpecialAnimation();
                return false;
            }
        }).into(holder.mNetworkImageViewSpecial);

        holder.specialTagSpecial.setVisibility(place.isSpecial() && specialPlaceLayout ?
                View.VISIBLE : View.GONE);

        if(place.isFavourite()){
            holder.likeButtonSpecial.setChecked();
        }else{
            holder.likeButtonSpecial.setUnChecked();
        }

        holder.likeButtonSpecial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DataHandler.getInstance(mContext).togglePlaceFavourite(place);
                holder.likeButtonSpecial.toggleState();
            }
        });
        holder.specialTagSpecial.setVisibility(specialPlaceLayout && place.isSpecial() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBindViewHolder(final PlaceRecycleAdapter.ViewHolder holder, int position) {

        final Place place = mDataSource.get(position);
        System.out.println("Place: "+place.getPalceNameEng() + "----" + place.isSpecial());
        if(place.isSpecial() || !specialPlaceLayout)
        {
            loadSpecialCard(holder, position, place);
        }
        else {
            loadCard(holder, position, place);
        }

        if(addDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.shake);
            holder.v.startAnimation(animation);
        }else {
            holder.deleteButton.setVisibility(View.GONE);
            holder.v.clearAnimation();
        }

        if(addDeleteButton){
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.v.clearAnimation();
                    holder.v.setVisibility(View.GONE);
                    DataHandler.getInstance(mContext).togglePlaceFavourite(place);

                    int p = holder.getAdapterPosition();
                    System.out.println("delete: "+p);
                    mDataSource.remove(p);
                    notifyItemRemoved(p);

                    System.out.println(mDataSource.size());
                    if(mDataSource.size() == 0)
                        mListener.showHideEmptyText();
                }
            });
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.v.clearAnimation();
                mListener.itemSelected(place);
            }
        });
    }

    @Override
    public void onViewRecycled(PlaceRecycleAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
//        if (holder instanceof PlaceRecycleAdapter.ViewHolder) {
//            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            Glide.clear(holder.mNetworkImageView);
        Glide.clear(holder.mNetworkImageViewSpecial);
//        }
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

//    @Override
//    public double aspectRatioForIndex(int i) {
//        return 1;
//    }

    @Override
    public void onViewDetachedFromWindow(final PlaceRecycleAdapter.ViewHolder holder)
    {
        holder.v.clearAnimation();
    }

    public void setDataSource(ArrayList<Place> dataSource) {
        this.mDataSource = dataSource;
    }

    public void setShowDeletionButton(boolean showDeleteButton) {
        addDeleteButton = showDeleteButton;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView titleArabic;
        public TextView titleEnglish;
        public ImageView mNetworkImageView;
        public CustomCheckBox likeButton;
        public CustomCheckBox deleteButton;
        public LinearLayout specialTag;
        private AnimationDrawable specialFrameAnimation;

        public TextView titleArabicSpecial;
        public TextView titleEnglishSpecial;
        public ImageView mNetworkImageViewSpecial;
        public CustomCheckBox likeButtonSpecial;
        public LinearLayout specialTagSpecial;
        private AnimationDrawable frameAnimation;

        public CardView specialCard;
        public CardView notSpecialCard;
        public View v;

        public ViewHolder(View v, boolean addLikeButton) {
            super(v);
            this.v = v;

            this.mNetworkImageView = (ImageView) v.findViewById(R.id.icon);
//            this.mNetworkImageView.setErrorImageResId(R.drawable.demoitem);
            this.mNetworkImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
            LayerDrawable layer = (LayerDrawable) mNetworkImageView.getBackground();
            frameAnimation = (AnimationDrawable) layer.getDrawable(0);


            this.titleArabic = (TextView) v.findViewById(R.id.place_title_ar);
            this.titleEnglish = (TextView) v.findViewById(R.id.place_title_en);
            this.likeButton = (CustomCheckBox) v.findViewById(R.id.like_button);
            likeButton.setVisibility(addLikeButton? View.VISIBLE : View.GONE);
            likeButton.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);
            this.specialTag = (LinearLayout) v.findViewById(R.id.special_place_tag);
            this.deleteButton = (CustomCheckBox) v.findViewById(R.id.delete_button);

            this.mNetworkImageViewSpecial = (ImageView) v.findViewById(R.id.special_icon);
            this.mNetworkImageViewSpecial.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);
            LayerDrawable specialLayer = (LayerDrawable) mNetworkImageView.getBackground();
            specialFrameAnimation = (AnimationDrawable) specialLayer.getDrawable(0);

            this.titleArabicSpecial = (TextView) v.findViewById(R.id.special_place_title_ar);
            this.titleEnglishSpecial = (TextView) v.findViewById(R.id.special_place_title_en);
            this.likeButtonSpecial = (CustomCheckBox) v.findViewById(R.id.special_like_button);
            this.specialTagSpecial = (LinearLayout) v.findViewById(R.id.special_special_place_tag);
            likeButtonSpecial.setVisibility(addLikeButton? View.VISIBLE : View.GONE);
            likeButtonSpecial.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);

            this.specialCard = (CardView) v.findViewById(R.id.special_card);
            this.notSpecialCard = (CardView)v.findViewById(R.id.not_special_card);

        }
        public void startSpecialAnimation() {
            frameAnimation.start();
        }

        public void stopSpecialAnimation() {
            frameAnimation.stop();
        }

        public void startAnimation() {
            frameAnimation.start();
        }

        public void stopAnimation() {
            frameAnimation.stop();
        }

    }

}
