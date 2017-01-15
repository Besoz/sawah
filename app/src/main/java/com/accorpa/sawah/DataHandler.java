package com.accorpa.sawah;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Bassem on 15/01/17.
 */
public class DataHandler {
    private static DataHandler ourInstance;
    private SharedPreferencesController sharedPreferences;


    public final static String OS = "Android";



    public static DataHandler getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new DataHandler(context);
        }
        return ourInstance;
    }

    private DataHandler(Context context) {
        sharedPreferences = SharedPreferencesController.getInstance(context);
    }


    public void updateDeviceToken(String token){
        sharedPreferences.setDeviceToken(token);
    }

    public String getDeiveToken(){

        String token = sharedPreferences.getDeviceToken();

        if(token ==  null){
            token = FirebaseInstanceId.getInstance().getToken();
            updateDeviceToken(token);
        }

        return token;
    }

}
