package com.accorpa.sawah.place;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceListActivity extends BasePlacesListActivity {

    private String cityID, catID, catName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        cityID = (String) getIntent().getSerializableExtra("CityID");
        catID = (String) getIntent().getSerializableExtra("CategoryID");
        catName = (String) getIntent().getSerializableExtra("CategoryName");

        if(DataHandler.getInstance(this).userExist()){
            addLikeButton = true;
        }
        specialPlaceLayout = true;

        super.onCreate(savedInstanceState);

        Utils.getInstance().changeStatusBarColor(this);
        DataHandler.getInstance(getApplicationContext()).requestPlacesArray(this, cityID, catID);
        showProgress(true);
    }

    public void recievePlacesList(Place[] arr) {

        places = DataHandler.getInstance(this)
                .mergeWithFavouritePlaces(new ArrayList<Place>(Arrays.asList(arr)));
//        show fragment
        listFragment.setPlacesList(places);

        showProgress(false);
    }

    public String getCityID() {
        return cityID;
    }

    public String getCatID() {
        return catID;
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.search_back_tool_bar;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_search);
        ((SearchView)item.getActionView()).setQueryHint(getString(R.string.search_places));

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(places == null) return false;

                ArrayList<Place> queriedCities = DataHandler.getInstance(PlaceListActivity.this).
                        queryPlaces(places, newText);

                listFragment.setPlacesList(queriedCities);

                return true;
            }
        });

        return true;
    }
    @Override
    protected String getToolbarTitle() {
        return catName;
    }
}
