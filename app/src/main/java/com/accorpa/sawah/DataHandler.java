package com.accorpa.sawah;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassem on 15/01/17.
 */
public class DataHandler {
    private static DataHandler ourInstance;
    private SharedPreferencesController sharedPreferences;

    private ObjectMapper mapper;



    public final static String OS = "Android";



    public static DataHandler getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new DataHandler(context);
        }
        return ourInstance;
    }

    private DataHandler(Context context) {
        sharedPreferences = SharedPreferencesController.getInstance(context);
        mapper = new ObjectMapper();

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

    public JSONObject getUserSignupData(String userName, String password) {

        User user = new User(userName,password,"", "", "", "", "");

        JSONObject userData = null;
        try {
            userData = new JSONObject(mapper.writeValueAsString(user));

            userData.put("DeviceToken", getDeiveToken());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return userData;
    }

}

