package com.sawah.sawah;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.SharedPreferencesController;


/**
 * Created by alaa on 08/03/17.
 */

public class SawahFcmListenerService  extends FirebaseMessagingService {
    private static final String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";
    String TAG = "SAWAH";
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        Log.e("SAWAH", "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        Log.d("SawahFcmListenerService", "test");

        DataHandler.getInstance(this).updateAppNotification(this);
        int badgeNumber = SharedPreferencesController.getInstance(this).getBadgeNumber();
        Log.d("SawahFcmListenerService", badgeNumber + "");
        SharedPreferencesController.getInstance(this).updateBadgeNumber(badgeNumber + 1);

    }
}
