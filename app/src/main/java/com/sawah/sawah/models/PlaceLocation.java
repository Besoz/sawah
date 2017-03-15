package com.sawah.sawah.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.android.gms.maps.model.LatLng;
import com.orm.SugarRecord;

/**
 * Created by root on 14/03/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceLocation extends SugarRecord{

    private Place place;
    private double lat;
    private double lng;


    public PlaceLocation(){

    }
    public PlaceLocation(Place place, double lat, double lng) {
        this.place = place;
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng getLaLng(){
        return new LatLng(lat, lng);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
