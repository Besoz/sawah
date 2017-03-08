package com.sawah.sawah.Handlers;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.arasthel.asyncjob.AsyncJob;
import com.sawah.sawah.BaseResponseListener;
import com.sawah.sawah.BitmapImage;
import com.sawah.sawah.CategoriesListActivity;
import com.sawah.sawah.CitiesListActivity;
import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.R;
import com.sawah.sawah.ServiceResponse;
import com.sawah.sawah.models.PlaceComment;
import com.sawah.sawah.models.PlaceImage;
import com.sawah.sawah.models.WorkTime;
import com.sawah.sawah.models.Category;
import com.sawah.sawah.models.City;
import com.sawah.sawah.models.Place;
import com.sawah.sawah.models.User;
import com.sawah.sawah.place.PlaceListActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rapid.decoder.BitmapDecoder;

/**
 * Created by Bassem on 15/01/17.
 */
public class DataHandler {
    public static final String CITY_ID_KEY = "CityID", DElIMITER = ":|:";
    private static final int PROFILE_IMAGE_WIDTH = 200, PROFILE_IMAGE_HEIGHT = 200;
    private static final int PLACE_IMAGE_WIDTH = 640, PLACE_IMAGE_HEIGHT = 360;
    public static final String CITY_NAME_KEY = "CityName";
    private static final float IMAGE_MAX_SIZE = 200;
    private static DataHandler ourInstance;
    private SharedPreferencesController sharedPreferences;

    private ServiceHandler serviceHandler;

    private ObjectMapper mapper;

//    private Context context;
    private Target target;

    private boolean userLocationSynced;


    public final static String OS = "Android";

    private static final Map<Integer, String> dayMap = new HashMap<>();
    private Location userLocation;


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

        context = context.getApplicationContext();


        dayMap.put(7, context.getString(R.string.sat_day));
        dayMap.put(1, context.getString(R.string.sun_day));
        dayMap.put(2, context.getString(R.string.mon_day));
        dayMap.put(3, context.getString(R.string.tue_day));
        dayMap.put(4, context.getString(R.string.wen_day));
        dayMap.put(5, context.getString(R.string.thu_day));
        dayMap.put(6, context.getString(R.string.fri_day));

    }


    public void updateDeviceToken(String token){
        sharedPreferences.setDeviceToken(token);
    }

    public String getDeviceToken(){

        String token = sharedPreferences.getDeviceToken();

        if(token ==  null){
            token = FirebaseInstanceId.getInstance().getToken();
            updateDeviceToken(token);
        }

        return token;
    }

    public JSONObject getUserSignupData(String userName, String password) {

        User user = new User(userName, password, "", "", "", "", "", "");

        JSONObject userData = null;
        try {
            userData = new JSONObject(mapper.writeValueAsString(user));

            userData.put("DeviceToken", getDeviceToken());

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
            activity.receiveCategorizesList(arr);

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

    public void requestPlacesArray(PlaceListActivity basePlacesListActivity, String cityID, String catID) {
        serviceHandler.requestPlacesArray(this, basePlacesListActivity, cityID, catID);

    }

    public void recievePlacesList(JSONArray response, PlaceListActivity placesListActivity) {
        try {
            Place[] arr = JacksonHelper.getInstance().convertToArray(response.toString(), Place.class);

//            ArrayList<Place> favArr = (ArrayList<Place>) Place.findWithQueryAsIterator(Place.class,
//                    "CityID = ? and CatID = ?", basePlacesListActivity.getCityID(),
//                    basePlacesListActivity.getCatID());

            placesListActivity.recievePlacesList(arr);

            Log.d("gg", String.valueOf(arr.length));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void receiveCitiesList(JSONArray response, CitiesListActivity citiesListActivity) {

        try {
            City[] arr = JacksonHelper.getInstance().convertToArray(response.toString(), City.class);
            citiesListActivity.receiveCitiesList(arr);
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

    public void setDefaultCity(City city){

        sharedPreferences.setDefaultCity(city);

    }
    public void syncDefaultCityAndLocation(String city, String latitude, String longitude, String userID){

        BaseResponseListener baseResponseListener = new BaseResponseListener();
        baseResponseListener.setOnResponseListner(new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                Log.d("change city service", "fail");
            }

            @Override
            public void successResponse(ServiceResponse response) {
                Log.d("change city service", "success");

            }
        });


        serviceHandler.changeCity(city,  getUser().getUserID(), latitude, longitude,
                baseResponseListener);

    }

    public void syncDefaultCityAndLocation(String city, String latitude, String longitude,
                                           String userID, final BaseRequestStateListener baseRequestStateListener){

        BaseResponseListener baseResponseListener = new BaseResponseListener();
        baseResponseListener.setOnResponseListner(baseRequestStateListener);

        serviceHandler.changeCity(city,  getUser().getUserID(), latitude, longitude,
                baseResponseListener);

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


    public ArrayList<Place> mergeWithFavouritePlaces(ArrayList<Place> places) {

        List<Place> favArr = loadAllPlaceFromDataBase();

        HashMap<String, Place> favPlacesIDs = new HashMap<String, Place>();


        for(int i = 0; i < favArr.size(); i++){
            favPlacesIDs.put(favArr.get(i).getPlaceID(), favArr.get(i));
        }

        for(int i = 0; i < places.size(); i++){
            if(favPlacesIDs.containsKey(places.get(i).getPlaceID())){

                places.get(i).setFavourite(true);
                favPlacesIDs.get(places.get(i).getPlaceID()).delete();
                places.get(i).save();

            }else{
                places.get(i).setFavourite(false);
            }
        }

        return places;
    }

// todo split the method getimage and and send to server
    public Bitmap requestUpdateUserImage(final BaseResponseListener listner,
                                         Uri userSelectedImage, final Context context) throws IOException {

        final String imageName = getImageName(userSelectedImage, context);

        final Bitmap userImage = getImage(userSelectedImage, context);

        final User user = getUser();

        //        send to server
        BaseResponseListener mResponseListner = new BaseResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if(isStatusSuccess()){
                    saveUserImage(userImage, imageName, context);
                    listner.onResponse(response);
                }else{
                    Log.d("Update user", "fail");
                }
            }
        };

        String encodedUserImage = getBase64Encoding(userImage);

        serviceHandler.updateUserImage(mResponseListner, user.getUserID(), encodedUserImage, imageName);

        return userImage;
    }

    private String getImageName(Uri userSelectedImage, Context context) {
        Cursor returnCursor =
                context.getContentResolver().query(userSelectedImage, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

    private String getBase64Encoding(Bitmap userImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        userImage.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap saveUserImage(Bitmap userImage, String imageName, Context context) {

        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("user_image", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, "profile.png");

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

        String path = directory.getAbsolutePath();

        user.setLocalImagePath(path);
//        user.setImageName(imageName);
        Log.d("profile image", user.getLocalImagePath().toString()+" uu ");

        saveUser(user);


        return userImage;
    }

    public Bitmap loadImageFromStorage(String path, Context context)
    {

        try {

            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("user_image", Context.MODE_PRIVATE);

            File f=new File(directory, "profile.png");
            FileInputStream fis = new FileInputStream(f);
            Bitmap b = BitmapFactory.decodeStream(fis);
            try
            {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public Bitmap loadProfileImage(Context context) {

        User user = getUser();


        Bitmap b = loadImageFromStorage(user.getLocalImagePath(), context);


        return b;
    }

    public void requestUdpateUserPassword(BaseResponseListener response, String currentPasswordStr,
                                          String newPasswordStr, String confirmPasswordStr){
        serviceHandler.updatePassword(response, getUser().getUserID(), currentPasswordStr,
                newPasswordStr, confirmPasswordStr);
    }

    public void requestUdpateUser(final User user, final BaseRequestStateListener responseListner) {

        BaseResponseListener baseResponseListener = new BaseResponseListener();
        baseResponseListener.setOnResponseListner(new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                Log.d("Update user", "fail");
            }

            @Override
            public void successResponse(ServiceResponse response) {
                Log.d("update user", response.toString());

                saveUser(response.getUser());
                responseListner.successResponse(response);
            }
        });


        ObjectMapper mapper =  new ObjectMapper();
        JSONObject userData = null;
        try {
            userData = new JSONObject(mapper.writeValueAsString(user));
            userData.put("DeviceToken", getDeviceToken());
            userData.put("OS", OS);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        serviceHandler.requestUpdateUser(userData, baseResponseListener);
    }

    public Bitmap getImage(Uri uri, Context context)
    {
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;//optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > 200) ? (originalSize / 200) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true; //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
            input = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            return bitmap;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        try {

//            return MediaStore.Images.Media.getBitmap(context.getContentResolver(),
//                    data);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    private String concateImageNameAndEncoding(String name, String encoding){
//        string builder for encoding is very large and full of terrror
//        no need encoding is copied only one time
        
//        StringBuilder sb = new StringBuilder(name.length() + encoding.length() + 2);
//
//        sb.append(name).append(DElIMITER).append(encoding);


        return name+DElIMITER+encoding;

    }

    public Bitmap[] getImages(Intent data, Context context) throws IOException {

//        ArrayList<String> imagesPathList = new ArrayList<String>();
        Log.d("Add new Place", data.getStringExtra("data"));

        String[] imagesPath = data.getStringExtra("data").split("\\|");
        Bitmap[] images = new Bitmap[imagesPath.length];

        for (int i = 0; i < images.length; i++) {
            images[i] = getImage(Uri.parse(imagesPath[i]), context);
        }

        Log.d("add new Place", images.length+"========");

        return images;
    }


    public void addNewPlace(final ArrayList<BitmapImage> bitmapImages, Place place, final BaseRequestStateListener listner) {
        Log.d("add new Place", "2");

        String cityID = getDefaultCityID(), userID = getUser().getUserID();

        ObjectMapper mapper =  new ObjectMapper();
        JSONObject PlaceData = null;
        try {
            PlaceData = new JSONObject(mapper.writeValueAsString(place));
            Log.d("Add new place", place.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        final BaseResponseListener placeImageResponseListener = new BaseResponseListener();
        placeImageResponseListener.setOnResponseListner(listner);

        BaseResponseListener placeDataResponseListener = new BaseResponseListener();
        placeDataResponseListener.setOnResponseListner(new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                Log.d("dd new place", "fail");
                listner.failResponse(response);
            }

            @Override
            public void successResponse(final ServiceResponse response) {

                new AsyncJob.AsyncJobBuilder<Boolean>()
                        .doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                            @Override
                            public Boolean doAsync() {
                                // Do some background work
                                String[] bitmapsEndcoded = new String[bitmapImages.size()];

                                for (int i = 0; i < bitmapsEndcoded.length ; i++) {
                                    bitmapsEndcoded[i] = concateImageNameAndEncoding(bitmapImages.get(i).name,
                                            getBase64Encoding(bitmapImages.get(i).getBitmap()));
                                }

                                serviceHandler.addPlaceImages(response.getDraftPointID(), bitmapsEndcoded,
                                        placeImageResponseListener);
                                return true;
                            }
                        })
                        .doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                            @Override
                            public void onResult(Boolean result) {
//                                Toast.makeText(context, "Result was: " + result, Toast.LENGTH_SHORT).show();
                            }
                        }).create().start();

            }
        });


        serviceHandler.addNewPlace(userID, cityID, PlaceData, placeDataResponseListener);


    }

    public void loadImageBitmaps(ArrayList<BitmapImage> images) {

        for (int i = 0; i < images.size() ; i++) {
            //                Uri s = Uri.parse(images.get(i).path);
            BitmapImage bitmapImage = new BitmapImage(images.get(i));
            String s = bitmapImage.path;

            File f = new File(s);
            float size  = f.length()/1024; // KB

            float scale = 1;
            if(size > IMAGE_MAX_SIZE){
                scale = IMAGE_MAX_SIZE/ size;
            }
            Log.d("SAWAH","scale: " + String.valueOf(scale));
            Bitmap b = BitmapDecoder.from(s).scaleBy(scale).decode();
            bitmapImage.setBitmap(b);

            images.set(i, bitmapImage);
        }

    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

//    todo improve performance
    public City[] queryCities(City[] cities, String newText) {

        if (TextUtils.isEmpty(newText)) return cities;

        ArrayList<City> queriedCities = new ArrayList<>();

        for (int i = 0; i < cities.length; i++) {

            if(cities[i].getCityNameAR().contains(newText) ||
                    cities[i].getCityNameEN().toLowerCase().contains(newText.toLowerCase())){
                queriedCities.add(cities[i]);
            }
        }

        return queriedCities.toArray(new City[queriedCities.size()]);
    }
    public Category[] queryCategories(Category[] categories, String newText) {

        if (TextUtils.isEmpty(newText)) return categories;

        ArrayList<Category> queriedCities = new ArrayList<>();

        for (int i = 0; i < categories.length; i++) {

            if(categories[i].getName().contains(newText)){
                queriedCities.add(categories[i]);
            }
        }

        return queriedCities.toArray(new Category[queriedCities.size()]);
    }

    public Place loadPlaceFromDataBase(Place place) {

        Place dbPlace = Place.findById(Place.class, place.getId());
        if(dbPlace != null){
            List<PlaceImage> images = PlaceImage.find(PlaceImage.class, "place = ?",
                    place.getId()+"");

            List<PlaceComment> comments = PlaceComment.find(PlaceComment.class, "place = ?",
                    place.getId()+"");

            List<WorkTime> workTimes = WorkTime.find(WorkTime.class, "place = ?",
                    place.getId()+"");

            Log.d("work yyyyyyyyyy", workTimes.size()+"");

            place.setPlaceImages(images.toArray(new PlaceImage[images.size()]));

            place.setComments(comments.toArray(new PlaceComment[comments.size()]));

            place.setWorkTimes(workTimes.toArray(new WorkTime[workTimes.size()]));
        }

        return place;
    }

    public ArrayList<Place> loadAllPlaceFromDataBase() {

        ArrayList<Place> places = (ArrayList<Place>) Place.listAll(Place.class);
        for(int i = 0; i < places.size(); i++){
           loadPlaceFromDataBase(places.get(i));
        }


        return places;
    }

    public String getDayMapping(int day) {
        return dayMap.get(day);
    }

    public ArrayList<Place> queryPlaces(ArrayList<Place> places, String newText) {

        if (TextUtils.isEmpty(newText)) return places;

        ArrayList<Place> queriedPlaces = new ArrayList<>();

        for (int i = 0; i < places.size(); i++) {

            if(places.get(i).getPalceNameArb().contains(newText) ||
                    places.get(i).getPalceNameEng().toLowerCase().contains(newText.toLowerCase())){
                queriedPlaces.add(places.get(i));
            }
        }

        return queriedPlaces;
    }

    public void eraseCurrentUser(Context context) {

//        remove user
        sharedPreferences.deleteUser();

        File directory = context.getDir("user_image", Context.MODE_PRIVATE);
        if(directory.exists())
        {
            File f=new File(directory, "profile.png");
            if(f.exists())
                f.delete();
        }
//        remove fav places
        Place.deleteAll(Place.class);
        PlaceComment.deleteAll(PlaceComment.class);
        WorkTime.deleteAll(WorkTime.class);
    }

    public City getDefaultCity() {
        return sharedPreferences.getDefaultCity();
    }

//    todo make it servic
    public void loadAndSaveUserNetworkImage(final Uri image,
                                            final BaseRequestStateListener baseRequestStateListener
                                            , final Context context)
    {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                saveUserImage(bitmap,"photo", context);
                baseRequestStateListener.successResponse(null);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                baseRequestStateListener.failResponse(null);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load(image).into(target);

    }

    public void assertUserLoacationSynced(Location location){
        this.userLocation = location;
        userLocationSynced = true;
    }

    public boolean isUserLoacationSynced(){
        return userLocationSynced;
    }

    public Location getUserLocation(){
        return this.userLocation;
    }


    public void checkInPlace(String placeID, String userID, BaseRequestStateListener requestStateListener) {

        BaseResponseListener baseResponseListener = new BaseResponseListener();
        baseResponseListener.setOnResponseListner(requestStateListener);

        serviceHandler.makeCheckin(placeID, userID, baseResponseListener);
    }
}

