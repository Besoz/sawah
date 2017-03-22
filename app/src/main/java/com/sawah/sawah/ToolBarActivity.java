package com.sawah.sawah;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.custom_views.CustomTextView;

import java.util.Locale;

public class ToolBarActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener,
        Response.ErrorListener {

    public static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private static final int LOCATION_PERMISSION_REQUEST = 50;

    protected LinearLayout mProgressView;
    protected ViewGroup mainLayout;

    private CustomTextView toolbarTitle;

    protected boolean mRequestingLocationUpdates;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Glide.get(ToolBarActivity.this).clearMemory();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (CustomTextView) findViewById(R.id.toolbar_title);

        toolbarTitle.setText(getToolbarTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mainLayout = (ViewGroup) findViewById(R.id.content_base);
        mProgressView = (LinearLayout) findViewById(R.id.base_progress_bar);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView;
        try {
            contentView = inflater.inflate(getLayoutResourceId(), null, false);
            mainLayout.addView(contentView);
        }
        catch (InflateException e)
        {
            System.out.println(e.getMessage());
        }


        createLocationRequest();

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(getActionBarMenuLayout(), menu);

        MenuItem item = menu.findItem(R.id.action_search);
        if(item != null) {
            SearchView sv = (SearchView) item.getActionView();
            TextView searchText = (TextView)
                    sv.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchText.setTextColor(ContextCompat.getColor(ToolBarActivity.this,R.color.textColor));
            searchText.setHintTextColor(ContextCompat.getColor(ToolBarActivity.this,R.color.textColor));
            Typeface myCustomFont = Typeface.createFromAsset(getAssets(),getString(R.string.default_font));
            searchText.setTypeface(myCustomFont);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_back:
                this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected int getLayoutResourceId() {
        return R.layout.content_base;
    }

    public void setLocle() {

        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    protected int getActionBarMenuLayout() {
        return R.menu.base;
    }


    protected void showProgress(final boolean show) {
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mainLayout.setVisibility(show ? View.GONE : View.VISIBLE);

    }

    protected void hideToolbarTitle() {
        toolbarTitle.setVisibility(View.GONE);
    }

    protected String getToolbarTitle() {
        return getString(R.string.app_name);
    }

    protected void setToolbarTitle(String s) {
        toolbarTitle.setText(s);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d("Location", "Connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    protected void startLocationUpdates() {
        Log.d("Location", "startLocationUpdates");


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d("Request location", "not granted");

            // Should we show an explanation?
            if (shouldShowExplaination()) {
                Log.d("Request location", "explain");

                showPermissionDialog();

            } else {
                Log.d("Request location", "request");

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        } else {

            Log.d("Request location", "requesting");

            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            if (requestOpenGps()) {
                Log.d("Request location", "request open enabled 1");

            }else{

                Log.d("Request location", "Error gps enabled 1");

            }
        }
    }

    protected void showPermissionDialog() {

        new MaterialDialog.Builder(ToolBarActivity.this)
                .title(R.string.permission_request_title)
                .content(R.string.permission_request_message)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).negativeText(R.string.cancel_text)
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    protected boolean shouldShowExplaination() {

        return ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Location", "changed");
    }

    protected void stopLocationUpdates() {
        if(mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }




    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(mGoogleApiClient.isConnected() && mRequestingLocationUpdates){
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d("Request location", "onResume");
        SearchView searchView = (SearchView)findViewById(R.id.customSeatch);
        if(searchView != null)
        {
            searchView.clearFocus();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("Request location", "onRequestPermissionsResult");


        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {

                Log.d("Request location", "Granted");

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    Log.d("Request location", "Request");

                    if(mGoogleApiClient.isConnected()){
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, this);

                        if (requestOpenGps()) {
                            Log.d("Request location", "request open enabled 1");

                        }else{

                            Log.d("Request location", "Error gps enabled 1");

                        }
                    }




                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    protected boolean requestOpenGps(){

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Log.d("Request location", "1");
            new MaterialDialog.Builder(ToolBarActivity.this)
                                       .title(R.string.enable_gps_title)
                                       .content(R.string.enable_gps_message)
                                       .positiveText(R.string.agree)
                                       .onPositive(new MaterialDialog.SingleButtonCallback() {
                                       @Override
                                       public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                              startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                       }}).negativeText("cancel")
                                       .autoDismiss(true)
                                       .titleGravity(GravityEnum.CENTER)
                                       .contentGravity(GravityEnum.CENTER)
                                        .show();

            return false;
        }else{


            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("Request location", "for result");

            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d("Memory", "Cleared isa ");
        Glide.get(ToolBarActivity.this).clearMemory();

        super.onDestroy();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        DialogHelper.getInstance().showNetworkErrorDialog(this);
        finish();
    }
}
