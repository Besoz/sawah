package com.sawah.sawah.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by root on 25/01/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceComment extends SugarRecord implements Serializable{

    private Place place;

    @JsonProperty("ImageLocation")
    private String imageLocation;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("commentDate")
    private String commentDate;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("RateValue")
    private float rateValue;

    public PlaceComment() {
        this.imageLocation = "";
        this.fullName = "";
        this.commentDate = "";
        this.description = "";
        this.rateValue = 0;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRateValue() {
        return rateValue;
    }

    public void setRateValue(float rateValue) {
        this.rateValue = rateValue;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
