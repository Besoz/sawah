package com.accorpa.sawah;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

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

    @JsonProperty("ContactNumber")
    private String contactNumber;

//    @JsonProperty("FB")
//    private String fb;
//
//    @JsonProperty("Inst")
//    private String inst;

    @JsonProperty("Lat")
    private double lattitude;

    @JsonProperty("Lng")
    private double longitude;

    @JsonProperty("IsOpen")
    private boolean isOpen;

    @JsonProperty("PointID")
    private String pointID;

    @JsonProperty("PriceLevel")
    private float priceLevel;

    @JsonProperty("RatingID")
    private String ratingID;

//    @JsonProperty("Snap")
//    private String snap;

    @JsonProperty("WebSite")
    private String webSite;

    @JsonProperty("Appointments")
    private HashMap<String,String> appointments;

//    @JsonProperty("Locations")
//    private String locations;

    @JsonProperty("IsSpecial")
    private boolean isSpecial;

    @JsonProperty("Tags")
    private String tags;

    @JsonProperty("CommentsCount")
    private int commentsCount;

    @JsonProperty("Comments")
    private PlaceComment[] comments;


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
        if(imagesLocations.length > 0)
            return imagesLocations[0];
        return "";
    }

    public String getBiography() {
        return biography;
    }

    public String[] getImagesLocations() {
        return imagesLocations;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getPointID() {
        return pointID;
    }

    public void setPointID(String pointID) {
        this.pointID = pointID;
    }

    public float getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(float priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getRatingID() {
        return ratingID;
    }

    public void setRatingID(String ratingID) {
        this.ratingID = ratingID;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public HashMap<String, String> getAppointments() {
        return appointments;
    }

    public void setAppointments(HashMap<String, String> appointments) {
        this.appointments = appointments;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public PlaceComment[] getComments() {
        return comments;
    }

    public void setComments(PlaceComment[] comments) {
        this.comments = comments;
    }

    public boolean haveContactNumber() {
        if(contactNumber != null && !contactNumber.equals("")) return true;
        return false;
    }

    public boolean haveWebSite() {
        if(webSite != null && !webSite.equals("")) return true;
        return false;
    }
}
