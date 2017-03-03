package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.util.Log;

import com.accorpa.sawah.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 25/02/17.
 */
public class Utils {
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
}
