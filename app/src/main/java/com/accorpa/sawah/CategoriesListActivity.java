package com.accorpa.sawah;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.models.Category;

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
