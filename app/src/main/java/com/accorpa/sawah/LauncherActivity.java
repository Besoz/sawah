package com.accorpa.sawah;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.accorpa.sawah.Authorization.AuthorizationManger;
import com.accorpa.sawah.Authorization.LoginListener;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.models.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity implements LoginListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "64Ej42adLTs8VXNwbEE6JaOux";
    private static final String TWITTER_SECRET = "KaeNwQEFEX6ntN9z7NE7rz9SS3W6VgLnPd9vTVOZrHXgA8KWB6";


    private View mSplashView, mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_launcher);

//        intializing view
        mSplashView = findViewById(R.id.splash_view);
        mProgressView = findViewById(R.id.progress_view);

//        starting firebase cloud service
        Intent service = new Intent(this, FCMService.class);
        service.putExtra("serviceType", "register");
        this.startService(service);

//        login user if exist else show login activity
        if(DataHandler.getInstance(this).userExist()){
            String userID = DataHandler.getInstance(this).getUser().getUserID();
            AuthorizationManger authorizationManger =
                    new AuthorizationManger(this.getApplicationContext(), this);
//            showProgress(true);
            authorizationManger.loginUser(userID);
        }else{
            NavigationHandler.getInstance().startLoginActivity(this);
        }
    }

    @Override
    public void loginSuccess(User user) {
        Log.d("After login", "iiiiiiiiiii");
        NavigationHandler.getInstance().startAfterLoginctivity(this);
    }

    @Override
    public void loginFailed(String message) {
//        todo handle saved user login failer
    }

    @Override
    public void loginError() {
//        todo handle network error
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mSplashView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
