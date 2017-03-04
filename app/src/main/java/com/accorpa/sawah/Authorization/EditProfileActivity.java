package com.accorpa.sawah.Authorization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.BaseResponseListener;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.models.User;

import org.json.JSONObject;

import java.io.IOException;

public class EditProfileActivity extends BaseActivity implements EditProfileFragment.OnFragmentInteractionListener, EditPasswordFragment.OnFragmentInteractionListener{

    private final static int PICK_IMAGE_REQUEST = 100;

    private EditProfileFragment editProfileFragment;
    private EditPasswordFragment editPasswordFragment;


    private FragmentTransaction ft;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        user = DataHandler.getInstance(this).getUser();

        editProfileFragment =  EditProfileFragment.newInstance(user);
        editPasswordFragment = EditPasswordFragment.newInstance();

        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, editProfileFragment);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            try {

                final Bitmap bitmap = DataHandler.getInstance(this).getImage(data.getData());

                BaseResponseListener mResponseListner = new BaseResponseListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        super.onResponse(response);
                        if(isStatusSuccess()){
                            setNavBarUserImage(bitmap);
                            editProfileFragment.setImageBitmap(bitmap);
                        }else{
                            Log.d("Update user", "fail");
                        }
                        showProgress(false);
                    }
                };
                showProgress(true);

                DataHandler.getInstance(this).requestUpdateUserImage(mResponseListner,
                        data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void selectImage() {
        NavigationHandler.getInstance().startImagePickerForResult(EditProfileActivity.this,
                PICK_IMAGE_REQUEST, false);
    }

    @Override
    public Bitmap getProfileImage() {

        return DataHandler.getInstance(this)
                .loadImageFromStorage(user.getLocalImagePath());
    }

    @Override
    public boolean hasProfileImage() {
        return !TextUtils.isEmpty(user.getLocalImagePath());
    }

    @Override
    public void editPassword() {
        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, editPasswordFragment);
        ft.commit();
    }

    @Override
    public void updateUser() {

        BaseRequestStateListener baseRequestStateListener =  new BaseRequestStateListener(){
            @Override
            public void failResponse(ServiceResponse response) {
                Log.d("Update user", "fail");

                showProgress(false);
            }

            @Override
            public void successResponse(ServiceResponse response) {
                NavigationHandler.getInstance().startCategoriesListActivity(EditProfileActivity.this);
                showProgress(false);

            }
        };

        showProgress(true);
        DataHandler.getInstance(this).requestUdpateUser(user, baseRequestStateListener);
    }

    @Override
    public void updatePassword(final BaseResponseListener mResponse, String currentPasswordStr,
                               String newPasswordStr, String confirmPasswordStr) {

        BaseResponseListener mResponseListner = new BaseResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if(isStatusSuccess()){
                    updatePasswordSuccess();
                }else{
                    mResponse.onResponse(response);
                }
                showProgress(false);
            }
        };
        showProgress(true);
        DataHandler.getInstance(this).requestUdpateUserPassword(mResponseListner, currentPasswordStr,
                newPasswordStr, confirmPasswordStr);
    }

    @Override
    public void updatePasswordSuccess() {
        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, editProfileFragment);
        ft.commit();
    }

    @Override
    protected String getToolbarTitle() {
        return "";
    }

}
