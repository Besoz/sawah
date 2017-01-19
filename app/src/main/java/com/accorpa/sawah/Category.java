package com.accorpa.sawah;

import android.widget.ListView;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by root on 18/01/17.
 */

public class Category extends  ListItem implements ListViewItem{

    @JsonProperty("CategoryName")
    private String name;

    @JsonProperty("CategoryID")
    private String CategoryID;

    @JsonProperty("ImageLocation")
    private String imageLocation;


    public Category() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public String getName() {
        return name;
    }

    public String getImageLocation() {
        return imageLocation;
    }
}
