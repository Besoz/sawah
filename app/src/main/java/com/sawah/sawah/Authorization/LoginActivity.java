package com.sawah.sawah.Authorization;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.R;
import com.sawah.sawah.ServiceResponse;
import com.sawah.sawah.custom_views.CustomButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static android.Manifest.permission.READ_CONTACTS;


import com.sawah.sawah.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, LoginListener {

    /**
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private AuthorizationController authorizationController;


    private ImageButton mFacebookLoginButton, mtwitterLoginButton, mgplusLoginButton;
    private CallbackManager callbackManager;
    private TwitterLoginButton twitterLoginButton;
    private GoogleApiClient mGoogleApiClient;
//    String uid;
//    String email;
    String name;

    private static final int RC_SIGN_IN = 0;
    private BaseRequestStateListener baseRequestStateListener;

//    private boolean mIntentInProgress;
//
//    private boolean mShouldResolve;
//
//    private ConnectionResult connectionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Utils.getInstance().setTypefaceToInputLayout(this, (TextInputLayout) findViewById(R.id.emailTextLayout));
        Utils.getInstance().setTypefaceToInputLayout(this, (TextInputLayout) findViewById(R.id.passwordTextLayout));
        Utils.getInstance().changeStatusBarColor(this);

        authorizationController = new AuthorizationController(this, this);

        baseRequestStateListener = new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {

                Log.d("failResponse", "failResponse");
                showProgress(false);
            }

            @Override
            public void successResponse(ServiceResponse response) {
                showProgress(false);
                startMainActivity();

            }
        };

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        CustomButton mEmailSignInButton = (CustomButton) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        CustomButton newUserButton = (CustomButton) findViewById(R.id.new_user_button);
        newUserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewUser();
            }
        });

        CustomButton forgetPasswordButton = (CustomButton) findViewById(R.id.forget_password_button);
        forgetPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startRetrievePasswordActivity();
            }
        });

        CustomButton skipButton = (CustomButton) findViewById(R.id.skip_login_button);
        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                authorizationController.skipLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        signInWithFacebook();
        signInWithTwitter();
        signInWithGoogle();

    }

    @Override
    protected void onStart() {
        super.onStart();


        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void signInWithGoogle() {
        mgplusLoginButton = (ImageButton) findViewById(R.id.gplus_login_btn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.google_app))
//                .requestEmail()
//                .build();

        GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult result) {
                Log.d("onConnectionFailed", "onConnectionFailed");
                if (!result.hasResolution()) {
                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), LoginActivity.this,
                            0).show();
                    return;
                }

            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        mgplusLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });

    }

    private void signInWithTwitter() {
        mtwitterLoginButton = (ImageButton) findViewById(R.id.twitter_login_btn);
        twitterLoginButton = (TwitterLoginButton) new TwitterLoginButton(this);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls\
//                Log.d("g", "Success twitter login");

                final TwitterSession session = result.data;
                final TwitterAuthToken authToken = session.getAuthToken();
                String accessToken = authToken.token;
                final String userName = session.getUserName();
                final long id = session.getUserId();

                Twitter.getApiClient().getAccountService().verifyCredentials(true, false, new Callback<com.twitter.sdk.android.core.models.User>() {
                    @Override
                    public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                        result.data.getId();
                        Log.d("Twitter",  id + " "+ result.data.getId()+ " "+result.data.name + " " +result.data.profileImageUrl);
                        showProgress(true);

                        authorizationController.socialLogin(userName, "TW", "",
                                result.data.name, "", Uri.parse(result.data.profileImageUrl),
                                baseRequestStateListener, LoginActivity.this);

                    }

                    @Override
                    public void failure(TwitterException e) {
                        showProgress(false);

                    }
                });
            }
            @Override
            public void failure(TwitterException exception) {
//                 Do  on failureLsomethingog.d("g", "Failed twitter Login");
                Log.d("twitter failure", "TwitterSession"); // "com.twitter.android"
//                ViewHandler.getInstance().openPlayStoreToInstallApp(LoginFragment.this.getContext(), "com.twitter.android");

            }
        });
        mtwitterLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterLoginButton.callOnClick();
            }
        });
    }

    private void signInWithFacebook() {
//        FacebookSdk.sdkInitialize(this);

//        final LoginButton facebookLoginButton = (LoginButton) new LoginButton(this);
//        facebookLoginButton.setReadPermissions("public_profile","email", "user_location");

        callbackManager = CallbackManager.Factory.create();

        Log.d("Facbook", AccessToken.getCurrentAccessToken()+" "+ Profile.getCurrentProfile());

        mFacebookLoginButton = (ImageButton) findViewById(R.id.facebook_login_btn);
        mFacebookLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile"));
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Log.d("facebook login", loginResult.getAccessToken().getToken());

                Uri pictureUri = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
                final String userName = Profile.getCurrentProfile().getName();

                Log.d("Facebook", pictureUri.toString()+" "+name);
//                attemptFacebookLogin(loginResult);

                showProgress(true);
                authorizationController.socialLogin(Profile.getCurrentProfile().getId(), "FB",
                        "", userName, "", pictureUri, baseRequestStateListener, LoginActivity.this);
            }

            @Override
            public void onCancel() {
                // App code
                showProgress(false);

                Log.d("facebook login", "onCancel");

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                showProgress(false);

                Log.d("facebook login", "onError");

            }
        });
    }

    private void startRetrievePasswordActivity() {
        NavigationHandler.getInstance().startRetrievePasswordActivity(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
        else if(TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {
            Log.d("g", "Twitter login"+resultCode+" "+requestCode);

            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }else {
            Log.d("g", "facebookLogin " + resultCode + " " + requestCode);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        System.out.println(result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();

            Log.d("Google", personName +" "+acct.getEmail() + acct.getId()+acct.getPhotoUrl());

            showProgress(true);

            authorizationController .socialLogin(personName, "GO", "", personName, acct.getEmail(),
                    acct.getPhotoUrl(), baseRequestStateListener, LoginActivity.this);

        }
    }

    private void createNewUser() {
        NavigationHandler.getInstance().startSignupActivity(this);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !AuthorizationController.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.enter_user_name));
            focusView = mEmailView;
            cancel = true;
        } else if (!AuthorizationController.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            authorizationController.loginUser(email, password);
        }
    }

    private void attemptLogin(String userID) {
        authorizationController.loginUser(userID);
    }



    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    @Override
    public void loginSuccess(User user) {
        showProgress(false);
        startMainActivity();
    }

    private void startMainActivity() {
        NavigationHandler.getInstance().startAfterLoginctivity(this);

    }

    @Override
    public void loginFailed(String message) {
        showProgress(false);
        mPasswordView.setError(message);
        mPasswordView.requestFocus();
    }

    @Override
    public void loginError() {
        showProgress(false);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
}

