package com.sawah.sawah.Handlers;

import android.content.Context;
import android.util.Log;

import com.sawah.sawah.ArrayRequestListener;
import com.sawah.sawah.BaseArrayResponseListener;
import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.BaseResponseListener;
import com.sawah.sawah.CategoriesListActivity;
import com.sawah.sawah.CitiesListActivity;
import com.sawah.sawah.Authorization.LoginResponseListener;
import com.sawah.sawah.LruBitmapCache;
import com.sawah.sawah.R;
import com.sawah.sawah.RequestListener;
import com.sawah.sawah.Authorization.SignupResponseListener;
import com.sawah.sawah.place.PlaceListActivity;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bassem on 10/01/17.
 */
public class ServiceHandler {

    private static ServiceHandler ourInstance;

//    private Context context;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private URLHandler urlHandler;

    private final String hostName;

    public static ServiceHandler getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new ServiceHandler(context);
        }
        return ourInstance;
    }

    private ServiceHandler(Context context) {
//        this.context = context.getApplicationContext();
        hostName = context.getString(R.string.host_name);

// Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 100*1024 * 1024); // 1MB cap*100

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        mRequestQueue.start();

        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(context));

        urlHandler = URLHandler.getInstance(context);
    }



    public void loginUser(String userName, String password, String deviceToken,
                          LoginResponseListener loginResponseListener,
                          Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = getLoginRequest(userName, password, deviceToken,
                loginResponseListener,errorListener);

        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonObjectRequest);

    }

    private JsonObjectRequest getLoginRequest(String userName, String password, String deviceToken,
                                              LoginResponseListener loginResponseListener,
                                              Response.ErrorListener errorListener){

        String loginUrl = urlHandler.getLoginUrl(userName, password, deviceToken);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginUrl, null,
               loginResponseListener, errorListener);

        return jsonObjectRequest;
    }

    public void signupUser(JSONObject userData, SignupResponseListener signupResponseListener,
                           Response.ErrorListener errorListener) {

        JsonObjectRequest jsonObjectRequest = getSignupRequest(userData, signupResponseListener,
                errorListener);
        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonObjectRequest);
    }



    private JsonObjectRequest getSignupRequest(JSONObject userData,
                                               SignupResponseListener signupResponseListener,
                                               Response.ErrorListener errorListener){

        String signupUrl = urlHandler.getSigupUrl();

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, signupUrl, userData,
                signupResponseListener, errorListener);

        return jsonObjectRequest;
    }

    public void loginUser(String userID, String deviceToken,
                          LoginResponseListener loginResponseListener,
                          Response.ErrorListener errorListener) {

        String loginUrl = urlHandler.getLoginUrl(userID, deviceToken);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginUrl, null,
                loginResponseListener, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    public void requestCategoriesList(BaseArrayResponseListener baseResponseListener, String cityID,
                                      Response.ErrorListener errorListener){
        Log.d("gg", "requesting");

        String serviceUrl = urlHandler.getCategoriesServiceUrl(cityID);
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, baseResponseListener,errorListener);

        mRequestQueue.add(categoriesArrayRequest);

    }

    public void requestCategoriesList(final DataHandler dataHandler,
                                      final CategoriesListActivity activity,
                                      Response.ErrorListener errorListener){
        Log.d("gg", "requesting");

        String serviceUrl = urlHandler.getCategoriesServiceUrl(dataHandler.getDefaultCityID());
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("gg", response.toString());
                dataHandler.recieveCategoriesList(response, activity);
            }
        },errorListener);

        mRequestQueue.add(categoriesArrayRequest);

    }

    public void requestPlacesArray(final DataHandler dataHandler,
                                   final PlaceListActivity basePlacesListActivity, String cityID,
                                   String categoryID, Response.ErrorListener errorListener) {
        Log.d("gg", "requesting");

        String serviceUrl = urlHandler.getPlacesServiceUrl(cityID, categoryID);
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("gg", response.toString());
                dataHandler.recievePlacesList(response, basePlacesListActivity);
            }
        },errorListener);

        mRequestQueue.add(categoriesArrayRequest);

    }

    public void requestPlacesArray(BaseArrayResponseListener baseResponseListener, String cityID,
                                   String searchQuery, Response.ErrorListener errorListener) {
        Log.d("gg", "requesting");

        String serviceUrl = urlHandler.getPlacesSearchServiceUrl(cityID, searchQuery);
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest placesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, baseResponseListener,errorListener);

        mRequestQueue.add(placesArrayRequest);

    }


    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    public void requestCitiesList(BaseArrayResponseListener baseResponseListener,
                                  Response.ErrorListener errorListener) {

        String serviceUrl = urlHandler.getCitiesServiceUrl();
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, baseResponseListener,errorListener);

        mRequestQueue.add(categoriesArrayRequest);
    }


    public void requestCitiesList(final DataHandler dataHandler,
                                  final CitiesListActivity citiesListActivity,
                                  Response.ErrorListener errorListener) {

        String serviceUrl = urlHandler.getCitiesServiceUrl();
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("gg", response.toString());
                dataHandler.receiveCitiesList(response, citiesListActivity);
            }
        },errorListener);

        mRequestQueue.add(categoriesArrayRequest);
    }

    public void postComment(Response.Listener<JSONObject> commentResponseListner,
                            String placeID, String text, String userID, float rating,
                            Response.ErrorListener errorListener) {
        Log.d("comment", placeID+" "+text+" "+userID+" "+rating);

        String addCommentUrl = urlHandler.getAddCommentUrl();

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                addCommentUrl, getPostCommentRequest(placeID, text, userID, rating),
                commentResponseListner, errorListener);

        mRequestQueue.add(jsonObjectRequest);

    }

    private JSONObject getPostCommentRequest(String placeID, String text, String userID, float rating){

        JSONObject request = new JSONObject();
        try {
            request.put("PointID", placeID);
            request.put("Description", text);
            request.put("UserID", userID);
            request.put("RateValue", rating);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;
    }





    public void updateUserImage(Response.Listener<JSONObject> listener, String userID,
                                String userImage, String imageName,
                                Response.ErrorListener errorListener) {

        String signupUrl = urlHandler.getUpdateUserImageUrl();

        JSONObject request = new JSONObject();
        try {
            request.put("UserID", userID);
            request.put("FileName", imageName);
            request.put("Image", userImage);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, signupUrl, request,
                listener, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    public void updatePassword(BaseResponseListener listener, String userID,
                               String currentPasswordStr, String newPasswordStr,
                               String confirmPasswordStr, Response.ErrorListener errorListener) {

        String url = urlHandler.getChangePasswordUrl();

        JSONObject request = new JSONObject();
        try {
            request.put("OldPassword", currentPasswordStr);
            request.put("NewPassword", newPasswordStr);
            request.put("UserID", userID);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("change password", request.toString());

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                listener, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    public void requestUpdateUser(JSONObject userData, BaseResponseListener mResponseListner,
                                  Response.ErrorListener errorListener) {

        String url = urlHandler.getUpdateUserDataUrl();


        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, userData,
                mResponseListner, errorListener);

        Log.d("update user", userData.toString());
        mRequestQueue.add(jsonObjectRequest);


    }

    public void addNewPlace(String userID, String cityID, JSONObject placeData,
                            BaseResponseListener placeDataResponseListner,
                            Response.ErrorListener errorListener) {

        String url = urlHandler.getAddNewPlaceUrl();

        Log.d("place data", placeData.toString());
        try {
            placeData.put("CityID", cityID);
            placeData.put("UserID", userID);
            placeData.put("Description", placeData.getString("BIO"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, placeData,
                placeDataResponseListner,errorListener);

        Log.d("update user", placeData.toString());
        mRequestQueue.add(jsonObjectRequest);

    }

    public void addPlaceImages(String draftPointID, String[] bitmapsEndcoded,
                               BaseResponseListener placeImageResponseListner,
                               Response.ErrorListener errorListener) {

        String url = urlHandler.getAddPlaceImagesUrl();
        JSONObject request = new JSONObject();
        JSONObject images = new JSONObject();

        try {
            request.put("DraftPointID", draftPointID);
            images.put("count", bitmapsEndcoded.length);

            for (int i = 0; i < bitmapsEndcoded.length; i++) {
                images.put(i+"", bitmapsEndcoded[i]);
            }

            request.put("Images", images);

//            Log.d("add new place", request.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, request,
                placeImageResponseListner, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    public void requestRetrievePassword(String email,
                                        BaseRequestStateListener baseRequestStateListener,
                                        Response.ErrorListener errorListener) {

        BaseResponseListener baseResponseListener1 = new BaseResponseListener();
        baseResponseListener1.setOnResponseListner(baseRequestStateListener);


        String url = urlHandler.getRetrievePasswordUrl(email);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                baseResponseListener1, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    public void signupUser(String socialUserID, String socialType, String OS,
                           String deviceToken, String DOB,
                           BaseResponseListener baseResponseListener,
                           Response.ErrorListener errorListener) {

        String url = urlHandler.getSocialLoginUrl();

        JSONObject payload = getSocialLoginPayload(socialUserID, socialType, OS,
                deviceToken, DOB);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, payload, baseResponseListener, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    private JSONObject getSocialLoginPayload(String socialUserID, String socialType, String DOB,
                                             String OS, String deviceToken){
        JSONObject request = new JSONObject();
        try {
            request.put("SocialUserID", socialUserID);
            request.put("SocialType", socialType);
            request.put("OS", OS);
            request.put("DeviceToken", deviceToken);
            request.put("DOB", DOB);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;
    }

    public void makeCheckin(String placeID, String userID,
                            BaseResponseListener baseResponseListener,
                            Response.ErrorListener errorListener) {

        String url = urlHandler.getCheckinUrl();

        JSONObject payload = getCheckinPayload(placeID, userID);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, payload, baseResponseListener, errorListener);

        mRequestQueue.add(jsonObjectRequest);
    }

    private JSONObject getCheckinPayload(String placeID, String userID){

        JSONObject request = new JSONObject();
        try {
            request.put("PointID", placeID);
            request.put("UserID", userID);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;
    }

    public void changeCity(String cityID, String userID, String latitude, String longitude,
                           BaseResponseListener baseResponseListener) {

        String url = urlHandler.getCheckinUrl();

        JSONObject payload = getChangeCityPayload(cityID, userID, latitude, longitude);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, payload, baseResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    private JSONObject getChangeCityPayload(String cityID, String userID, String latitude,
                                         String longitude){

        JSONObject request = new JSONObject();
        try {
            request.put("CityID", cityID);
            request.put("UserID", userID);
            request.put("LatLng", latitude+","+longitude);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return request;
    }


    public void notifyPlaceVisit(String placeID, BaseResponseListener baseResponseListener) {

        String url = urlHandler.getAddVisitUrl();

        JSONObject request = new JSONObject();
        try {
            request.put("PointID", placeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("gg", request.toString());

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, request, baseResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        mRequestQueue.add(jsonObjectRequest);

    }

    public void getAppVersion(final RequestListener requestListener,
                              Response.ErrorListener errorListener) {

        final String url = urlHandler.getAppVersionUrl();

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            requestListener.successResponse(response.get("Version"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, errorListener);

        mRequestQueue.add(jsObjRequest);
    }


}


