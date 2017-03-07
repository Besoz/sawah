package com.sawah.sawah;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.ServiceHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.custom_views.CustomEditText;
import com.sawah.sawah.custom_views.CustomTextView;
import com.sawah.sawah.models.User;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends BaseActivity {

    private String placeID;
    private Button commentPostButton;
    private CustomEditText commentText;
    private SimpleRatingBar ratingBar;
    private  AlertDialog dialog;


    private User user;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        removeNavigationDrawer();

        placeID = (String) getIntent().getSerializableExtra("PlaceID");

        intializeCommentPostButton();
        intializeRatingBar();

        commentText = (CustomEditText) findViewById(R.id.comment_edit_text);


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setTitle(R.string.done_text)
//                .setMessage(R.string.comment_in_review_text)
//                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                        CommentActivity.this.finish();
//                    }
//                });

//        builder.setView(this.getLayoutInflater().inflate(R.layout.comment_alret_dialog, null));
//
//        builder.setNeutralButton(R.string.agree, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//                CommentActivity.this.finish();
//            }
//        });

//        dialog = builder.create();




//        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface arg0) {
//                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#000000"));
//            }
//        });

        user = DataHandler.getInstance(this).getUser();


        CustomTextView userName = (CustomTextView) findViewById(R.id.user_name);
        userName.setText(user.getFullName());

        CircleImageView userImage = (CircleImageView) findViewById(R.id.profile_image);
        if(!TextUtils.isEmpty(user.getLocalImagePath())){
            Bitmap b = DataHandler.getInstance(this)
                    .loadImageFromStorage(user.getLocalImagePath(), this);
            userImage.setImageBitmap(b);
        }
    }

    private void intializeRatingBar() {
        ratingBar = (SimpleRatingBar) findViewById(R.id.rating_bar);
    }


    private void intializeCommentPostButton() {
        commentPostButton = (Button) findViewById(R.id.post_comment_button);
        commentPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valideComment()){
                    postComment();
                }
            }
        });

    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.back_tool_bar;
    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }
    private boolean valideComment() {
        if(commentText.getText().toString().length() == 0){
            return false;
        }
        return true;
    }

    private void postComment() {
        ServiceHandler.getInstance(this).postComment(new CommentResposneListener(),
                placeID, commentText.getText().toString(),
                DataHandler.getInstance(this).getUser().getUserID(), ratingBar.getRating());
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_comment;
    }

    private class CommentResposneListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject jsonResponse) {
            ObjectMapper mapper = new ObjectMapper();

            Log.d("gg", jsonResponse.toString());
            ServiceResponse response = null;
            try {
                response = mapper.readValue(jsonResponse.toString(), ServiceResponse.class);

                if(response.isStatusSuccess()){
                    CommentActivity.this.commentPostSuccess();
                }else{
                    CommentActivity.this.commentPostFail();
                }

            } catch (IOException e) {
                CommentActivity.this.commentPostError();
            }
        }
    }

    private void commentPostError() {
        Log.d("Comment", "Post Error");
    }

    private void commentPostSuccess() {

        new MaterialDialog.Builder(this)
                .title(R.string.done_text)
                .content(R.string.comment_in_review_text)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        CommentActivity.this.finish();
                    }
                })
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();

        Log.d("Comment", "Post Success");
    }

    private void commentPostFail() {
        Log.d("Comment", "Post Fail");

    }

}


