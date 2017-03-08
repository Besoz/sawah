package com.sawah.sawah.Handlers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.sawah.sawah.AddNewPlace.AddNewPlaceActivity;
import com.sawah.sawah.AlertDialog;
import com.sawah.sawah.Authorization.LoginActivity;
import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.LauncherActivity;
import com.sawah.sawah.R;
import com.sawah.sawah.place.PlaceDetailsActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * Created by root on 05/03/17.
 */
public class DialogHelper {
    private static DialogHelper ourInstance = new DialogHelper();

    public static DialogHelper getInstance() {
        return ourInstance;
    }

    private DialogHelper() {
    }

    public void showSelectImageDialog(Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.alert_title)
                .content(R.string.choose_place_image_message)
                .positiveText(R.string.agree)
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    public MaterialDialog showSuccess(Context context, String message)
    {
        return new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .title(R.string.done_text).titleGravity(GravityEnum.CENTER)
                .content(message).contentGravity(GravityEnum.CENTER)
                .autoDismiss(true)
                .positiveText(R.string.agree).show();
    }

    public MaterialDialog showError(Context context, String message)
    {
        return new MaterialDialog.Builder(context)
                .title(R.string.error).titleGravity(GravityEnum.CENTER)
                .theme(Theme.LIGHT)
                .content(message)
                .contentGravity(GravityEnum.CENTER).autoDismiss(true)
                .positiveText(R.string.agree).show();
    }

    public MaterialDialog showAlert(Context context, String message)
    {
        return new MaterialDialog.Builder(context)
                .title(R.string.alert_title).titleGravity(GravityEnum.CENTER)
                .theme(Theme.LIGHT)
                .content(message)
                .contentGravity(GravityEnum.CENTER).autoDismiss(true)
                .positiveText(R.string.agree).show();
    }

    public void showPLaceAddedDialog(final Context context) {

        Intent intent = new Intent(context, AlertDialog.class);
        intent.putExtra("DialogID", AlertDialog.ADD_PLACE_DONE);
        context.startActivity(intent);
    }

//    public MaterialDialog showAlert(Context context, String s) {
//
//        return new MaterialDialog.Builder(context)
//                .title(R.string.error).titleGravity(GravityEnum.CENTER)
//                .theme(Theme.LIGHT)
//                .content(s)
//                .contentGravity(GravityEnum.CENTER).autoDismiss(true)
//                .positiveText(R.string.agree).show();
//    }

    public void showRequestOpenGPS(final Context context){

        new MaterialDialog.Builder(context)
                .title(R.string.enable_gps_title)
                .content(R.string.enable_gps_message)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }}).negativeText("cancel")
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();


    }

    public void showNewVersionDialog(final LauncherActivity launcherActivity, String message) {
        new MaterialDialog.Builder(launcherActivity)
                .title(R.string.app_version_title)
                .content(R.string.app_version_message)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        SharingHandler.getInstance().openWebsite(launcherActivity,
                                "market://details?id=com.sawah.sawah");
                    }
                })
                .negativeText(R.string.cancel_text)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        launcherActivity.continueLaunch();
                    }
                })
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

}
