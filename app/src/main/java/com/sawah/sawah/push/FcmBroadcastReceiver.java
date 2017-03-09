package com.sawah.sawah.push;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.SharedPreferencesController;

/**
 * Created by alaa on 09/03/17.
 */

public class FcmBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("FcmBroadcastReceiver", "tesssssst");
//
//        SharedPreferences prefs = context.getSharedPreferences(ComplexSharedPreferences.namePreferences,
//                Context.MODE_PRIVATE);
//        String badgeCount = prefs.getString(SharedPreferencesController.BADGE_NUMBER_KEY, "155");

        int badgeCount = SharedPreferencesController.getInstance(context).getBadgeNumber() + 1;
        SharedPreferencesController.getInstance(context).updateBadgeNumber(badgeCount);

        Log.d("FcmBroadcastReceiver", badgeCount + "");
        DataHandler.getInstance(context).updateAppNotification(context, badgeCount);
    }
}