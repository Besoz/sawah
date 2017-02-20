package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;

import com.accorpa.sawah.Authorization.EditProfileActivity;
import com.accorpa.sawah.Authorization.ImageRequestListner;
import com.accorpa.sawah.BaseResponseListner;
import com.accorpa.sawah.CategoriesListActivity;
import com.accorpa.sawah.CitiesListActivity;
import com.accorpa.sawah.CommentActivity;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.place.FavouritePlacesList;
import com.accorpa.sawah.place.PlacesListActivity;
import com.accorpa.sawah.models.Category;
import com.accorpa.sawah.models.City;
import com.accorpa.sawah.models.Place;
import com.accorpa.sawah.models.User;
import com.android.volley.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Bassem on 15/01/17.
 */
public class DataHandler {
    public static final String CITY_ID_KEY = "CityID";
    private static DataHandler ourInstance;
    private SharedPreferencesController sharedPreferences;

    private ServiceHandler serviceHandler;

    private ObjectMapper mapper;

    private Context context;


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

        this.context = context.getApplicationContext();
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


    public boolean userExist(){
        return sharedPreferences.isSavedUserExists();
    }

    public User getUser(){
        return sharedPreferences.getUser();
    }

    public void recieveCategoriesList(JSONArray response, CategoriesListActivity activity) {

        try {
            Category[] arr = JacksonHelper.getInstance().convertToArray(response.toString(), Category.class);
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

    public void requestCitiesArray(CitiesListActivity activity) {
        serviceHandler.requestCitiesList(this, activity);
        Log.d("gg", "requesting");
    }

    public void requestPlacesArray(PlacesListActivity placesListActivity, String cityID, String catID) {
        serviceHandler.requestPlacesArray(this, placesListActivity, cityID, catID);

    }

    public void recievePlacesList(JSONArray response, PlacesListActivity placesListActivity) {
        try {
            Place[] arr = JacksonHelper.getInstance().convertToArray(response.toString(), Place.class);

//            ArrayList<Place> favArr = (ArrayList<Place>) Place.findWithQueryAsIterator(Place.class,
//                    "CityID = ? and CatID = ?", placesListActivity.getCityID(),
//                    placesListActivity.getCatID());

            placesListActivity.recievePlacesList(arr);

            Log.d("gg", String.valueOf(arr.length));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void recieveCitiesList(JSONArray response, CitiesListActivity citiesListActivity) {

        try {
            City[] arr = JacksonHelper.getInstance().convertToArray(response.toString(), City.class);
            citiesListActivity.recieveCitiesList(arr);
            Log.d("gg", String.valueOf(arr.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasDefaultCity() {
        return sharedPreferences.hasDefaultCity();
    }

    public String getDefaultCityID() {
        return sharedPreferences.getDefaultCityID();
    }

    public void setDefaulCity(String CityID){

        sharedPreferences.setDefaultCityID(CityID);

    }

    public void togglePlaceFavourite(Place place) {
        if(place.isFavourite()){
//            remove from database
            place.setFavourite(false);
            place.delete();
        }else{
//            add to database
            place.setFavourite(true);
            place.save();

        }
    }

    public void requestFavouritePlacesArray(FavouritePlacesList favouritePlacesList) {
        DatabaseHelper.getInstance().getFavouritePlaces(this, favouritePlacesList, "", "");
    }

    public void recieveFavouritePlacesList(FavouritePlacesList activity, List<Place> places) {
//        activity.recieveFavouritePlacesList(places);
    }

    public Place[] mergeWithFavouritePlaces(Place[] places) {

        List<Place> favArr = (List<Place>) Place.listAll(Place.class);

        HashMap<String, Place> favPlacesIDs = new HashMap<String, Place>();

        for(int i = 0; i < favArr.size(); i++){
            favPlacesIDs.put(favArr.get(i).getPlaceID(), favArr.get(i));
        }

        for(int i = 0; i < places.length; i++){
            if(favPlacesIDs.containsKey(places[i].getPlaceID())){

                places[i].setFavourite(true);
                places[i].setId(favPlacesIDs.get(places[i].getPlaceID()).getId());

//                   todo set nested object IDs
            }else{
                places[i].setFavourite(false);
            }
        }

        return places;
    }

// todo split the method getimage and and send to server
    public Bitmap requestUpdateUserImage(final BaseResponseListner listner, final Context context,
                                         Uri userSelectedImage) throws IOException {

        Cursor returnCursor =
                context.getContentResolver().query(userSelectedImage, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();

        final String imageName = returnCursor.getString(nameIndex);

        final Bitmap userImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                userSelectedImage);

        final User user = getUser();

        //        send to server
        BaseResponseListner mResponseListner = new BaseResponseListner() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if(isStatusSuccess()){
                    saveUserImage(context, userImage, imageName);
                    listner.onResponse(response);
                }else{
                    Log.d("Update user", "fail");
                }
            }
        };


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        userImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        serviceHandler.updateUserImage(mResponseListner, user.getUserID(), encoded, imageName);

        return userImage;
    }

    private Bitmap saveUserImage(Context context, Bitmap userImage, String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("user_image", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, imageName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            userImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        User user = getUser();

        user.setImageLocation(directory.getAbsolutePath());
        user.setImageName(imageName);
        Log.d("profile image", user.getImageLocation()+" uu "+user.getImageName());

        saveUser(user);

        return userImage;
    }

    private Bitmap loadImageFromStorage(String path, String imageName)
    {

        try {
            File f=new File(path, imageName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap loadProfileImage() {

        User user = getUser();


        Bitmap b = loadImageFromStorage(user.getImageLocation(), user.getImageName());


        return b;
    }

    public void requestUdpateUserPassword(BaseResponseListner response, String currentPasswordStr,
                                          String newPasswordStr, String confirmPasswordStr){
        serviceHandler.updatePassword(response, getUser().getUserID(), currentPasswordStr,
                newPasswordStr, confirmPasswordStr);
    }

    public void requestUdpateUser(final User user, final BaseResponseListner responseListner) {

        BaseResponseListner mResponseListner = new BaseResponseListner() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                Log.d("update user", response.toString());
                if(isStatusSuccess()){

                    saveUser(user);
                    responseListner.onResponse(response);
                }else{
                    Log.d("Update user", "fail");
                }
            }
        };


        ObjectMapper mapper =  new ObjectMapper();
        JSONObject userData = null;
        try {
            userData = new JSONObject(mapper.writeValueAsString(user));
            userData.put("DeviceToken", getDeiveToken());
            userData.put("OS", OS);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        serviceHandler.requestUpdateUser(userData, mResponseListner);
    }

    public Bitmap getImage(Uri data) throws IOException {
;
        return MediaStore.Images.Media.getBitmap(context.getContentResolver(),
                data);
    }
}

