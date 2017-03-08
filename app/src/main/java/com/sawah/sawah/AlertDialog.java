package com.sawah.sawah;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afollestad.materialdialogs.DialogAction;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.sawah.sawah.Handlers.SharingHandler;

public class AlertDialog extends AppCompatActivity {

    public static final int ADD_PLACE_DONE = 10;
    public static final int NEW_VERSION_DIALOG = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);

        int dialogID = (int) getIntent().getSerializableExtra("DialogID");


        switch (dialogID) {

            case ADD_PLACE_DONE:

                new MaterialDialog.Builder(this)
                        .title(R.string.done_text)
                        .content(R.string.add_new_place_request_sent)
                        .positiveText(R.string.agree)
                        .autoDismiss(true)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                NavigationHandler.getInstance().startCategoriesListActivity(AlertDialog.this);
                                finish();
                            }
                        })
                        .titleGravity(GravityEnum.CENTER)
                        .contentGravity(GravityEnum.CENTER)
                        .show();

                break;
            case NEW_VERSION_DIALOG:

                new MaterialDialog.Builder(this)
                        .title(R.string.app_version_title)
                        .content(R.string.app_version_message)
                        .positiveText(R.string.agree)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                SharingHandler.getInstance().openWebsite(AlertDialog.this, "market://details?id=com.sawah.sawah");
                            }})
                        .negativeText("cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                finish();
                                }
                            })
                        .autoDismiss(true)
                        .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        })
                        .titleGravity(GravityEnum.CENTER)
                        .contentGravity(GravityEnum.CENTER)
                        .show();

                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 11:
                break;
            case 12:
                break;
            default:
                break;
        }
    }
}
