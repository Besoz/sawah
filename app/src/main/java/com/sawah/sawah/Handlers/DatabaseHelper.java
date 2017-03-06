package com.sawah.sawah.Handlers;

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
