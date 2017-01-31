package com.accorpa.sawah;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class LauncherActivity extends AppCompatActivity implements LoginListener {

    private View mSplashView, mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
