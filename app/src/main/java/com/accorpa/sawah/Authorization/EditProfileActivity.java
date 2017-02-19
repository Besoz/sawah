package com.accorpa.sawah.Authorization;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.BaseResponseListner;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.User;
import com.android.volley.Response;

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

        user = DataHandler.getInstance(this).getUser();

        editProfileFragment =  EditProfileFragment.newInstance(user);
        editPasswordFragment = EditPasswordFragment.newInstance();

        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, editProfileFragment);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
                data.getData() != null) {
            try {

                final Bitmap bitmap = DataHandler.getInstance(this).getImage(data.getData());

                BaseResponseListner mResponseListner = new BaseResponseListner() {
                    @Override
                    public void onResponse(JSONObject response) {
                        super.onResponse(response);
                        if(isStatusSuccess()){

                            editProfileFragment.setImageBitmap(bitmap);
                        }else{
                            Log.d("Update user", "fail");
                        }
                        showProgress(false);
                    }
                };
                showProgress(true);

                DataHandler.getInstance(this).requestUpdateUserImage(mResponseListner, this.getApplicationContext(),
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
                PICK_IMAGE_REQUEST);
    }

    @Override
    public Bitmap getProfileImage() {
        return null;
    }

    @Override
    public boolean hasProfileImage() {
        return false;
    }

    @Override
    public void editPassword() {
        ft  = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, editPasswordFragment);
        ft.commit();
    }

    @Override
    public void updateUser() {

        BaseResponseListner responseListner = new BaseResponseListner() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if(isStatusSuccess()){
                    NavigationHandler.getInstance().startCategoriesListActivity(EditProfileActivity.this);
                }else{
                    Log.d("Update user", "fail");
                }
                showProgress(false);
            }
        };
        showProgress(true);
        DataHandler.getInstance(this).requestUdpateUser(user, responseListner);
    }

    @Override
    public void updatePassword(final BaseResponseListner mResponse, String currentPasswordStr,
                               String newPasswordStr, String confirmPasswordStr) {

        BaseResponseListner mResponseListner = new BaseResponseListner() {
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

}
