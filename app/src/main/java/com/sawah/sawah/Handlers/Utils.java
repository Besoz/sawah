package com.sawah.sawah.Handlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sawah.sawah.BitmapImage;
import com.sawah.sawah.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 25/02/17.
 */
public class Utils {

    private static final float SHAKE_THRESHOLD = 800;
    private static final long SHAKE_FREQUENCY = 500;
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
//        Log.d("day", day);
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
                                         float zAccel, long diffTime, long currShake, long lastShake) {
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);

        float speed = Math.abs(deltaX + deltaY + deltaZ) / diffTime * 10000;

        return (speed > SHAKE_THRESHOLD && (currShake - lastShake ) > SHAKE_FREQUENCY );
    }




    public int pxToDp(float px,  DisplayMetrics displayMetrics) {

//        because utils is un aware of context
//        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public int dpToPx(float dp, DisplayMetrics displayMetrics) {

//        because utils is un aware of context
//        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public Bitmap resizeBitmapInDp(Bitmap b, float dpH, float dpW, DisplayMetrics displayMetrics){

        float pxH = dpToPx(dpH, displayMetrics);
        float pxW = dpToPx(dpW, displayMetrics);

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, dpW, pxH), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);

    }

    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public double distance(double lat1, double lon1,  double lat2,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters


        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
