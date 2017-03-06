package com.sawah.sawah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.sawah.sawah.Handlers.JacksonHelper;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.models.PlaceComment;
import com.bumptech.glide.util.Util;

import java.io.IOException;

public class CommentsListActivity extends ListActivity {

    private PlaceComment[] comments;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        Intent intent = this.getIntent();

        removeNavigationDrawer();
        String commentsJSONArray = (String) intent.getSerializableExtra("PlaceCommentsArray");
        try {
            comments = JacksonHelper.getInstance().convertToArray(commentsJSONArray,
                    PlaceComment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(new CommentsAdapter(this, comments));
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
}
