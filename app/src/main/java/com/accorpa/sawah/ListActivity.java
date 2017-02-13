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

    protected void showProgress(final boolean show) {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            gridView.setVisibility(show ? View.GONE : View.VISIBLE);
    }


}
