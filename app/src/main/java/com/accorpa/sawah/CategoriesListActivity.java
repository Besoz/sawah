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

public class CategoriesListActivity extends ListActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final String cityID = (String) getIntent().getSerializableExtra(DataHandler
                .getInstance(context.getApplicationContext()).CITY_ID_KEY);

        mListView = (GridView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Category selectedCatgory = (Category) mListView.getAdapter().getItem(position);

                NavigationHandler.getInstance().startPlacesListActivity(CategoriesListActivity.this,
                        selectedCatgory.getCategoryID(), cityID);
            }
        });
        DataHandler.getInstance(getApplicationContext()).requestCategoriesArray(this);
        showProgress(true);
    }



    public void recieveCategouriesList(Category[] categotyList) {

        mListView.setAdapter(new CategoriesAdapter(this, categotyList));
        showProgress(false);

    }
}
