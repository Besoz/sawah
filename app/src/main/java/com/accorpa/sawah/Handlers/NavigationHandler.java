package com.accorpa.sawah.Handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.accorpa.sawah.AboutSawahActivity;
import com.accorpa.sawah.AddNewPlace.AddNewPlaceActivity;
import com.accorpa.sawah.Authorization.EditPasswordActivity;
import com.accorpa.sawah.Authorization.EditProfileActivity;
import com.accorpa.sawah.Authorization.RetrievePasswordActivity;
import com.accorpa.sawah.CategoriesListActivity;
import com.accorpa.sawah.CitiesListActivity;
import com.accorpa.sawah.CommentActivity;
import com.accorpa.sawah.CommentsListActivity;
import com.accorpa.sawah.GeneralInstructionActivity;
import com.accorpa.sawah.Authorization.LoginActivity;
import com.accorpa.sawah.models.PlaceComment;
import com.accorpa.sawah.place.FavouritePlacesList;
import com.accorpa.sawah.place.PlaceDetailsActivity;
import com.accorpa.sawah.Authorization.SignupActivity;
import com.accorpa.sawah.models.Place;
import com.accorpa.sawah.place.PlaceListActivity;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
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

    public void startLoginActivity(Context context) {

        Intent loginActivity = new Intent(context, LoginActivity.class);
        context.startActivity(loginActivity);
    }


    public void startPlacesListActivity(Context context, String categoryID, String CityID,
                                        String CatName) {

        Intent placesList = new Intent(context, PlaceListActivity.class);
        placesList.putExtra("CityID", CityID);
        placesList.putExtra("CategoryID", categoryID);
        placesList.putExtra("CategoryName", CatName);

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

//        if(dataHandler.hasDefaultCity()){
//
//            intent = new Intent(context, CategoriesListActivity.class);
//            intent.putExtra(dataHandler.CITY_ID_KEY, dataHandler.getDefaultCityID());
//
//        }else{
            intent = new Intent(context, CitiesListActivity.class);
//        }

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

    public void startCommentActivity(Context context, String placeID) {
        Intent commentActivity = new Intent(context, CommentActivity.class);
        commentActivity.putExtra("PlaceID", placeID);
        context.startActivity(commentActivity);
    }

    public void startCommentsListActivity(Context context, PlaceComment[] comments) {

        Intent commentListActivity = new Intent(context, CommentsListActivity.class);
        String JsonArr = "";
        try {
            JsonArr = JacksonHelper.getInstance().convertToJSON(comments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        commentListActivity.putExtra("PlaceCommentsArray", JsonArr);
        context.startActivity(commentListActivity);

    }


    public void startFavouritePlacesList(Context context) {
        Intent activity = new Intent(context, FavouritePlacesList.class);
        context.startActivity(activity);
    }

    public void startAboutSawah(Context context) {
        Intent activity = new Intent(context, AboutSawahActivity.class);
        context.startActivity(activity);
    }

    public void startGeneralInstruction(Context context){
        Intent activity = new Intent(context, GeneralInstructionActivity.class);
        context.startActivity(activity);
    }

    public void startEditProfileActivity(Context context) {
        Intent activity = new Intent(context, EditProfileActivity.class);
        context.startActivity(activity);
    }

    public void startImagePickerForResult(Activity activity, int pickImageRequest, boolean multiSelect) {




        if(multiSelect){
            Intent intent = new Intent(activity, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
            activity.startActivityForResult(intent, Constants.REQUEST_CODE);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest);
    }

    public void AddNewPlace(Context context) {
        Intent activity = new Intent(context, AddNewPlaceActivity.class);
        context.startActivity(activity);
    }

    public void startRetrievePasswordActivity(Context context) {
        Intent activity = new Intent(context, RetrievePasswordActivity.class);
        context.startActivity(activity);
    }

    public void startEditPasswordActivity(Context context) {
        Intent activity = new Intent(context, EditPasswordActivity.class);
        context.startActivity(activity);
    }
}