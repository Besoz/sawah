package com.accorpa.sawah.Authorization;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.BaseResponseListener;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomEditText;
import com.accorpa.sawah.custom_views.CustomTextView;
import com.accorpa.sawah.models.User;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity {

    private final static int PICK_IMAGE_REQUEST = 100;



    private FragmentTransaction ft;

    private User user;

    private CircleImageView profileImage;
    private CustomEditText birthDate;
    private DatePickerDialog dpd;
    private CustomEditText userEmail, userName, userPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        user = DataHandler.getInstance(this).getUser();


        userName = (CustomEditText) findViewById(R.id.user_name);
        userEmail = (CustomEditText) findViewById(R.id.user_email);
        userPhone = (CustomEditText) findViewById(R.id.user_phone);

        userName.setText(user.getFullName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getMobileNumber());

        CustomButton editProfilePicButton = (CustomButton) findViewById(R.id.edit_profile_picture);
        editProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        profileImage = (CircleImageView) findViewById(R.id.profile_image);

        if(hasProfileImage()){
            profileImage.setImageBitmap(getProfileImage());
        }

        CustomTextView editPassword = (CustomTextView) findViewById(R.id.edit_password);
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword();
            }
        });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                DateFormat formatter = new SimpleDateFormat(User.DATE_FORMAT);
                String date = formatter.format(newDate.getTime());

                user.setBirthDate(date);
                birthDate.setText(date);
            }
        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(user.getBirthDateObject());

        dpd = new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_WEEK));

        dpd.updateDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_WEEK));

        dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());


        birthDate = (CustomEditText) findViewById(R.id.birth_date);
        birthDate.setText(user.getBirthDate());
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show();
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.gender_spinner);

        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.male));
        list.add(getString(R.string.female));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(),
                        getResources().getString(R.string.default_font));
                ((TextView) v).setTypeface(externalFont);

                return v;
            }


            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(),
                        getResources().getString(R.string.default_font));
                ((TextView) v).setTypeface(externalFont);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    user.setSex(User.MALE);
                }else{
                    user.setSex(User.FEMALE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        CustomButton updateProfileButton = (CustomButton) this.findViewById(R.id.update_profile_buttton);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptupdateUser();
            }
        });

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
                            profileImage.setImageBitmap(bitmap);
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

    public void selectImage() {
        NavigationHandler.getInstance().startImagePickerForResult(EditProfileActivity.this,
                PICK_IMAGE_REQUEST, false);
    }

    public Bitmap getProfileImage() {

        return DataHandler.getInstance(this)
                .loadImageFromStorage(user.getLocalImagePath());
    }

    public boolean hasProfileImage() {
        return !TextUtils.isEmpty(user.getLocalImagePath());
    }

    public void editPassword() {
        NavigationHandler.getInstance().startEditPasswordActivity(this);
    }


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













    public void setImageBitmap(Bitmap imageBitmap) {
        profileImage.setImageBitmap(imageBitmap);
    }

    private void attemptupdateUser() {
        // Reset errors.
        userEmail.setError(null);

        // Store values at the time of the login attempt.
        String userEmailStr = userEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(userEmailStr) && !AuthorizationController.isEmailValid(userEmailStr)) {
            userEmail.setError(getString(R.string.error_invalid_email));
            focusView = userEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            user.setEmail(userEmailStr);
            user.setFullName(userName.getText().toString());
            user.setMobileNumber(userPhone.getText().toString());


            updateUser();
        }
    }
















    @Override
    protected String getToolbarTitle() {
        return "";
    }






















}
