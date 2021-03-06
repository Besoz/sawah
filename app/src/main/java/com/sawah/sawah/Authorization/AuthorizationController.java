package com.sawah.sawah.Authorization;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.BaseResponseListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.ServiceHandler;
import com.sawah.sawah.ServiceResponse;
import com.sawah.sawah.models.User;

/**
 * Created by root on 16/01/17.
 */

public class AuthorizationController implements LoginListener {

//    todo destroy this structure
//    todo remove context awareness
//    todo use BaseListeners

    private static AuthorizationController authMan;
    private LoginListener loginListener;

    private DataHandler dataHandler;
    private ServiceHandler serviceHandler;

    private LoginResponseListener loginResponseListener;


    public AuthorizationController(Context context, LoginListener loginListener){
        this.loginListener = loginListener;

        loginResponseListener =  new LoginResponseListener(this);
        dataHandler = DataHandler.getInstance(context.getApplicationContext());
        serviceHandler = ServiceHandler.getInstance(context.getApplicationContext());;
    }


    public void loginUser(String userID, Response.ErrorListener errorListener) {
        Log.d("After login", "iiiiiiiiiii");

        serviceHandler.loginUser(userID, dataHandler.getDeviceToken(), loginResponseListener,
                errorListener);
    }
    public void loginUser(String userID, BaseRequestStateListener baseRequestStateListener,
                          Response.ErrorListener errorListener) {
        Log.d("After login", "iiiiiiiiiii");

        serviceHandler.loginUser(userID, dataHandler.getDeviceToken(), loginResponseListener,
                errorListener);
    }


    public void loginUser(String email, String password, Response.ErrorListener errorListener) {
        serviceHandler.loginUser(email, password, dataHandler.getDeviceToken(),
                loginResponseListener, errorListener);
    }

    public void socialLogin(String socialUserID, String socialType, String DOB,
                            final String fullName, final String email, final Uri image,
                            final BaseRequestStateListener origionListener,
                            final Context context, final Response.ErrorListener errorListener){


        BaseRequestStateListener socialSignupListener = new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                origionListener.failResponse(response);
            }

            @Override
            public void successResponse(ServiceResponse response) {

                BaseRequestStateListener updateUserListener = getUpdateUserListener(origionListener,
                        image, context);

                final User user = response.getUser();
                user.setEmail(email);
                user.setFullName(fullName);

                DataHandler.getInstance(context).requestUpdateUser(user, updateUserListener,
                        errorListener);
            }
        };
        BaseResponseListener baseResponseListener = new BaseResponseListener();
        baseResponseListener.setOnResponseListner(socialSignupListener);

        serviceHandler.signupUser(socialUserID, socialType, DOB, dataHandler.OS,
                dataHandler.getDeviceToken(), baseResponseListener, errorListener);

    }

    private void saveUser(User updatedUser) {

        dataHandler.saveUser(updatedUser);

    }


    @Override
    public void loginSuccess(final User user) {

        Log.d("After login", "iiiiiiiiiii");

        if(dataHandler.userExist() && TextUtils.equals(dataHandler.getUser().getImageLocation(),  user.getImageLocation())){
            user.setLocalImagePath(dataHandler.getUser().getLocalImagePath());
            saveUser(user);

            loginListener.loginSuccess(user);
//            loginListener = null;

        }else{
//        save user
            saveUser(user);
            loginListener.loginSuccess(user);
//            loginListener = null;
        }
    }

    @Override
    public void loginFailed(String message) {

        //        notify calling activity
        loginListener.loginFailed(message);
//        loginListener = null;
    }

    @Override
    public void loginError() {
        //        notify calling activity
        loginListener.loginError();
//        loginListener = null;
    }

    public void skipLogin(Context context){
        NavigationHandler.getInstance().startCategoriesListActivity(context);
    }


    public static boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    public static boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public BaseRequestStateListener getUpdateUserListener(final BaseRequestStateListener origionListener,
                                                          final Uri imageURI, final Context context) {

        BaseRequestStateListener updateUserListener = new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                origionListener.failResponse(response);
            }
            @Override
            public void successResponse(ServiceResponse response) {

                final User updatedUser = response.getUser();
                saveUser(updatedUser);

                BaseRequestStateListener imageLoadAndSaveListner = new BaseRequestStateListener() {
                    @Override
                    public void failResponse(ServiceResponse response) {
                        origionListener.failResponse(response);
                    }

                    @Override
                    public void successResponse(ServiceResponse response) {
                        origionListener.successResponse(response);
                    }
                };

//                        save image locally
                DataHandler.getInstance(context).loadAndSaveUserNetworkImage(imageURI, imageLoadAndSaveListner, context);
            }
        };

        return updateUserListener;
    }
}
