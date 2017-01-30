package com.accorpa.sawah;

import android.content.Context;
import android.content.Intent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Bassem on 14/01/17.
 */
public class NavigationHandler {
    private static NavigationHandler ourInstance = new NavigationHandler();

    public static NavigationHandler getInstance() {
        return ourInstance;
    }

    private NavigationHandler() {
    }

    public void startSignupActivity(Context context) {
        Intent signUpActivity = new Intent(context, SignupActivity.class);
        context.startActivity(signUpActivity);
    }

    public void startLoginActivity(Context context, String userID) {

        Intent loginActivity = new Intent(context, LoginActivity.class);
        loginActivity.putExtra("UserID", userID);
        context.startActivity(loginActivity);
    }


    public void startPlacesListActivity(Context context,  String categoryID, String CityID) {

        Intent placesList = new Intent(context, PlacesListActivity.class);
        placesList.putExtra("CityID", CityID);
        placesList.putExtra("CategoryID", categoryID);
        context.startActivity(placesList);
    }

    public void startPlaceDetailsActivity(Context context, Place selectedPlace) {

        ObjectMapper mapper = new ObjectMapper();

        JSONObject jsonPlace = null;
        try {
            jsonPlace = new JSONObject(mapper.writeValueAsString(selectedPlace));

            Intent placesList = new Intent(context, PlaceDetailsActivity.class);
            placesList.putExtra("PlaceJSONObject",  jsonPlace.toString());

            context.startActivity(placesList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void startAfterLoginctivity(Context context) {

        Intent afterLoginActivity = getCategoriesListActivityIntent(context);
        afterLoginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(afterLoginActivity);

    }

    private Intent getCategoriesListActivityIntent(Context context){

        DataHandler dataHandler =  DataHandler.getInstance(context.getApplicationContext());
        Intent intent = null;

        if(dataHandler.hasDefaultCity()){

            intent = new Intent(context, CategoriesListActivity.class);
            intent.putExtra(dataHandler.CITY_ID_KEY, dataHandler.getDefaultCityID());

        }else{
            intent = new Intent(context, CitiesListActivity.class);
        }

        return intent;
    }

    public void startCategoriesListActivity(Context context, String cityID) {

        Intent intent = new Intent(context, CategoriesListActivity.class);
        intent.putExtra(DataHandler.getInstance(context.getApplicationContext()).CITY_ID_KEY, cityID);
        context.startActivity(intent);
    }
    public void startCategoriesListActivity(Context context) {

        Intent cityActivity = getCategoriesListActivityIntent(context);
        context.startActivity(cityActivity);
    }

    public void startCityActivity(Context context){

        Intent intent = new Intent(context, CitiesListActivity.class);
        context.startActivity(intent);
    }
}