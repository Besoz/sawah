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

    public void showPLaceAddedDialog(final Context context) {

        Intent intent = new Intent(context, AlertDialog.class);
        intent.putExtra("DialogID", AlertDialog.ADD_PLACE_DONE);
        context.startActivity(intent);
    }
}
