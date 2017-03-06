package com.sawah.sawah.Handlers;

import com.sawah.sawah.models.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by root on 14/02/17.
 */
public class GoogleMapHelper {
    private static GoogleMapHelper ourInstance = new GoogleMapHelper();

    public static GoogleMapHelper getInstance() {
        return ourInstance;
    }

    private GoogleMapHelper() {
    }

    public void moveMapToPlace(Place place, GoogleMap map){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getPosition(), 15));
    }
}
