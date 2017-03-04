package com.accorpa.sawah;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.models.Category;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class CategoriesListActivity extends BaseActivity implements RecycleAdapterListener {


    private static final long LOCATION_REFRESH_TIME = 5000;
    private static final float LOCATION_REFRESH_DISTANCE = 1000;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private String cityID;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Category[] categories;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.list);

        mRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (position % 3 == 0) return 2;
                else return 1;
            }
        };

        gridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
//        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(gridLayoutManager);


        cityID = (String) getIntent().getSerializableExtra(DataHandler
                .getInstance(this.getApplicationContext()).CITY_ID_KEY);


        if (DataHandler.getInstance(this).userExist()) {

            mRequestingLocationUpdates = true;

        }else{
            mRequestingLocationUpdates = false;
        }

        DataHandler.getInstance(getApplicationContext()).requestCategoriesArray(this);
        showProgress(true);
    }




    public void receiveCategorizesList(Category[] categoryList) {

        this.categories = categoryList;
        mAdapter = new CategoriesRecycleAdapter(this, this, categoryList);
        mRecyclerView.setAdapter(mAdapter);
        showProgress(false);

    }

    @Override
    public void itemSelected(Object object) {
        NavigationHandler.getInstance().startPlacesListActivity(CategoriesListActivity.this,
                ((Category) object).getCategoryID(), cityID, ((Category) object).getName());
    }

    @Override
    protected String getToolbarTitle() {
        return DataHandler.getInstance(this).getDefaultCity().getCityNameAR();
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.search_back_tool_bar;
    }

    protected int getLayoutResourceId() {
        return R.layout.fragment_place_list;
    }


    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);

        String userId = DataHandler.getInstance(this).getUser().getUserID();
        DataHandler.getInstance(this).syncDefaultCityAndLocation(cityID, location.getLatitude()+"",
                location.getLongitude()+"", userId);
        stopLocationUpdates();
        mGoogleApiClient.disconnect();
    }
}
