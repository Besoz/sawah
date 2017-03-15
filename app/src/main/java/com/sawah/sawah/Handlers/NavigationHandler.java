package com.sawah.sawah.Handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sawah.sawah.AboutSawahActivity;
import com.sawah.sawah.AddNewPlace.AddNewPlaceActivity;
import com.sawah.sawah.Authorization.EditPasswordActivity;
import com.sawah.sawah.Authorization.EditProfileActivity;
import com.sawah.sawah.Authorization.RetrievePasswordActivity;
import com.sawah.sawah.CategoriesListActivity;
import com.sawah.sawah.CitiesListActivity;
import com.sawah.sawah.comment.CommentActivity;
import com.sawah.sawah.comment.CommentsListActivity;
import com.sawah.sawah.GeneralInstructionActivity;
import com.sawah.sawah.Authorization.LoginActivity;
import com.sawah.sawah.models.PlaceComment;
import com.sawah.sawah.place.FavouritePlacesList;
import com.sawah.sawah.place.PlaceDetailsActivity;
import com.sawah.sawah.Authorization.SignupActivity;
import com.sawah.sawah.models.Place;
import com.sawah.sawah.place.PlaceListActivity;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sawah.sawah.place.PlaceSearchActivity;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Bassem on 14/01/17.
 */
public class NavigationHandler {
    public static final String COMMENT_COUNTS_K = "TotalCommentsCount", PLACE_ID_K = "PlaceID";

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
        loginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.getApplicationContext().startActivity(loginActivity);
        ((Activity)context).finishAffinity();
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

    public void startMainctivity(Context context) {
        startCityActivity(context,Intent.FLAG_ACTIVITY_CLEAR_TOP);

    }

    public void startAfterLoginctivity(Context context) {

        Intent afterLoginActivity = getCategoriesListActivityIntent(context);
        afterLoginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(afterLoginActivity);
        ((Activity)context).finishAffinity();
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

    public void startCityActivity(Context context, int flag){

        Intent intent = new Intent(context, CitiesListActivity.class);
        intent.setFlags(flag);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public void startCommentActivity(Context context, String placeID) {
        Intent commentActivity = new Intent(context, CommentActivity.class);
        commentActivity.putExtra(PLACE_ID_K, placeID);
        context.startActivity(commentActivity);
    }

    public void startCommentsListActivity(Context context, int totalCommentsCount, String placeID,
                                          PlaceComment[] comments) {

        Intent commentListActivity = new Intent(context, CommentsListActivity.class);
        String JsonArr = "";
        try {
            JsonArr = JacksonHelper.getInstance().convertToJSON(comments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        commentListActivity.putExtra("PlaceCommentsArray", JsonArr);
        commentListActivity.putExtra(COMMENT_COUNTS_K, totalCommentsCount);
        commentListActivity.putExtra(PLACE_ID_K, placeID);

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

    public void startImagePickerForResult(Activity activity, int pickImageRequest, int maxNo) {




//        if(maxNo > 1){
            Intent intent = new Intent(activity, AlbumSelectActivity.class);
//set limit on number of images that can be selected, default is 10
            intent.putExtra(Constants.INTENT_EXTRA_LIMIT, maxNo);
            activity.startActivityForResult(intent, pickImageRequest);
//            return;
//        }
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest);
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

    public void startPlaceSearchActivity(Context context, String cityID) {
        Intent intent = new Intent(context, PlaceSearchActivity.class);
        intent.putExtra(DataHandler.getInstance(context.getApplicationContext()).CITY_ID_KEY, cityID);
        context.startActivity(intent);
    }
}