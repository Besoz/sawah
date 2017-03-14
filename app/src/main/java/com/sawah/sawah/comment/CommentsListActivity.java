package com.sawah.sawah.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.sawah.sawah.ArrayRequestListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.JacksonHelper;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.ListActivity;
import com.sawah.sawah.R;
import com.sawah.sawah.models.PlaceComment;

import java.io.IOException;

public class CommentsListActivity extends ListActivity {

    private PlaceComment[] comments;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        removeNavigationDrawer();



        Intent intent = this.getIntent();

        String placeID = (String) intent.getSerializableExtra(NavigationHandler.PLACE_ID_K);

        String commentsJSONArray = (String) intent.getSerializableExtra("PlaceCommentsArray");
        try {
            comments = JacksonHelper.getInstance().convertToArray(commentsJSONArray,
                    PlaceComment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mListView = (ListView) findViewById(R.id.list);

        final CommentsAdapter adapter = new CommentsAdapter(this, comments);
        mListView.setAdapter(adapter);


        ArrayRequestListener<PlaceComment> requestListener = new ArrayRequestListener<PlaceComment>() {
            @Override
            public void failResponse() {
                showProgress(false);
            }

            @Override
            public void successResponse(PlaceComment[] response) {
                showProgress(false);
                adapter.setDataSource(response);
            }
        };

        showProgress(true);
        DataHandler.getInstance(this).requestPlaceComments(placeID, this, requestListener);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_comments_list;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.commments);
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.back_tool_bar;
    }

    @Override
    protected void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//        mainLayout.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
