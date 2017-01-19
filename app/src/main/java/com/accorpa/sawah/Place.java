package com.accorpa.sawah;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by root on 18/01/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {

    @JsonProperty("PointName")
    private String palceName;

    @JsonProperty("PointNameEN")
    private String palceNameEng;

    @JsonProperty("PointNameAR")
    private String palceNameArb;

    @JsonProperty("BIO")
    private String biography;

    @JsonProperty("ImagesLocation")
    private String[] imagesLocations;



    public Place() {
    }

    public String getPalceName() {
        return palceName;
    }

    public void setPalceName(String palceName) {
        this.palceName = palceName;
    }

    public String getPalceNameEng() {
        return palceNameEng;
    }

    public void setPalceNameEng(String palceNameEng) {
        this.palceNameEng = palceNameEng;
    }

    public String getPalceNameArb() {
        return palceNameArb;
    }

    public void setPalceNameArb(String palceNameArb) {
        this.palceNameArb = palceNameArb;
    }

    public String getBio() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setImagesLocations(String[] imagesLocations) {
        this.imagesLocations = imagesLocations;
    }


    public String getImageLocation() {
        return imagesLocations[0];
    }
}
