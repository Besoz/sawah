package com.accorpa.sawah;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.models.City;

public class CitiesListActivity extends ListActivity {

    private City[] cities;
    private CitiesAdapter citiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);

//        if(DataHandler.getInstance(this).getDefaultCity() == null)
            removeNavigationDrawer();
//        hideToolbarTitle();

        mListView = (GridView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                City selectedCity = (City) mListView.getAdapter().getItem(position);


                DataHandler.getInstance(context.getApplicationContext()).setDefaultCity(selectedCity);

                NavigationHandler.getInstance().startCategoriesListActivity(CitiesListActivity.this,
                        selectedCity.getCityID());
            }
        });
        DataHandler.getInstance(getApplicationContext()).requestCitiesArray(this);
        showProgress(true);

    }



    public void receiveCitiesList(City[] cities) {

        this.cities = cities;
        citiesAdapter = new CitiesAdapter(this, cities);
        mListView.setAdapter(citiesAdapter);
        showProgress(false);
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.search_tool_bar;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.cities);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.findItem(R.id.action_search);
        ((SearchView)item.getActionView()).setQueryHint(getString(R.string.searh_city));

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

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

                citiesAdapter.updateDataSource(queriedCities);

                return true;
            }
        });

        return true;
    }

}
