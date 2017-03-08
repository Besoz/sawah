package com.sawah.sawah;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.SharedPreferencesController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Created by alaa on 08/03/17.
 */

public class SawahFcmListenerService  extends FirebaseMessagingService {

    String TAG = "SAWAH";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        Log.d("SawahFcmListenerService", "test");

        DataHandler.getInstance(this).updateAppNotification(this);
        int badgeNumber = SharedPreferencesController.getInstance(this).getBadgeNumber();
        Log.d("SawahFcmListenerService", badgeNumber + "");
        SharedPreferencesController.getInstance(this).updateBadgeNumber(badgeNumber + 1);
        Intent notificationIntent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
//                Notification notification = new Notification(R.drawable.common_full_open_on_phone, message, when);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification();
            notification.icon = R.drawable.common_full_open_on_phone;
            try {
                Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                deprecatedMethod.invoke(notification, this, TAG,  remoteMessage.getNotification().getBody(), pendingIntent);
            } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {


            }
        } else {
            // Use new API
            Notification.Builder builder = new Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.common_full_open_on_phone)
                    .setContentTitle(TAG)
                    .setContentText(remoteMessage.getNotification().getBody());
            notification = builder.build();
        }
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);


    }
}
