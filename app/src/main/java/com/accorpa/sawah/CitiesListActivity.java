package com.accorpa.sawah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;

public class CitiesListActivity extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = (GridView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                City selectedCity = (City) mListView.getAdapter().getItem(position);

                DataHandler.getInstance(context.getApplicationContext()).setDefaulCity(selectedCity.getCityID());

                NavigationHandler.getInstance().startCategoriesListActivity(CitiesListActivity.this,
                        selectedCity.getCityID());
            }
        });
        DataHandler.getInstance(getApplicationContext()).requestCitiesArray(this);
        showProgress(true);

    }



    public void recieveCitiesList(City[] cities) {
        mListView.setAdapter(new CitiesAdapter(this, cities));
        showProgress(false);
    }


}
