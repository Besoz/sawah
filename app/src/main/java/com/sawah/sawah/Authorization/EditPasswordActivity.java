package com.sawah.sawah.Authorization;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TableRow;

import com.sawah.sawah.BaseActivity;
import com.sawah.sawah.BaseResponseListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.R;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.custom_views.CustomEditText;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditPasswordActivity extends BaseActivity {

    private CircleImageView profileImage;

    CustomEditText currentPassword;
    CustomEditText newPassword;
    CustomEditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        currentPassword = (CustomEditText)  findViewById(R.id.current_password_edit_text);
        newPassword = (CustomEditText) findViewById(R.id.edit_password_edit_text);
        confirmPassword = (CustomEditText) findViewById(R.id.confirm_password_edit_text);

        removeNavigationDrawer();

        TableRow row1 = (TableRow) findViewById(R.id.r1);
        TableRow row3 = (TableRow) findViewById(R.id.r3);
        TableRow row4 = (TableRow) findViewById(R.id.r4);

        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(currentPassword, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        row3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPassword.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(newPassword, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        row4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                confirmPassword.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(confirmPassword, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        CustomButton updatePasswordButton = (CustomButton) findViewById(R.id.edit_password_button);
        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptupdatePassword();
            }
        });
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
            confirmPassword.setError(getString(R.string.enter_password));
            focusView = newPassword;
            cancel = true;
        } else if (!TextUtils.equals(newPasswordStr, confirmPasswordStr)) {
            confirmPassword.setError(getString(R.string.error_not_equal_password));
            focusView = confirmPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPasswordStr)) {
            newPassword.setError(getString(R.string.enter_password));
            focusView = newPassword;
            cancel = true;
        } else if (!AuthorizationController.isPasswordValid(newPasswordStr)) {
            newPassword.setError(getString(R.string.error_invalid_password));
            focusView = newPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(currentPasswordStr)) {
            currentPassword.setError(getString(R.string.enter_password));
            focusView = currentPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            updatePassword( currentPasswordStr, newPasswordStr, confirmPasswordStr);
        }
    }

    public void updatePassword(String currentPasswordStr,
                               String newPasswordStr, String confirmPasswordStr) {

        BaseResponseListener mResponseListner = new BaseResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                super.onResponse(response);
                if(isStatusSuccess()){
                    MaterialDialog m = DialogHelper.getInstance()
                            .showSuccess(EditPasswordActivity.this,
                                    getString(R.string.password_update_success));
                    m.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            updatePasswordSuccess();
                        }
                    });
                }else{
                    currentPassword.requestFocus();
                    currentPassword.setError(getString(R.string.error_incorrect_password));
                    Log.d("Update user", "fail");
                }
                showProgress(false);
            }
        };
        showProgress(true);
        DataHandler.getInstance(this).requestUpdateUserPassword(mResponseListner, currentPasswordStr,
                newPasswordStr, confirmPasswordStr, this);
    }

    public void updatePasswordSuccess() {
        finish();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_password_acitvity;
    }

    protected String getToolbarTitle() {
        return getString(R.string.change_password);
    }
    protected int getActionBarMenuLayout() {
        return R.menu.back_tool_bar;
    }
}
