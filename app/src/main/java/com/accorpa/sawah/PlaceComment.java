package com.accorpa.sawah;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by root on 25/01/17.
 */
public class PlaceComment implements Serializable{

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
}
