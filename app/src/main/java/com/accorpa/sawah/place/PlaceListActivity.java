package com.accorpa.sawah.place;

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

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;

import java.util.ArrayList;
import java.util.Arrays;

public class PlaceListActivity extends BasePlacesListActivity implements SensorEventListener {

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


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
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



    @Override
    public void onSensorChanged(SensorEvent event) {

        Log.d("sensor", "shake detected w/ speed: here 3" );

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {
            Log.d("sensor", "shake detected w/ speed: here" );
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;



            boolean accChange =  Utils.getInstance().isAccelerationChanged(xPreviousAccel,
                    yPreviousAccel, zPreviousAccel, event.values[0], event.values[1], event.values[2],  diffTime);

            if (accChange && (System.currentTimeMillis() - lastShake ) > 500 ) {
                Log.d("sensor", "shake detected w/ speed: ==============" );
//                Toast.makeText(this, "shake detected w/ speed: ", Toast.LENGTH_SHORT).show();
                lastShake = System.currentTimeMillis();
                executeShakeAction();

            }

            updateAccelParameters(event.values[0], event.values[1], event.values[2]);   // (1)

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
