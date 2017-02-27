package com.accorpa.sawah.Authorization;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accorpa.sawah.BaseResponseListener;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomEditText;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditPasswordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private CircleImageView profileImage;

    CustomEditText currentPassword;
    CustomEditText newPassword;
    CustomEditText confirmPassword;



    public EditPasswordFragment() {
        // Required empty public constructor
    }

    public static EditPasswordFragment newInstance() {
        EditPasswordFragment fragment = new EditPasswordFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        currentPassword = (CustomEditText) view.findViewById(R.id.current_password_edit_text);
        newPassword = (CustomEditText) view.findViewById(R.id.edit_password_edit_text);
        confirmPassword = (CustomEditText) view.findViewById(R.id.confirm_password_edit_text);

        CustomButton updatePasswordButton = (CustomButton) view.findViewById(R.id.edit_password_button);
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptupdatePassword();
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

    public interface OnFragmentInteractionListener {

        void updatePassword(BaseResponseListener response, String confirmPasswordStr, String newPasswordStr, String confirmPasswordStr1);

        void updatePasswordSuccess();
    }

    private void attemptupdatePassword() {

        // Reset errors.
        currentPassword.setError(null);
        newPassword.setError(null);
        confirmPassword.setError(null);

        // Store values at the time of the login attempt.
        final String currentPasswordStr = currentPassword.getText().toString();
        String newPasswordStr = newPassword.getText().toString();
        String confirmPasswordStr = confirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;



        if (TextUtils.isEmpty(confirmPasswordStr)) {
            confirmPassword.setError(getString(R.string.error_field_required));
            focusView = newPassword;
            cancel = true;
        } else if (!TextUtils.equals(newPasswordStr, confirmPasswordStr)) {
            confirmPassword.setError(getString(R.string.error_not_equal_password));
            focusView = confirmPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPasswordStr)) {
            newPassword.setError(getString(R.string.error_field_required));
            focusView = newPassword;
            cancel = true;
        } else if (!AuthorizationManger.isPasswordValid(newPasswordStr)) {
            newPassword.setError(getString(R.string.error_invalid_password));
            focusView = newPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(currentPasswordStr)) {
            currentPassword.setError(getString(R.string.error_field_required));
            focusView = currentPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            BaseResponseListener mResponseListner = new BaseResponseListener() {
                @Override
                public void onResponse(JSONObject response) {
                    super.onResponse(response);
                    if(isStatusSuccess()){

                    }else{
                        currentPassword.requestFocus();
                        currentPassword.setError(getString(R.string.error_incorrect_password));
                        Log.d("Update user", "fail");
                    }
                }
            };

            mListener.updatePassword(mResponseListner, currentPasswordStr, newPasswordStr, confirmPasswordStr);
        }
    }
}
