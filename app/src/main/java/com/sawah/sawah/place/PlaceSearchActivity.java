package com.sawah.sawah.place;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;

import com.sawah.sawah.ArrayRequestListener;
import com.sawah.sawah.BaseArrayResponseListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.R;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.models.Place;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceSearchActivity extends PlaceListActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        removeNavigationDrawer();
        mapToggleButton = (CustomButton) findViewById(R.id.map_toggle_button);
        mapToggleButton.setVisibility(View.GONE);


    }

    @Override
    protected void setupSearch()
    {
        SearchView searchView = styleSearch();
        searchView.setQueryHint(getString(R.string.search_in_places));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                if(TextUtils.isEmpty(query))
                    return false;

                ArrayRequestListener<Place> arrayRequestListener = new ArrayRequestListener<Place>() {
                    @Override
                    public void failResponse() {
                        showProgress(false);
                    }

                    @Override
                    public void successResponse(Place[] response) {
                        recievePlacesList(response);
                    }

                };
                DataHandler.getInstance(PlaceSearchActivity.this)
                        .searchPlacesList(arrayRequestListener, cityID, query);
                showProgress(true);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

}
