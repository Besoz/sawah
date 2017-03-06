package com.sawah.sawah.Authorization;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.sawah.sawah.BaseActivity;
import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.BaseResponseListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.R;
import com.sawah.sawah.ServiceResponse;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.custom_views.CustomEditText;
import com.sawah.sawah.custom_views.CustomTextView;
import com.sawah.sawah.models.User;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
    private CustomTextView birthDate;
//    private DatePickerDialog dpd;
    private CustomEditText userEmail, userName, userPhone;
    private Spinner genderSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        user = DataHandler.getInstance(this).getUser();


        userName = (CustomEditText) findViewById(R.id.user_name);
        userEmail = (CustomEditText) findViewById(R.id.user_email);
        userPhone = (CustomEditText) findViewById(R.id.user_phone);
//        gender_spinner = (Spinner) findViewById(R.id.gender_spinner);

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

        TableRow tr = (TableRow)findViewById(R.id.edit_password_row);
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPassword();
            }
        });

//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                DateFormat formatter = new SimpleDateFormat(User.DATE_FORMAT);
//                String date = formatter.format(newDate.getTime());
//
//                user.setBirthDate(date);
//                birthDate.setText(date);
//            }
//        };

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(user.getBirthDateObject());

//        dpd = new DatePickerDialog(this,R.style.MyDatePickerDialogTheme ,date, calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        dpd.updateDate(calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//
//        dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        Calendar now = Calendar.getInstance();
        final DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        DateFormat formatter = new SimpleDateFormat(User.DATE_FORMAT);
                        String date = formatter.format(newDate.getTime());

                        user.setBirthDate(date);
                        birthDate.setText(date);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
//                dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_1);
        dpd.showYearPickerFirst(true);


        birthDate = (CustomTextView) findViewById(R.id.birth_date);
        birthDate.setText(user.getBirthDate());
        TableRow birthDateRow = (TableRow) findViewById(R.id.date_row);

        birthDateRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dpd.setTitle("");
//                dpd.show();
//                DatePickerBuilder dpb2 = new DatePickerBuilder()
//                        .setFragmentManager(getSupportFragmentManager())
//                        .setStyleResId(R.style.BetterPickersDialogFragment)
//                        .setYearOptional(true);
//                dpb2.show();

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);

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

        genderSpinner.setAdapter(dataAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        if(user.getSex().equals(User.FEMALE))
            genderSpinner.setSelection(1);
        else
            genderSpinner.setSelection(0);

        CustomButton updateProfileButton = (CustomButton) this.findViewById(R.id.update_profile_buttton);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptupdateUser();
            }
        });

        TableRow row1 = (TableRow) findViewById(R.id.name_row);
        TableRow row2 = (TableRow) findViewById(R.id.email_row);
        TableRow row3 = (TableRow) findViewById(R.id.phone_row);
        TableRow row4 = (TableRow) findViewById(R.id.sex_row);


        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(userName, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        row2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(userEmail, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userPhone.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(userPhone, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        row4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genderSpinner.performClick();
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
                PICK_IMAGE_REQUEST, 1);
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
                MaterialDialog m = DialogHelper.getInstance()
                        .showError(EditProfileActivity.this,
                                response.getMessage());
            }

            @Override
            public void successResponse(ServiceResponse response) {
                showProgress(false);
                MaterialDialog m = DialogHelper.getInstance()
                        .showSuccess(EditProfileActivity.this,
                                getString(R.string.profile_update_success));
                m.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        NavigationHandler.getInstance().startCategoriesListActivity(EditProfileActivity.this);
                        onBackPressed();
                    }
                });
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
