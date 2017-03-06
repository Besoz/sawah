package com.accorpa.sawah.Handlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.accorpa.sawah.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 25/02/17.
 */
public class Utils {

    private static final float SHAKE_THRESHOLD = 500;
    private final float shakeThreshold = 1.5f;

    private static Utils ourInstance;

    public static Utils getInstance() {
        if(ourInstance == null){
            ourInstance = new Utils();
        }
        return ourInstance;
    }

    private Map<String, Integer> dayMap;


    private Utils() {

        dayMap = new HashMap<>();
        dayMap.put("Saturday", 7);
        dayMap.put("Sunday", 1);
        dayMap.put("Monday", 2);
        dayMap.put("Tuesday",3);
        dayMap.put("Wednesday",4);
        dayMap.put("Thursday", 5);
        dayMap.put("Friday", 6);

    }

    public int mapDay(String day) {
        Log.d("day", day);
        return dayMap.get(day);
    }

    public int getTodayDayNumber() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public void setTypefaceToInputLayout(Context c, TextInputLayout inputLayout)
    {
        String fontFilePath = c.getResources().getString(R.string.default_font);
        final Typeface tf = Typeface.createFromAsset(c.getAssets(), fontFilePath);
        inputLayout.setTypeface(tf);
        inputLayout.getEditText().setTypeface(tf);
    }

    public void changeStatusBarColor(Activity activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window window = activity.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(activity,R.color.colorPrimary));
        }

    }

    public boolean isAccelerationChanged(float xPreviousAccel, float yPreviousAccel,
                                         float zPreviousAccel, float xAccel, float yAccel,
                                         float zAccel, long diffTime) {
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);

        float speed = Math.abs(deltaX + deltaY + deltaZ) / diffTime * 10000;

        System.out.println(diffTime+" "+xPreviousAccel+" "+yPreviousAccel+" "+zPreviousAccel+" "+xAccel+" "+yAccel+" "+zAccel+" "+speed);

        return (speed > SHAKE_THRESHOLD);
    }
}
