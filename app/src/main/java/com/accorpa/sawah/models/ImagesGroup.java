package com.accorpa.sawah.models;

import java.util.List;

import static com.orm.SugarRecord.find;

/**
 * Created by root on 07/02/17.
 */

public class ImagesGroup {

    private PlaceImage[] images;
    private String[] imagesUrls;

    public String[] getImagesUrls() {
        return imagesUrls;
    }

    public void setImagesUrls(String[] imagesUrls) {
        this.imagesUrls = imagesUrls;
    }


    public ImagesGroup(int length) {
        images = new PlaceImage[length];
    }

    public ImagesGroup(Place place) {
        List<PlaceImage> imageList = PlaceImage.find(PlaceImage.class, "place = ?", place.getId()+"");
        images = imageList.toArray(new PlaceImage[imageList.size()]);
    }

    public String getNextImage() {
        if(images.length > 0)
            return images[0].getImageURL();
        return "";
    }

    public void addImage(PlaceImage placeImage) {
        images[0] = placeImage;
    }
}
