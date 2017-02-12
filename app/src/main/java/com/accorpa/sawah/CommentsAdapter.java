package com.accorpa.sawah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.models.PlaceComment;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by root on 02/02/17.
 */

public class CommentsAdapter extends BaseAdapter{


    private Context mContext;
    private LayoutInflater mInflater;

    private PlaceComment[] mDataSource;

    public CommentsAdapter(Context mContext, PlaceComment[] mDataSource) {
        this.mContext = mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mDataSource = mDataSource;
    }

    @Override
    public int getCount() {
        return mDataSource.length;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        CommentView holder;

        PlaceComment comment = (PlaceComment) getItem(position);

        if(convertView == null) {

            convertView = mInflater.inflate(R.layout.comment_list_item, parent, false);

            holder = new CommentsAdapter.CommentView();
            holder.userPhotoImageView = (NetworkImageView) convertView.findViewById(R.id.profile_image);
            holder.userNameTextView = (TextView) convertView.findViewById(R.id.user_name);
            holder.commentDateTextView = (TextView) convertView.findViewById(R.id.comment_date);
            holder.commentTextView = (TextView) convertView.findViewById(R.id.comment_text);

            convertView.setTag(holder);
        }
        else{
            holder = (CommentsAdapter.CommentView) convertView.getTag();
        }

//        holder.userPhotoImageView.set
        holder.userNameTextView.setText(comment.getFullName());
        holder.commentDateTextView.setText(comment.getCommentDate());
        holder.commentTextView.setText(comment.getDescription());

//        holder.titleTextView.setText(recipe.getName());
//
        ImageLoader mImageLoader = ServiceHandler.getInstance(mContext.getApplicationContext()).getImageLoader();
        String imageUrl= comment.getImageLocation().replaceAll(" ", "%20");

//        mImageLoader.get(imageUrl, getImageListener(holder.userPhotoImageView, R.drawable.sawah_logo, R.drawable.gplus_login_logo));
        holder.userPhotoImageView.setBackgroundResource(R.drawable.yellow_bird_progess_dialog);

        holder.userPhotoImageView.setImageUrl(imageUrl, mImageLoader);

        return convertView;
    }

    public static class CommentView{

        public TextView userNameTextView;
        public TextView commentDateTextView;
        public TextView commentTextView;
        public NetworkImageView userPhotoImageView;
    }

}
