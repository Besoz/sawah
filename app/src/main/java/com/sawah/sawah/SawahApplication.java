package com.sawah.sawah;

import com.orm.SugarApp;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by ahmedhofny on 3/7/17.
 */

public class SawahApplication extends SugarApp {
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
