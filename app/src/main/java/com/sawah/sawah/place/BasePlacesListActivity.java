package com.sawah.sawah.place;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sawah.sawah.BaseActivity;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.R;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.models.Place;

import java.util.ArrayList;
import java.util.Arrays;

public class BasePlacesListActivity extends BaseActivity implements PlaceListFragment.OnFragmentInteractionListener {


    protected ArrayList<Place> places;

    protected PlaceListFragment listFragment;
    protected PlacesMapFragment mapFragment;

//    private LinearLayout mProgressView;
//    private View mainView;

    protected FragmentTransaction ft;

    protected CustomButton mapToggleButton;

    protected boolean mapView;

    protected boolean addLikeButton, specialPlaceLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        places =  new ArrayList<>();

        listFragment = PlaceListFragment.newInstance(addLikeButton, specialPlaceLayout);
        mapFragment = PlacesMapFragment.newInstance();

        mapToggleButton = (CustomButton) findViewById(R.id.map_toggle_button);
        mapToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapView){

                    showListFragment();

                    mapToggleButton.setText(R.string.view_map);
                    mapView = false;
                }else{

                    mapFragment.setPlaces(places);

                    showMapFragment();

                    mapToggleButton.setText(R.string.view_list);
                    mapView = true;
                }
            }


        });

        showListFragment();
    }

    private void showMapFragment() {
        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, mapFragment);
        ft.commit();
    }

    protected PlaceListFragment showListFragment() {
        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, listFragment);
        ft.commit();
        return listFragment;
    }


    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPlaceSelected(Place place){
        NavigationHandler.getInstance().startPlaceDetailsActivity(BasePlacesListActivity.this, place);
    }

    @Override
    public ArrayList<Place> getPlaces() {
        return DataHandler.getInstance(this).mergeWithFavouritePlaces(places);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_places_list;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
