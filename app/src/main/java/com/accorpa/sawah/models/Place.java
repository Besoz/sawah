package com.accorpa.sawah.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 18/01/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place extends SugarRecord implements ClusterItem{

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @JsonProperty("favourite")
    private boolean favourite;

    @JsonProperty("PointName")
    private String palceName;

    @JsonProperty("PointNameEN")
    private String palceNameEng;

    @JsonProperty("PointNameAR")
    private String palceNameArb;

    @JsonProperty("BIO")
    private String biography;

    @Ignore
    @JsonProperty("ImagesLocation")
    private PlaceImage[] images;


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

    @Ignore
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

    @Ignore
    @JsonProperty("Comments")
    private PlaceComment[] comments;

    @JsonIgnore
    @Ignore
    private LatLng position;

    public Place() {
        Log.d("Default Constructor", this.getId()+"");
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

    public void setImages(String[] imagesUrls) {
        Log.d("Image group", "+9999999999999999999999");

        this.images = new PlaceImage[imagesUrls.length];

        for (int i = 0; i < imagesUrls.length; i++) {
            images[i] = new PlaceImage(this, imagesUrls[i]);
        }

//        Log.d("Image group", this.imageGroup.getNextImage());

    }

    public String[] getImages(){

        Log.d("Getter", "Get Images");

        String[] imagesUrls = new String[this.images.length];

        for (int i = 0; i < images.length; i++) {
            imagesUrls[i] = this.images[i].getImageURL();
        }

        return imagesUrls;
    }

    @JsonIgnore
    public void setPlaceImages(PlaceImage[] placeImages){
        this.images = placeImages;
    }

    @JsonIgnore
    public PlaceImage[] getPlaceImages() {
        return this.images;
    }

    public String getImageLocation() {

        if(images.length > 0)
            return images[0].getImageURL();
        return "";
    }


    public String getBiography() {
        return biography;
    }

//    public String[] getImageGroup() {
//        return imageGroup;
//    }

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
        position = new LatLng(lattitude, longitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        position = new LatLng(lattitude, longitude);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getPlaceID() {
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
        for (int i = 0;  i < comments.length; i++){
            comments[i].setPlace(this);
        }
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

    @Override
    public long save() {

        long x = super.save();
//            List<PlaceComment> comments = new ArrayList<PlaceComment>(Arrays.asList(place.getComments()));
        SugarRecord.saveInTx(this.getComments());
        SugarRecord.saveInTx(this.getPlaceImages());

        return x;
    }

    @Override
    public boolean delete() {

        SugarRecord.deleteInTx(this.getComments());
        SugarRecord.deleteInTx(this.getPlaceImages());

        return super.delete();
    }

    @JsonIgnore
    @Override
    public LatLng getPosition() {
        return position;
    }
}
