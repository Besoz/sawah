package com.accorpa.sawah.AddNewPlace;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.BitmapImage;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.models.Place;
import com.darsh.multipleimageselect.helpers.Constants;

import java.util.ArrayList;

public class AddNewPlaceActivity extends BaseActivity implements MapAllocationFragment.OnFragmentInteractionListener, AddingPlaceDetailsFragment.OnFragmentInteractionListener {

    private static final int PICK_IMAGE_REQUEST = 100;
    private Place newPlace;
    private MapAllocationFragment placeLocationFragment;
    private AddingPlaceDetailsFragment addingPlaceDetailsFragment;

    private FragmentTransaction ft;
    private boolean imageSelcted;

    private ArrayList<BitmapImage> bitmapImages;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_new_place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        newPlace = new Place();

        placeLocationFragment =  MapAllocationFragment.newInstance(newPlace);
        addingPlaceDetailsFragment =  AddingPlaceDetailsFragment.newInstance();

        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, placeLocationFragment);
        ft.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void selectPlaceImages() {
        NavigationHandler.getInstance().startImagePickerForResult(AddNewPlaceActivity.this,
                PICK_IMAGE_REQUEST, 5);
    }

    @Override
    public void addNewPlace(Place place) {
        Log.d("add new Place", "1");
        showProgress(true);
        DataHandler.getInstance(this).addNewPlace(bitmapImages, place, new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {
                showProgress(false);
            }

            @Override
            public void successResponse(ServiceResponse response) {
                showProgress(false);
                NavigationHandler.getInstance().startCategoriesListActivity(AddNewPlaceActivity.this);
            }

        });
    }

    @Override
    public boolean isImageSelected() {
        return imageSelcted;
    }

    @Override
    public void placeDatilsUpdated(Place place) {

        addingPlaceDetailsFragment.setPlace(place);

        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, addingPlaceDetailsFragment);
        ft.commit();

    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("on resume", "resume");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null ) {

            bitmapImages= data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);

            DataHandler.getInstance(this).loadImageBitmaps(bitmapImages);


            if (bitmapImages.size() > 0 ){
                addingPlaceDetailsFragment.showSelectedImages(bitmapImages);
                imageSelcted = true;
            }else{
                imageSelcted = false;
            }



        }else{
            Log.d("add new Place", "big error");
        }

    }
    protected String getToolbarTitle() {
        return getString(R.string.add_place);
    }

//    todo remove location from the fragment or find better structure
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(placeLocationFragment.isVisible()){
            placeLocationFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}
