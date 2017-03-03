package com.accorpa.sawah;

import android.*;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class CategoriesListActivity extends BaseActivity implements RecycleAdapterListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static final long LOCATION_REFRESH_TIME = 5000;
    private static final float LOCATION_REFRESH_DISTANCE = 1000;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private String cityID;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Category[] categories;

    private boolean mRequestingLocationUpdates;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


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
            createLocationRequest();
            mRequestingLocationUpdates = true;

            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }


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
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("Location", "Connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)) {



            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        } else {

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "changed");

        String userId = DataHandler.getInstance(this).getUser().getUserID();
        DataHandler.getInstance(this).syncDefaultCityAndLocation(cityID, location.getLatitude()+"",
                location.getLongitude()+"", userId);
        stopLocationUpdates();
        mGoogleApiClient.disconnect();

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mRequestingLocationUpdates) mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

}
