package com.accorpa.sawah.Handlers;

import android.content.Context;

import com.accorpa.sawah.models.User;

/**
 * Created by Bassem on 15/01/17.
 */

public class SharedPreferencesController {

    private static SharedPreferencesController ourInstance;
    private ComplexSharedPreferences sharedPreferences;

    private final static String DEVICE_TOKEN_KEY = "device_Token", USER_DATA_KEY = "user_data",
    CITY_ID_KEY = "city_id";


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
        sharedPreferences.commit();

    }

    public String getDeviceToken(){
        return sharedPreferences.getObject(DEVICE_TOKEN_KEY, String.class);
    }


    public void updateUser(User user) {
        sharedPreferences.putObject(USER_DATA_KEY, user);
        sharedPreferences.commit();
    }

    public boolean hasDefaultCity() {
        return sharedPreferences.hasKey(CITY_ID_KEY);
    }

    public String getDefaultCityID() {
        return sharedPreferences.getObject(CITY_ID_KEY, String.class);
    }

    public void setDefaultCityID(String cityID){
        sharedPreferences.putObject(CITY_ID_KEY, cityID);
        sharedPreferences.commit();
    }

    public boolean isSavedUserExists() {
        return sharedPreferences.hasKey(USER_DATA_KEY);
    }

    public User getUser() {
        return sharedPreferences.getObject(USER_DATA_KEY, User.class);
    }
}