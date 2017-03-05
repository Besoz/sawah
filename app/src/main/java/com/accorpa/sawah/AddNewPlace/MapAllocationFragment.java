package com.accorpa.sawah.AddNewPlace;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomEditText;
import com.accorpa.sawah.models.Place;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapAllocationFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private OnFragmentInteractionListener mListener;

    private CustomEditText placeNameEditText;

    private Place place;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;
    private Location userSelectedLocation;

    private GoogleMap googleMap;

    private CustomButton nextButton;

    private ImageView imageView;

    private FrameLayout frameLayout;
    private boolean locationDenied;


    public MapAllocationFragment() {

    }


    public MapAllocationFragment(Place place) {
        // Required empty public constructor
        this.place = place;
    }

    public static MapAllocationFragment newInstance(Place place) {
        MapAllocationFragment fragment = new MapAllocationFragment(place);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_allocation, container, false);


        placeNameEditText = (CustomEditText) view.findViewById(R.id.place_name_edittext);

        imageView = (ImageView) view.findViewById(R.id.location_picker_pin);

        frameLayout = (FrameLayout) view.findViewById(R.id.main_map_view);

                nextButton = (CustomButton) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlaceDetails();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    private void setPlaceDetails() {
        // Reset errors.
        placeNameEditText.setError(null);

        // Store values at the time of the login attempt.
        String placeNamedStr = placeNameEditText.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(placeNamedStr)) {
            placeNameEditText.setError(getString(R.string.enter_place_name));
            focusView = placeNameEditText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            place.setPalceName(placeNamedStr);

            place.setLattitude(googleMap.getCameraPosition().target.latitude);
            place.setLongitude(googleMap.getCameraPosition().target.longitude);

            Log.d("latlong", googleMap.getCameraPosition().target.latitude+ " "+ googleMap.getCameraPosition().target.longitude);
            mListener.placeDatilsUpdated(place);
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
//        nextButton.getHeight()

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) ((View) nextButton).getLayoutParams();
        ViewGroup.MarginLayoutParams lp2 = (ViewGroup.MarginLayoutParams) ((View) placeNameEditText).getLayoutParams();


//        lp.bottomMargin+lp.topMargin;

        googleMap.setPadding(0, placeNameEditText.getHeight() + lp2.bottomMargin, 0, lp.bottomMargin+nextButton.getHeight());



        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();

        params.setMargins(0, googleMap.getProjection().toScreenLocation(googleMap.getCameraPosition().target).y - imageView.getHeight(), 0, 0);

        imageView.setLayoutParams(params);



        if (mLastLocation != null) {
//            addMarker();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowExplaination()) {

                showPermissionDialog();

            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

            if (requestOpenGps()) {
                Log.d("Request location", "request open enabled 1");

            }else{

                Log.d("Request location", "Error gps enabled 1");

            }




        }
    }

    protected boolean requestOpenGps(){

        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Log.d("Request location", "1");


            return false;
        }else{


            return true;
        }
    }

    protected void showPermissionDialog() {

        new MaterialDialog.Builder(getContext())
                .title(R.string.permission_request_title)
                .content(R.string.permission_request_message)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getContext().getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }).negativeText(R.string.cancel_text)
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    protected boolean shouldShowExplaination() {

        return ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void onPause() {

        if(mGoogleApiClient.isConnected()){
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }


        super.onPause();
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }




                    if (requestOpenGps()) {
                        Log.d("Request location", "request open enabled 1");

                    }else{

                        Log.d("Request location", "Error gps enabled 1");

                    }

                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (googleMap != null) {
            if (!locationDenied && !(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                googleMap.setMyLocationEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()),
                                20));
            }

//            addMarker();

        }


    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        void placeDatilsUpdated(Place place);
    }
}
