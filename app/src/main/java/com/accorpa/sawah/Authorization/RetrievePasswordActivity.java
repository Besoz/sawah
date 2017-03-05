package com.accorpa.sawah.Authorization;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.view.View;

import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.Handlers.DialogHelper;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.custom_views.CustomAutoCompleteTextView;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomEditText;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrievePasswordActivity extends AppCompatActivity {

    View mProgressView;
    View mFormView;

    CustomAutoCompleteTextView mEmailView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        AppCompatImageButton backButton = (AppCompatImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Utils.getInstance().setTypefaceToInputLayout(this, (TextInputLayout) findViewById(R.id.mail));

        CustomButton submit = (CustomButton) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt();
            }
        });

        mProgressView = findViewById(R.id.progress_view);
        mFormView = findViewById(R.id.form);
        mEmailView = (CustomAutoCompleteTextView) findViewById(R.id.email);
    }


    private void attempt() {

        // Reset errors.
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (!AuthorizationController.isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
//            todo call authorization manger .. but after restructuring it
            ServiceHandler.getInstance(this).requestRetrievePassword(email,
                    new BaseRequestStateListener() {
                        @Override
                        public void failResponse(ServiceResponse response) {

                            showProgress(false);
                            DialogHelper.getInstance().showError(RetrievePasswordActivity.this,
                                    response.getMessage());
                        }

                        @Override
                        public void successResponse(ServiceResponse message) {
                            showProgress(false);
                            MaterialDialog m = DialogHelper.getInstance().showSuccess
                                    (RetrievePasswordActivity.this,
                                    getString(R.string.password_sent_to_mail_successfuly));
                            m.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    NavigationHandler.getInstance()
                                            .startLoginActivity(RetrievePasswordActivity
                                                    .this);
                                }
                            });
                        }
                    });
        }
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
