package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.net.Uri;

import com.accorpa.sawah.R;

/**
 * Created by Bassem on 12/01/17.
 */
public class URLHandler {
    private static URLHandler ourInstance;

    private String Serverpath, loginPath, registerPath, categoriesPath, placesPath, citiesPath,
            addCommentPath, updateUserDataPath, updateUserImagePath, changePasswordPath;

//    private DataHandler dataHandler;

    public static URLHandler getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new URLHandler(context);
        }
        return ourInstance;
    }

    private URLHandler(Context context) {

//        dataHandler = DataHandler.getInstance(context);

        String scheme =  context.getString(R.string.connection_scheme);
        String authority =  context.getString(R.string.host_name);
        loginPath = context.getString(R.string.login_service_url);
        registerPath = context.getString(R.string.register_service_url);
        categoriesPath = context.getString(R.string.categories_list_service_url);
        placesPath = context.getString(R.string.places_list_service_url);
        citiesPath =  context.getString(R.string.cities_service_url);
        addCommentPath = context.getString(R.string.add_comment_url);
        updateUserDataPath = context.getString(R.string.update_user_data_url);;
        updateUserImagePath = context.getString(R.string.update_user_image_url);;
        changePasswordPath = context.getString(R.string.change_password_url);;

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme).authority(authority);

        Serverpath = builder.build().toString();

    }

    public String getLoginUrl(String userName, String password, String deviceToken) {

        Uri.Builder loginParams = new Uri.Builder();
        loginParams.appendQueryParameter("username", userName)
                            .appendQueryParameter("Password", password)
                            .appendQueryParameter("os", "Android")
                            .appendQueryParameter("devicetoken", deviceToken);
        String loginUrlStr = loginParams.build().toString();

        loginUrlStr = Serverpath + loginPath + loginParams;

        return loginUrlStr;
    }
    public String getLoginUrl(String userID, String token) {

        Uri.Builder loginParams = new Uri.Builder();
        loginParams.appendQueryParameter("userID", userID)
                .appendQueryParameter("os", "Android")
                .appendQueryParameter("devicetoken", token);
        String loginUrlStr = loginParams.build().toString();

        loginUrlStr = Serverpath + loginPath + loginParams;

        return loginUrlStr;
    }


    public String getSigupUrl() {
        return Serverpath + registerPath;
    }


    public String getCategoriesServiceUrl(String cityID) {

        Uri.Builder placesParams = new Uri.Builder();
        placesParams.appendQueryParameter("cityID", cityID);

        String placesListUrlStr = placesParams.build().toString();

        return Serverpath+categoriesPath+placesListUrlStr;
    }


    public String getPlacesServiceUrl(String cityID, String categoryID) {

        Uri.Builder placesParams = new Uri.Builder();
        placesParams.appendQueryParameter("catid", categoryID)
                .appendQueryParameter("cityID", cityID);
        String placesListUrlStr = placesParams.build().toString();

        placesListUrlStr = Serverpath + placesPath + placesParams;

        return placesListUrlStr;
    }

    public String getCitiesServiceUrl() {
        return Serverpath + citiesPath;
    }

    public String getAddCommentUrl() {
        return Serverpath+addCommentPath;
    }

    public String getUpdateUserDataUrl() {
        return Serverpath+updateUserDataPath;
    }
    public String getUpdateUserImageUrl() {
        return Serverpath+updateUserImagePath;
    }
    public String getChangePasswordUrl() {
        return Serverpath+changePasswordPath;
    }

    public String getServerpath(){
        return Serverpath;
    }
}
