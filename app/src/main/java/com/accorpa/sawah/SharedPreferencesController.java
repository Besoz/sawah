package com.accorpa.sawah;

import android.content.Context;

/**
 * Created by Bassem on 15/01/17.
 */

public class SharedPreferencesController {

    private static SharedPreferencesController ourInstance;
    private ComplexSharedPreferences sharedPreferences;

    private final static String DEVICE_TOKEN_KEY = "device_Token";


    public static SharedPreferencesController getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new SharedPreferencesController(context);
        }
        return ourInstance;
    }

    private SharedPreferencesController(Context context){
        sharedPreferences =  ComplexSharedPreferences.getComplexPreferences(context);

    }

    public void setDeviceToken(String token){
        sharedPreferences.putObject(DEVICE_TOKEN_KEY, token);
    }

    public String getDeviceToken(){
        return sharedPreferences.getObject(DEVICE_TOKEN_KEY, String.class);
    }
}
