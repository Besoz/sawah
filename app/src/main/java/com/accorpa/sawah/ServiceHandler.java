package com.accorpa.sawah;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.util.LruCache;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bassem on 10/01/17.
 */
public class ServiceHandler {

    private static ServiceHandler ourInstance;

    private Context context;
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
        this.context = context.getApplicationContext();
        hostName = context.getString(R.string.host_name);

// Instantiate the cache
        Cache cache = new DiskBasedCache(this.context.getCacheDir(), 100*1024 * 1024); // 1MB cap*100

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        mRequestQueue.start();

        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(this.context));

        urlHandler = URLHandler.getInstance(this.context);
    }



    public void loginUser(String userName, String password, String deviceToken, LoginResponseListener loginResponseListener) {

        JsonObjectRequest jsonObjectRequest = getLoginRequest(userName, password, deviceToken, loginResponseListener);

        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonObjectRequest);

    }

    private JsonObjectRequest getLoginRequest(String userName, String password, String deviceToken, LoginResponseListener loginResponseListener){

        String loginUrl = urlHandler.getLoginUrl(userName, password, deviceToken);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginUrl, null,
               loginResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        return jsonObjectRequest;
    }

    public void signupUser(JSONObject userData, SignupResponseListener signupResponseListener) {

        JsonObjectRequest jsonObjectRequest = getSignupRequest(userData, signupResponseListener);
        // Add the request to the RequestQueue.
        mRequestQueue.add(jsonObjectRequest);
    }



    private JsonObjectRequest getSignupRequest(JSONObject userData, SignupResponseListener signupResponseListener){

        String signupUrl = urlHandler.getSigupUrl();

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, signupUrl, userData,
                signupResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        return jsonObjectRequest;
    }

    public void loginUser(String userID, String deviceToken, LoginResponseListener loginResponseListener) {

        String loginUrl = urlHandler.getLoginUrl(userID, deviceToken);

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, loginUrl, null,
                loginResponseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    public void requestCategoriesList(final DataHandler dataHandler, final CategoriesListActivity activity){
        Log.d("gg", "requesting");

        String serviceUrl = urlHandler.getCategoriesServiceUrl("1");
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("gg", response.toString());
                dataHandler.recieveCategoriesList(response, activity);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(categoriesArrayRequest);

    }

    public void requestPlacesArray(final DataHandler dataHandler,
                                   final PlacesListActivity placesListActivity, String cityID,
                                   String categoryID) {
        Log.d("gg", "requesting");

        String serviceUrl = urlHandler.getPlacesServiceUrl(cityID, categoryID);
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("gg", response.toString());
                dataHandler.recievePlacesList(response, placesListActivity);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(categoriesArrayRequest);

    }


    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    public void requestCitiesList(final DataHandler dataHandler,
                                  final CitiesListActivity citiesListActivity) {

        String serviceUrl = urlHandler.getCitiesServiceUrl();
        Log.d("gg", "requesting"+urlHandler);

        JsonArrayRequest categoriesArrayRequest = new JsonArrayRequest(Request.Method.GET,
                serviceUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("gg", response.toString());
                dataHandler.recieveCitiesList(response, citiesListActivity);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

        mRequestQueue.add(categoriesArrayRequest);
    }

    public void postComment(Response.Listener<JSONObject> commentResponseListner,
                            String placeID, String text, String userID, float rating) {
        Log.d("comment", placeID+" "+text+" "+userID+" "+rating);

        String addCommentUrl = urlHandler.getAddCommentUrl();

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                addCommentUrl, getPostCommentRequest(placeID, text, userID, rating),
                commentResponseListner, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
            }
        });

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
}


