package com.sawah.sawah.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orm.SugarRecord;

/**
 * Created by root on 07/02/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceImage extends SugarRecord{

    private Place place;
    private String imageURL;

    public PlaceImage() {
        imageURL = "";
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
