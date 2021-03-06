package com.sawah.sawah;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.google.firebase.crash.FirebaseCrash;
import com.orm.SugarApp;
import com.sawah.sawah.Handlers.DataHandler;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ahmedhofny on 3/7/17.
 */

public class SawahApplication extends SugarApp {
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);

    }

    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
//        DataHandler.getInstance(this).updateAppNotification(this,);
//        FirebaseCrash.log("Application start SAWAH");

        boolean isDebuggable = ( 0 != ( getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if(isDebuggable) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectAll() //for all detectable problems
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .detectAll()// for all detectable problems
                    .penaltyLog()
//                    .penaltyDeath()
                    .build());
        }
    }
}
