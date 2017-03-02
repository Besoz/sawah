package com.accorpa.sawah.Authorization;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.BaseResponseListener;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.models.User;
import com.google.android.gms.vision.text.Text;

/**
 * Created by root on 16/01/17.
 */

public class AuthorizationController implements LoginListener {

//    todo destroy this structure
//    todo remove context awareness
//    todo use BaseListeners

    private static AuthorizationController authMan;
    private Context context;
    private LoginListener loginListener;

    private DataHandler dataHandler;
    private ServiceHandler serviceHandler;

    private LoginResponseListener loginResponseListener;


    public AuthorizationController(Context context, LoginListener loginListener){
        this.context = context;
        this.loginListener = loginListener;

        loginResponseListener =  new LoginResponseListener(this);
        dataHandler = DataHandler.getInstance(this.context.getApplicationContext());
        serviceHandler = ServiceHandler.getInstance(this.context.getApplicationContext());;
    }


    public void loginUser(String userID) {
        Log.d("After login", "iiiiiiiiiii");

        serviceHandler.loginUser(userID, dataHandler.getDeviceToken(), loginResponseListener);
    }
    public void loginUser(String userID, BaseRequestStateListener baseRequestStateListener) {
        Log.d("After login", "iiiiiiiiiii");

        serviceHandler.loginUser(userID, dataHandler.getDeviceToken(), loginResponseListener);
    }


    public void loginUser(String email, String password) {
        serviceHandler.loginUser(email, password, dataHandler.getDeviceToken(), loginResponseListener);
    }

    public void socialLogin(String socialUserID, String socialType, String DOB,
                            final String fullName, final String email, final Uri image,
                            final BaseRequestStateListener origionListener,
                            final Context context){


        BaseRequestStateListener socialSignupListener = new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                origionListener.failResponse(response);
            }

            @Override
            public void successResponse(ServiceResponse response) {

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
                        DataHandler.getInstance(context).loadAndSaveUserNetworkImage(image, imageLoadAndSaveListner);
                    }
                };


                final User user = response.getUser();
                user.setEmail(email);
                user.setFullName(fullName);
                DataHandler.getInstance(context).requestUdpateUser(user, updateUserListener);
            }
        };
        BaseResponseListener baseResponseListener = new BaseResponseListener();
        baseResponseListener.setOnResponseListner(socialSignupListener);

        serviceHandler.signupUser(socialUserID, socialType, DOB, dataHandler.OS,
                dataHandler.getDeviceToken(), baseResponseListener);

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

        }else{
//        save user
            saveUser(user);



//        load user image
            dataHandler.loadAndSaveUserNetworkImage(Uri.parse(user.getImageLocation()), new BaseRequestStateListener() {
                @Override
                public void failResponse(ServiceResponse response) {
                    loginListener.loginFailed("");

                }

                @Override
                public void successResponse(ServiceResponse response) {
                    //        notify calling activity
                    loginListener.loginSuccess(user);
                }
            });
        }
    }

    @Override
    public void loginFailed(String message) {

        //        notify calling activity
        loginListener.loginFailed(message);

    }

    @Override
    public void loginError() {
        //        notify calling activity
        loginListener.loginError();
    }

    public void skipLogin(){
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
}
