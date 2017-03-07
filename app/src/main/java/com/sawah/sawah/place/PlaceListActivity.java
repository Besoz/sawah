package com.sawah.sawah.place;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.bumptech.glide.Glide;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.R;
import com.sawah.sawah.models.Place;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceListActivity extends BasePlacesListActivity implements SensorEventListener {

    private static final long CHECK_FREQUENCY = 100;
    private String cityID, catID, catName;

    private SensorManager sensorManager;



    /* And here the previous ones */
    private float xPreviousAccel;
    private float yPreviousAccel;
    private float zPreviousAccel;

    /* Used to suppress the first shaking */
    private boolean firstUpdate = true;

    /* Has a shaking motion been started (one direction) */
    private boolean shakeInitiated = false;

    private long lastUpdate, lastShake;



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

        setupSearch();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
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
        return R.menu.back_tool_bar;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

//        MenuItem item = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        return true;
    }
    @Override
    protected String getToolbarTitle() {
        return "";
    }

    protected void setupSearch()
    {
        SearchView searchView = styleSearch();
        searchView.setQueryHint(getString(R.string.search_in) + " " + catName);

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
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

//        Log.d("sensor", "shake detected w/ speed: here 3" );

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > CHECK_FREQUENCY) {

            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            boolean accChange =  Utils.getInstance().isAccelerationChanged(xPreviousAccel,
                    yPreviousAccel, zPreviousAccel, event.values[0], event.values[1],
                    event.values[2],  diffTime,  System.currentTimeMillis(), lastShake);

            if (accChange && (System.currentTimeMillis() - lastShake ) > 500 ) {
//                Log.d("sensor", "shake detected w/ speed: ==============" );
//                Toast.makeText(this, "shake detected w/ speed: ", Toast.LENGTH_SHORT).show();
                lastShake = System.currentTimeMillis();
                executeShakeAction();
            }
            updateAccelParameters(event.values[0], event.values[1], event.values[2]);
        }

    }

    private void executeShakeAction() {
        Log.d("Shake", "shaked");

        if (specialPlaceLayout)
            listFragment.setNormalLayoutManager();
        else
            listFragment.setSpecialLayoutManager();

        specialPlaceLayout = !specialPlaceLayout;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel,
                                       float zNewAccel) {

        xPreviousAccel = xNewAccel;
        yPreviousAccel = yNewAccel;
        zPreviousAccel = zNewAccel;

    }
}
