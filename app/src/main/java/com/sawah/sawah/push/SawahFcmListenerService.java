package com.sawah.sawah.push;


import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.SharedPreferencesController;
import com.sawah.sawah.LauncherActivity;
import com.sawah.sawah.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


/**
 * Created by alaa on 08/03/17.
 */

public class SawahFcmListenerService  extends FirebaseMessagingService {

    String TAG = "SAWAH";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){

        Log.d("SawahFcmListenerService", "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;


        if (isAppIsInBackground(getApplicationContext())) {
            Log.d("SawahFcmListenerService", "isAppIsInBackground");
            Intent fcmBroadcaster = new Intent(this, FcmBroadcastReceiver.class);

            PendingIntent recurringDownload = PendingIntent.getBroadcast(this,
                    0, fcmBroadcaster, PendingIntent.FLAG_ONE_SHOT);

            AlarmManager alarms = (AlarmManager) this.getSystemService(
                    Context.ALARM_SERVICE);
            alarms.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, recurringDownload);


        }
        else
        {
            DataHandler.getInstance(this).clearBadgeCount(this);
        }
//        int badgeNumber = SharedPreferencesController.getInstance(this).getBadgeNumber();
//        Log.d("SawahFcmListenerService", badgeNumber + "");
//        SharedPreferencesController.getInstance(this).updateBadgeNumber(this, badgeNumber + 1);
//        DataHandler.getInstance(this).updateAppNotification(this, SharedPreferencesController.getInstance(this).getBadgeNumber());



        Intent notificationIntent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = null;
//                Notification notification = new Notification(R.drawable.common_full_open_on_phone, message, when);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification();
            notification.icon = R.drawable.bell;
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
                    .setSmallIcon(R.drawable.bell)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody());
            notification = builder.build();
        }
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);


    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
            Log.d("SawahFcmListenerService", "1 - isAppIsInBackground:" + isInBackground);
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
            Log.d("SawahFcmListenerService", "2 - isAppIsInBackground:" + isInBackground);
        }

        return isInBackground;
    }

}
