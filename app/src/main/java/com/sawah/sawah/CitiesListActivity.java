package com.sawah.sawah;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.models.City;

public class CitiesListActivity extends BaseActivity implements RecycleAdapterListener{

    private City[] cities;
//    private CitiesAdapter citiesAdapter;

    private RecyclerView mRecyclerView;
    private CityRecyclerViewAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);

//        if(DataHandler.getInstance(this).getDefaultCity() == null)
        removeNavigationDrawer();

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        DataHandler.getInstance(getApplicationContext()).requestCitiesArray(this,this);
        showProgress(true);

        setupSearch();
    }



    public void receiveCitiesList(City[] cities) {

        this.cities = cities;

        cityAdapter = new CityRecyclerViewAdapter(this, this, cities);
        mRecyclerView.setAdapter(cityAdapter);

        showProgress(false);
    }

//    @Override
//    protected int getActionBarMenuLayout() {
//        return R.menu.search_tool_bar;
//    }

    @Override
    protected String getToolbarTitle() {
//        return getString(R.string.cities);
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));


        return true;
    }

    protected void setupSearch()
    {
        SearchView searchView = styleSearch();
        searchView.setQueryHint(getString(R.string.searh_city));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(cities == null) return false;

                City[] queriedCities = DataHandler.getInstance(CitiesListActivity.this).
                        queryCities(cities, newText);

                cityAdapter.updateDataSource(queriedCities);

                return true;
            }
        });
    }

    @Override
    public void itemSelected(Object object) {

        City selectedCity = (City) object;

        DataHandler.getInstance(CitiesListActivity.this.getApplicationContext())
                .setDefaultCity(selectedCity);

        NavigationHandler.getInstance().startCategoriesListActivity(CitiesListActivity.this,
                selectedCity.getCityID());
    }

    @Override
    public void showHideEmptyText() {

    }
    protected int getLayoutResourceId() {
        return R.layout.fragment_place_list;
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        NavigationHandler.getInstance().startLoginActivity(this);
        DialogHelper.getInstance().showNetworkErrorDialog(this);

    }
}
