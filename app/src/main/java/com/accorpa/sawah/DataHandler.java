package com.accorpa.sawah;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Bassem on 15/01/17.
 */
public class DataHandler {
    private static DataHandler ourInstance;
    private SharedPreferencesController sharedPreferences;

    private ServiceHandler serviceHandler;

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

        serviceHandler = ServiceHandler.getInstance(context);

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

        User user = new User(userName, password, "", "", "", "", "", "", "");

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

    public void saveUser(User user) {
        sharedPreferences.updateUser(user);
    }

    private <T> T[] convertToArray(JSONArray response, Class<T> c) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        T[] result = (T[]) mapper.readValue(response.toString(), mapper.getTypeFactory().constructArrayType(c));

//        mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, c));
//        List<T> myObjects = mapper.readValue(response.toString(), mapper.getTypeFactory().constructCollectionType(List.class, c));
        return (T[]) result;
    }

    public void recieveCategoriesList(JSONArray response, CategoriesListActivity activity) {

        try {
            Category [] arr = convertToArray(response, Category.class);
            activity.recieveCategouriesList(arr);

            Log.d("gg", String.valueOf(arr.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void requestCategoriesArray(CategoriesListActivity activity) {
        serviceHandler.requestCategoriesList(this, activity);
        Log.d("gg", "requesting");
    }


    public void requestPlacesArray(PlacesListActivity placesListActivity, String cityID, String catID) {
        serviceHandler.requestPlacesArray(this, placesListActivity, cityID, catID);

    }

    public void recievePlacesList(JSONArray response, PlacesListActivity placesListActivity) {
        try {
            Place[] arr = convertToArray(response, Place.class);
            placesListActivity.recievePlacesList(arr);

            Log.d("gg", String.valueOf(arr.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

