package com.sawah.sawah.Handlers;

import android.content.Context;

import com.sawah.sawah.R;
import com.sawah.sawah.models.City;
import com.sawah.sawah.models.User;

/**
 * Created by Bassem on 15/01/17.
 */

public class SharedPreferencesController {

    private static String APP_VERSION = "1";
    private static SharedPreferencesController ourInstance;
    private ComplexSharedPreferences sharedPreferences;

    private final static String DEVICE_TOKEN_KEY = "device_Token", USER_DATA_KEY = "user_data",
            CITY_ID_KEY = "city_id", CITY_DATA_KEY = "city_data", APP_VERSION_KEY = "app_version";


    public static SharedPreferencesController getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SharedPreferencesController(context);
        }

        APP_VERSION = context.getString(R.string.app_version);
        return ourInstance;
    }

    private SharedPreferencesController(Context context) {
        sharedPreferences = ComplexSharedPreferences.getComplexPreferences(context);

    }

    public void setDeviceToken(String token) {
        sharedPreferences.putObject(DEVICE_TOKEN_KEY, token);
        sharedPreferences.commit();

    }

    public String getDeviceToken() {
        return sharedPreferences.getObject(DEVICE_TOKEN_KEY, String.class);
    }


    public void updateUser(User user) {
        sharedPreferences.putObject(USER_DATA_KEY, user);
        sharedPreferences.commit();
    }

    public boolean hasDefaultCity() {
        return sharedPreferences.hasKey(CITY_DATA_KEY);
    }

    public String getDefaultCityID() {
        return sharedPreferences.getObject(CITY_DATA_KEY, City.class).getCityID();
    }

    public void setDefaultCity(City city) {
        sharedPreferences.putObject(CITY_DATA_KEY, city);
        sharedPreferences.commit();
    }

    public boolean isSavedUserExists() {
        return sharedPreferences.hasKey(USER_DATA_KEY);
    }

    public User getUser() {
        return sharedPreferences.getObject(USER_DATA_KEY, User.class);
    }

    public void deleteUser() {
        sharedPreferences.deleteObject(USER_DATA_KEY);
    }

    public City getDefaultCity() {
        return sharedPreferences.getObject(CITY_DATA_KEY, City.class);
    }

    public String getAppVersion() {

//        if (!sharedPreferences.hasKey(APP_VERSION_KEY)){
//            sharedPreferences.putObject(APP_VERSION_KEY, APP_VERSION);
//            sharedPreferences.commit();
//        }
//
//        return sharedPreferences.getObject(APP_VERSION_KEY,String.class);

        return APP_VERSION;
    }

}
