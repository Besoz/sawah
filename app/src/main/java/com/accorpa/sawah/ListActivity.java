package com.accorpa.sawah;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

public class ListActivity extends BaseActivity {

    protected GridView mListView;
    Context context = this;

    private LinearLayout mProgressView;
    private View gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressView = (LinearLayout) findViewById(R.id.progress_bar);
        gridView = (View) findViewById(R.id.list);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_categories_list;
    }

}
