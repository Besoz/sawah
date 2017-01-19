package com.accorpa.sawah;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

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

    public void startCityActivity(Context context) {
        Intent cityActivity = new Intent(context, CategoriesListActivity.class);
        context.startActivity(cityActivity);
    }

    public void startPlacesListActivity(Context context, String categoryID) {

        Intent placesList = new Intent(context, PlacesListActivity.class);
        placesList.putExtra("CityID", "1");
        placesList.putExtra("CategoryID", categoryID);
        context.startActivity(placesList);
    }

    public void startPlaceDetailsActivity(Context context, Place selectedPlace) {

        ObjectMapper mapper = new ObjectMapper();

        JSONObject jsonPlace = null;
        try {
            jsonPlace = new JSONObject(mapper.writeValueAsString(selectedPlace));

            Intent placesList = new Intent(context, PlaceDetails.class);
            placesList.putExtra("PlaceJSONObject",  jsonPlace.toString());

            context.startActivity(placesList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void startAfterLoginctivity(Context context) {
        Intent cityActivity = new Intent(context, CategoriesListActivity.class);
        cityActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(cityActivity);
    }
}