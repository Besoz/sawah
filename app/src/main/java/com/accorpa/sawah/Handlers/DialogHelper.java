package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.accorpa.sawah.AddNewPlace.AddNewPlaceActivity;
import com.accorpa.sawah.AlertDialog;
import com.accorpa.sawah.Authorization.LoginActivity;
import com.accorpa.sawah.BaseRequestStateListener;
import com.accorpa.sawah.R;
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

    public void showPLaceAddedDialog(final Context context) {

        Intent intent = new Intent(context, AlertDialog.class);
        intent.putExtra("DialogID", AlertDialog.ADD_PLACE_DONE);
        context.startActivity(intent);
    }
}
