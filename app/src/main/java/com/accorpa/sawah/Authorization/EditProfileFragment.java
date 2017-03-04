package com.accorpa.sawah.Authorization;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableRow;

import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomEditText;
import com.accorpa.sawah.custom_views.CustomTextView;
import com.accorpa.sawah.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CircleImageView profileImage;
    private CustomEditText birthDate;
    private DatePickerDialog dpd;
    private CustomEditText userEmail, userName, userPhone;

    private User user;


    public EditProfileFragment() {


    }
    public EditProfileFragment(User user) {
        // Required empty public constructor
        this.user = user;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(User user) {
        EditProfileFragment fragment = new EditProfileFragment(user);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userName = (CustomEditText) view.findViewById(R.id.user_name);
        userEmail = (CustomEditText) view.findViewById(R.id.user_email);
        userPhone = (CustomEditText) view.findViewById(R.id.user_phone);
//
//        TableRow nameRow = (TableRow) view.findViewById(R.id.name_row);
//        nameRow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userName.requestFocus();
//            }
//        });
//        TableRow mailRow = (TableRow) view.findViewById(R.id.mail_row);
//        mailRow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userEmail.requestFocus();
//            }
//        });
//        TableRow phoneRow = (TableRow) view.findViewById(R.id.phone_row);
//        mailRow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userPhone.requestFocus();
//            }
//        });

        userName.setText(user.getFullName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getMobileNumber());

        CustomButton editProfilePicButton = (CustomButton) view.findViewById(R.id.edit_profile_picture);
        editProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.selectImage();
            }
        });

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);

        if(mListener.hasProfileImage()){
            profileImage.setImageBitmap(mListener.getProfileImage());
        }

        CustomEditText editPassword = (CustomEditText) view.findViewById(R.id.edit_password);
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.editPassword();
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

        dpd = new DatePickerDialog(getContext(), date, user.getBirthDateObject().getYear(),
                user.getBirthDateObject().getMonth(), user.getBirthDateObject().getDay());
        dpd.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());

        birthDate = (CustomEditText) view.findViewById(R.id.birth_date);
        birthDate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                dpd.show();
            }
        });


        Spinner spinner = (Spinner) view.findViewById(R.id.gender_spinner);

        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.male));
        list.add(getString(R.string.female));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item, list);
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

        CustomButton updateProfileButton = (CustomButton) view.findViewById(R.id.update_profile_buttton);
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                attemptupdateUser();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
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

    public void setImageBitmap(Bitmap imageBitmap) {
        profileImage.setImageBitmap(imageBitmap);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void selectImage();

        Bitmap getProfileImage();

        boolean hasProfileImage();

        void editPassword();

        void updateUser();
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


            mListener.updateUser();
        }
    }
}
