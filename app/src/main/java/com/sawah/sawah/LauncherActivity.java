package com.sawah.sawah;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.*;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sawah.sawah.Authorization.AuthorizationController;
import com.sawah.sawah.Authorization.LoginListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.SharedPreferencesController;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.models.User;

public class LauncherActivity extends AppCompatActivity implements LoginListener {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private final int SPLASH_DISPLAY_LENGTH = 1000;

//    private View mSplashView, mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        SharedPreferencesController.getInstance(this).updateBadgeNumber(0);
        DataHandler.getInstance(this).updateAppNotification(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            DataHandler.getInstance(this).checkAppVersion(new RequestListener() {
                @Override
                public void failResponse(Object response) {
//                    version the same
                    continueLaunch();
                }

                @Override
                public void successResponse(Object response) {
//                    new version
                    DialogHelper.getInstance().showNewVersionDialog(LauncherActivity.this,
                            (String) response);
                    Log.d("Version", "Success");

                }
            });
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void continueLaunch() {
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

//    private void showProgress(final boolean show) {
//        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//        mSplashView.setVisibility(show ? View.GONE : View.VISIBLE);
//    }
}
