package com.accorpa.sawah;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;

import java.io.IOException;

public class CommentsListActivity extends ListActivity {

    private PlaceComment[] comments;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();

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
}
