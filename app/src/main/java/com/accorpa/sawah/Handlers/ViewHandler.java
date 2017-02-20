package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.accorpa.sawah.Authorization.EditProfileActivity;
import com.accorpa.sawah.R;

import java.io.IOException;

/**
 * Created by root on 15/02/17.
 */
public class ViewHandler {
    private static ViewHandler ourInstance = new ViewHandler();

    public static ViewHandler getInstance() {
        return ourInstance;
    }

    private ViewHandler() {
    }

    public Bitmap getBitmapFromURI(Context context, Uri uri) throws IOException {

        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        // Log.d(TAG, String.valueOf(bitmap));

    }
}
