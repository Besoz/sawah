package com.accorpa.sawah;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Bassem on 14/01/17.
 */
public class NavigationHandler {
    private static NavigationHandler ourInstance = new NavigationHandler();

    public static NavigationHandler getInstance() {
        return ourInstance;
    }

    private NavigationHandler() {
    }

    public void startSignupActivity(Context context){
        Intent signUpActivity = new Intent(context, SignupActivity.class);
        context.startActivity(signUpActivity);
    }

    public void startLoginActivity(Context context, String userID) {

        Intent loginActivity = new Intent(context, LoginActivity.class);
        loginActivity.putExtra("UserID", userID);
        context.startActivity(loginActivity);
    }

    public void startCityActivity(Context context) {
        Intent cityActivity = new Intent(context, CategoriesListActivity.class);
        context.startActivity(cityActivity);
    }
}
