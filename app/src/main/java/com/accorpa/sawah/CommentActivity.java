package com.accorpa.sawah;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.accorpa.sawah.custom_views.CustomEditText;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

public class CommentActivity extends BaseActivity {

    private String placeID;
    private Button commentPostButton;
    private CustomEditText commentText;
    private RatingBar ratingBar;
    private  AlertDialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        placeID = (String) getIntent().getSerializableExtra("PlaceID");

        intializeCommentPostButton();
        intializeRatingBar();

        commentText = (CustomEditText) findViewById(R.id.comment_edit_text);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(this.getLayoutInflater().inflate(R.layout.comment_alret_dialog, null));

        builder.setNeutralButton(R.string.agree, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                CommentActivity.this.finish();
            }
        });

        dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#000000"));
            }
        });

        //        builder.set
//        builder.setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.dismiss();
//                    CommentActivity.this.finish();
//                }
//        });
//
//        builder.setNegativeButton(R.string.agree, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();
//                CommentActivity.this.finish();
//            }
//        });

//        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) dialog.getButton(AlertDialog.BUTTON_NEUTRAL).getLayoutParams();
//                positiveButtonLL.gravity = Gravity.CENTER;
//                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setLayoutParams(positiveButtonLL);
//                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#000000"));
//                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#000000"));
    }

    private void intializeRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("rating", rating+"");
                ratingBar.setRating(rating);

            }
        });
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

        dialog.show();

        Log.d("Comment", "Post Success");
    }

    private void commentPostFail() {
        Log.d("Comment", "Post Fail");

    }

}


