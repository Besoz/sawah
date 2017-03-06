package com.sawah.sawah;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sawah.sawah.Authorization.AuthorizationController;
import com.sawah.sawah.Authorization.LoginListener;
import com.sawah.sawah.Handlers.ActivityCycleListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.Foreground;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.models.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class LauncherActivity extends AppCompatActivity implements LoginListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private View mSplashView, mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.TWITTER_KEY),
                getString(R.string.TWITTER_SECRET));
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_launcher);

//        ActivityCycleListener listener = new ActivityCycleListener() {
//            @Override
//            public void onBecameForeground() {
//                NavigationHandler.getInstance().startMainctivity(LauncherActivity.this);
//            }
//
//            @Override
//            public void onBecameBackground() {
//
//            }
//        };
//        Foreground f = Foreground.init(getApplication());
//        f.addListener(listener);
//        intializing view
        mSplashView = findViewById(R.id.splash_view);
        mProgressView = findViewById(R.id.progress_view);

//        starting firebase cloud service
        Intent service = new Intent(this, FCMService.class);
        service.putExtra("serviceType", "register");
        this.startService(service);

        Utils.getInstance().changeStatusBarColor(this);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                //        login user if exist else show login activity
                if(DataHandler.getInstance(LauncherActivity.this).userExist()){
                    String userID = DataHandler.getInstance(LauncherActivity.this).getUser().getUserID();
                    AuthorizationController authorizationController =
                            new AuthorizationController(LauncherActivity.this.getApplicationContext(),
                                    LauncherActivity.this);
//            showProgress(true);
                    authorizationController.loginUser(userID);
                }else{
                    NavigationHandler.getInstance().startLoginActivity(LauncherActivity.this);
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
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