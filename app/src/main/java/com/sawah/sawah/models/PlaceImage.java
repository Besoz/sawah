package com.sawah.sawah.models;

import com.orm.SugarRecord;

/**
 * Created by root on 07/02/17.
 */
public class PlaceImage extends SugarRecord{

    private Place place;
    private String imageURL;

    public PlaceImage() {
    }

    public PlaceImage(Place place, String s) {
        this.place = place;
        imageURL = s;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }


}
