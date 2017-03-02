package com.accorpa.sawah;

import android.graphics.Bitmap;

import com.darsh.multipleimageselect.models.Image;

/**
 * Created by root on 21/02/17.
 */

public class BitmapImage extends Image {

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
//
    private Bitmap bitmap;

    public BitmapImage(long id, String name, String path, boolean isSelected) {
        super(id, name, path, isSelected);
    }

    public BitmapImage(Image image){
        super(image.id, image.name, image.path, image.isSelected);

    }

    public String getPath(){
        return this.path;
    }

}
