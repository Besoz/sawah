package com.accorpa.sawah;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;

import static com.accorpa.sawah.R.styleable.View;

public class CategoriesListActivity extends ListActivity implements ListView.OnScrollListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = (GridView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Category selectedCatgory = (Category) mListView.getAdapter().getItem(position);

                NavigationHandler.getInstance().startPlacesListActivity(CategoriesListActivity.this, selectedCatgory.getCategoryID());
            }
        });
        DataHandler.getInstance(getApplicationContext()).requestCategoriesArray(this);
    }



    public void recieveCategouriesList(Category[] categotyList) {

        mListView.setAdapter(new CategoriesAdapter(this, categotyList));

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:


//                mBusy = false;
//
//                int first = view.getFirstVisiblePosition();
//                int count = view.getChildCount();
//
//                for (int i = 0; i < count; i++) {
//
//                    holder.icon = (ImageView) view.getChildAt(i).findViewById(
//                            R.id.icon);
//                    if (holder.icon.getTag() != null) {
//                        holder.icon.setImageBitmap(IMAGE[first+i]);// this is the image url array.
//                        holder.icon.setTag(null);
//                    }
//                }

                Log.d("scroll", "Idle");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                mBusy = true;
                // mStatus.setText("Touch scroll");
                Log.d("scroll", "Touch scroll");

                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                mBusy = true;
                // mStatus.setText("Fling");
                Log.d("scroll", "Fling");

                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
