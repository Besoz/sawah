package com.accorpa.sawah.place;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.CitiesListActivity;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.ListActivity;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomCheckBox;
import com.accorpa.sawah.models.City;
import com.accorpa.sawah.models.Place;

public class PlacesListActivity extends BaseActivity implements PlaceListFragment.OnFragmentInteractionListener {

    private String cityID, catID;

    private Place[] places;

    private PlaceListFragment listFragment;
    private PlacesMapFragment mapFragment;

//    private LinearLayout mProgressView;
//    private View mainView;

    private FragmentTransaction ft;

    private CustomButton mapToggleButton;

    private boolean mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_places_list);

        cityID = (String) getIntent().getSerializableExtra("CityID");
        catID = (String) getIntent().getSerializableExtra("CategoryID");

//        mListView = (GridView) findViewById(R.id.list);
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
//                Place selectedPlace= (Place) mListView.getAdapter().getItem(position);
//
//                NavigationHandler.getInstance().startPlaceDetailsActivity(PlacesListActivity.this, selectedPlace);
//            }
//        });
//        adapter = new PlacesAdapter(this, new Place[0]);
//        mListView.setAdapter(adapter);
        places =  new Place[0];

//        mProgressView = (LinearLayout) findViewById(R.id.progress_bar);
//        mainView = (View) findViewById(R.id.main_view);

        listFragment = PlaceListFragment.newInstance();
        mapFragment = PlacesMapFragment.newInstance();

        mapToggleButton = (CustomButton) findViewById(R.id.map_toggle_button);
        mapToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapView){

                    ft  = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, listFragment);
                    ft.commit();

                    mapToggleButton.setText(R.string.view_map);
                    mapView = false;
                }else{

                    mapFragment.setPlaces(places);

                    ft  = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment, mapFragment);
                    ft.commit();

                    mapToggleButton.setText(R.string.view_list);
                    mapView = true;
                }
            }
        });

        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, listFragment);
        ft.commit();

        DataHandler.getInstance(getApplicationContext()).requestPlacesArray(this, cityID, catID);
        showProgress(true);
    }


    public void recievePlacesList(Place[] arr) {

        places = DataHandler.getInstance(this).mergeWithFavouritePlaces(arr);
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
    protected void onResume() {

        super.onResume();
    }

    @Override
    public void onPlaceSelected(Place place){
        NavigationHandler.getInstance().startPlaceDetailsActivity(PlacesListActivity.this, place);
    }

    @Override
    public Place[] getPlaces() {
        return DataHandler.getInstance(this).mergeWithFavouritePlaces(places);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_places_list;
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.search_tool_bar;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(places == null) return false;

                Place[] queriedCities = DataHandler.getInstance(PlacesListActivity.this).
                        queryPlaces(places, newText);

                listFragment.setPlacesList(queriedCities);

                return true;
            }
        });

        return true;
    }
//    protected void showProgress(final boolean show) {
//        // The ViewPropertyAnimator APIs are not available, so simply show
//        // and hide the relevant UI components.
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//        mainView.setVisibility(show ? View.GONE : View.VISIBLE);
//    }
}
