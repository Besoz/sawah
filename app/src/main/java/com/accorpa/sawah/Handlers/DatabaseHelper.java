package com.accorpa.sawah.Handlers;

import android.os.AsyncTask;

import com.accorpa.sawah.models.Place;
import com.accorpa.sawah.place.FavouritePlacesList;
import com.arasthel.asyncjob.AsyncJob;

import java.util.List;

/**
 * Created by root on 06/02/17.
 */
public class DatabaseHelper {
    private static DatabaseHelper ourInstance = new DatabaseHelper();

    public static DatabaseHelper getInstance() {
        return ourInstance;
    }

    private DatabaseHelper() {
    }


}
