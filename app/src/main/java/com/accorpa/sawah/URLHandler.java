package com.accorpa.sawah;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Bassem on 12/01/17.
 */
public class URLHandler {
    private static URLHandler ourInstance;

    private String Serverpath, loginPath, registerPath;

    private DataHandler dataHandler;

    public static URLHandler getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new URLHandler(context);
        }
        return ourInstance;
    }

    private URLHandler(Context context) {

        dataHandler = DataHandler.getInstance(context);

        String scheme =  context.getString(R.string.connection_scheme);
        String authority =  context.getString(R.string.host_name);
        loginPath = context.getString(R.string.login_service_url);
        registerPath = context.getString(R.string.register_service_url);


        Uri.Builder builder = new Uri.Builder();
        builder.scheme(scheme).authority(authority);

        Serverpath = builder.build().toString();

    }

    public String getLoginUrl(String userName, String password, String deviceToken) {

        Uri.Builder loginParams = new Uri.Builder();
        loginParams.appendQueryParameter("username", userName)
                            .appendQueryParameter("Password", password)
                            .appendQueryParameter("os", dataHandler.OS)
                            .appendQueryParameter("devicetoken", deviceToken);
        String loginUrlStr = loginParams.build().toString();

        loginUrlStr = Serverpath + loginPath + loginParams;

        return loginUrlStr;
    }

    public String getSigupUrl() {
        return Serverpath + registerPath;
    }
}
