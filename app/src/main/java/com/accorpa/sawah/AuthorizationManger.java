package com.accorpa.sawah;

import android.content.Context;

/**
 * Created by root on 16/01/17.
 */

public class AuthorizationManger implements LoginListener {

    private static AuthorizationManger authMan;
    private Context context;
    private LoginListener loginListener;

    private DataHandler dataHandler;
    private ServiceHandler serviceHandler;

    private LoginResponseListener loginResponseListener;


    public AuthorizationManger(Context context, LoginListener loginListener){
        this.context = context;
        this.loginListener = loginListener;

        loginResponseListener =  new LoginResponseListener(this);
        dataHandler = DataHandler.getInstance(this.context.getApplicationContext());
        serviceHandler = ServiceHandler.getInstance(this.context.getApplicationContext());;
    }


    public void loginUser(String userID) {
        serviceHandler.loginUser(userID, dataHandler.getDeiveToken(), loginResponseListener);
    }

    public void loginUser(String email, String password) {
        serviceHandler.loginUser(email, password, dataHandler.getDeiveToken(), loginResponseListener);
    }


    @Override
    public void loginSuccess(User user) {
//        save user
        dataHandler.saveUser(user);
//        notify calling activity
        loginListener.loginSuccess(user);

//        navigate to next page and clear stack

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
        NavigationHandler.getInstance().startCityActivity(context);
    }
}
