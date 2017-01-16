package com.accorpa.sawah;

import android.content.Context;
import android.net.Uri;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

/**
 * Created by bassem on 10/01/17.
 */
public class ServiceHandler {

    private static ServiceHandler ourInstance;

    private Context context;
    private RequestQueue mRequestQueue;
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
        Cache cache = new DiskBasedCache(this.context.getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

        mRequestQueue.start();

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
}
