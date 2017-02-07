package com.accorpa.sawah.place;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.ListActivity;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;

public class PlacesListActivity extends ListActivity {

    private String cityID, catID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_places_list);

        cityID = (String) getIntent().getSerializableExtra("CityID");
        catID = (String) getIntent().getSerializableExtra("CategoryID");

        mListView = (GridView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Place selectedPlace= (Place) mListView.getAdapter().getItem(position);

                NavigationHandler.getInstance().startPlaceDetailsActivity(PlacesListActivity.this, selectedPlace);
            }
        });
        DataHandler.getInstance(getApplicationContext()).requestPlacesArray(this, cityID, catID);
        showProgress(true);
    }


    public void recievePlacesList(Place[] arr) {
        mListView.setAdapter(new PlacesAdapter(this, arr));
        showProgress(false);

    }

    public String getCityID() {
        return cityID;
    }

    public String getCatID() {
        return catID;
    }
}
