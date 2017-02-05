package com.accorpa.sawah;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by root on 23/01/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class City {


    @JsonProperty("CityID")
    private String cityID;

    @JsonProperty("CityNameAR")
    private String CityNameAR;

    @JsonProperty("CityNameEN")
    private String cityNameEN;

    @JsonProperty("ImageLocation")
    private String imageLocation;

    @JsonProperty("Advices")
    private String advices;

    @JsonProperty("Numbers")
    private String numbers;

    @JsonProperty("Categories")
    private Category[] categories;

    public City() {
    }


    public String getCityID() {
        return cityID;
    }

    public void setCityID(String cityID) {
        this.cityID = cityID;
    }

    public String getCityNameAR() {
        return CityNameAR;
    }

    public void setCityNameAR(String cityNameAR) {
        CityNameAR = cityNameAR;
    }

    public String getCityNameEN() {
        return cityNameEN;
    }

    public void setCityNameEN(String cityNameEN) {
        this.cityNameEN = cityNameEN;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getAdvices() {
        return advices;
    }

    public void setAdvices(String advices) {
        this.advices = advices;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }
}
