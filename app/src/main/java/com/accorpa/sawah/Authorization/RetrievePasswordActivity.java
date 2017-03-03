package com.accorpa.sawah.Authorization;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.view.View;

import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.ServiceResponse;
import com.accorpa.sawah.custom_views.CustomAutoCompleteTextView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrievePasswordActivity extends AppCompatActivity {

    @BindView(R.id.progress_view)  View mProgressView;
    @BindView(R.id.form)  View mFormView;

    @BindView(R.id.email) CustomAutoCompleteTextView mEmailView;
    @OnClick(R.id.submit) void submit() {
        attempt();
    }


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
            mEmailView.setError(getString(R.string.error_field_required));
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

                            new MaterialDialog.Builder(RetrievePasswordActivity.this)
                                    .title(R.string.done_text)
                                    .content(response.getMessage())
                                    .positiveText(R.string.agree)
                                    .autoDismiss(true)
                                    .titleGravity(GravityEnum.CENTER)
                                    .contentGravity(GravityEnum.CENTER)
                                    .show();
                        }

                        @Override
                        public void successResponse(ServiceResponse message) {
                            showProgress(false);

                            new MaterialDialog.Builder(RetrievePasswordActivity.this)
                                    .title(R.string.done_text)
                                    .content(R.string.password_sent_to_mail_successfuly)
                                    .positiveText(R.string.agree)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog,
                                                            @NonNull DialogAction which) {
                                            NavigationHandler.getInstance()
                                                    .startLoginActivity(RetrievePasswordActivity
                                                            .this);
                                        }
                                    })
                                    .autoDismiss(true)
                                    .titleGravity(GravityEnum.CENTER)
                                    .contentGravity(GravityEnum.CENTER)
                                    .show();
                        }
                    });
        }
    }

    private void showProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}
