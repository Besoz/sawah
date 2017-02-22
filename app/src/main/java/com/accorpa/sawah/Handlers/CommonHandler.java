package com.accorpa.sawah.Handlers;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by Accorpa on 12/21/2015.
 */
public class CommonHandler {

//    private static CommonHandler commonHandler;
//    //    2016-04-28T09:22:45+02:00
//    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ssZ";//"yyyy-MM-dd HH:mm:ss";
//
//    public static CommonHandler getInstance() {
//        if (commonHandler == null) {
//            commonHandler = new CommonHandler();
//        }
//
//        return commonHandler;
//    }
//
//    public Date getDateFromLong(Long dateTimeStamp) {
////        return new Date(Long.parseLong(dateTimeStamp));// * 1000);
//        return new Date(dateTimeStamp);
//    }
//
//    public String getPartOfString(String text, int length) {
//        if (length < 0)
//            length = 40;
//        if (text.length() > length) {
//            return text.substring(0, length);
//        }
//        return text;
//    }
//
//    public String getDeviceID(Context context) {
//        final TelephonyManager tm = (TelephonyManager) context
//                .getSystemService(Context.TELEPHONY_SERVICE);
//
//        final String tmDevice, tmSerial, tmPhone, androidId;
//        tmDevice = "" + tm.getDeviceId();
//        tmSerial = "";// + tm.getSimSerialNumber();
//        androidId = ""
//                + android.provider.Settings.Secure.getString(
//                context.getContentResolver(),
//                android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(),
//                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
//        String deviceId = deviceUuid.toString();
//
//        return deviceId;
//    }
//
//    public void getLocationUpdates(final Activity activity) {
//        final LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
//        Criteria criteria = new Criteria();
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (provider != null) {
//            Location location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
////                DataHandler.getInstance().setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
////            updateLocation(location, activity);
//            }
//        }
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                updateLocation(location, activity);
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
////                //Logger.getInstance().appendLog("onStatusChanged ");
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
////                //Logger.getInstance().appendLog("onProviderEnabled ");
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
////                //Logger.getInstance().appendLog("onProviderDisabled ");
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 1, locationListener);
//    }
//
//    public void updateLocation(Location location, Activity activity) {
//        LatLng lastLatLng = DataHandler.getInstance().getLocation();
//        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
////        //Logger.getInstance().appendLog("onLocationChanged - newLatLng:" + newLatLng + "/lastLatLng:" + lastLatLng);
//
////        if (lastLatLng == null || !lastLatLng.equals(newLatLng))
//        {
//            DataHandler.getInstance().setLocation(newLatLng);
//            if (activity != null && activity instanceof LocationUpdaterInterface) {
//                ((LocationUpdaterInterface) activity).handleViewLessThan70();
//                ((LocationUpdaterInterface) activity).updateDistanceView();
//            }
//
//            // send location to server
//            ArrayList param = ServiceUrlsHandler.getInstance().getTrackingParams(newLatLng, DataHandler.getInstance().getCurrentUserId());
//            String url = ServiceUrlsHandler.getInstance().createTrackingUrl();
//            ServiceTask serviceTask = ServiceHandler.getInstance().executeTask(url, "", ServiceHandler.getInstance().getPostMethod(),
//                    null, param, null, false);
//            //Logger.getInstance().appendLog("onLocationChanged sendUpdates - newLatLng:" + newLatLng + "/lastLatLng:" + lastLatLng);
//        }
////                locationManager.removeUpdates(this);
//    }
//
//    public boolean isDouble(String str) {
//        try {
//            Double.parseDouble(str);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    public boolean isInteger(String str) {
//        try {
//            Integer.parseInt(str);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    public boolean isLong(String str) {
//        try {
//            Long.parseLong(str);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//
//    public boolean isEmailValid(String email) {
//        return email.contains("@");
//    }
//
//    public boolean isNotNull(String objValue) {
//        if (objValue != null && !objValue.equalsIgnoreCase("") && !objValue.equalsIgnoreCase("null")) {
//            return true;
//        }
//        return false;
//    }
//
////    public void setCurrentLocation(final Activity activity, final GoogleMap mMap, final LatLngBounds.Builder builder, final boolean isUpdateLocation) {
////        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
////        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
////            int requestCode = 10;
////            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, requestCode);
////            dialog.show();
////
////        } else { // Google Play Services are available
////
////            int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
////
////            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
////                    ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                // TODO: Consider calling
////                //    ActivityCompat#requestPermissions
////                // here to request the missing permissions, and then overriding
////                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                //                                          int[] grantResults)
////                // to handle the case where the user grants the permission. See the documentation
////                // for ActivityCompat#requestPermissions for more details.
////                ActivityCompat.requestPermissions(activity,
////                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
////                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
////                return;
////            }
////            mMap.setMyLocationEnabled(true);
////
////            final LocationManager locationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);
////            Criteria criteria = new Criteria();
////            String provider = locationManager.getBestProvider(criteria, true);
////            Location location = null;
////            if (provider != null)
////                location = locationManager.getLastKnownLocation(provider);
////            if (location != null) {
////                DataHandler.getInstance().setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
////            }
////            LocationListener locationListener = new LocationListener() {
////                @Override
////                public void onLocationChanged(Location location) {
//////                    LatLng lastLatLng = DataHandler.getInstance().getLocation();
////                    LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
//////                    DataHandler.getInstance().setLocation(newLatLng);
////
////                    builder.include(DataHandler.getInstance().getLocation());
//////                    ViewHandler.getInstance().zoomToLatLngBounds(builder, mMap);
//////                    ViewHandler.getInstance().zoomCameraToCurrentLocation(latLng, mMap);
////                    if (isUpdateLocation) {
////                        if (!lastLatLng.equals(newLatLng)) {
////                            double distance = getDistance(lastLatLng, newLatLng);
////                            if (distance <= DataHandler.getInstance().getCanNotReachDistance()) {
////                                // show canot reach view
//////                                if (activity != null && ((HeadingToCarActivity) activity).headingToCarFragment != null) {
//////                                    ((HeadingToCarActivity) activity).headingToCarFragment.handleViewLessThan70();
//////                                }
////
////                            }
////                            // send location to server
//////                            ArrayList param = ServiceUrlsHandler.getInstance().getTrackingParams(newLatLng, DataHandler.getInstance().getCurrentUserId());
//////                            String url = ServiceUrlsHandler.getInstance().createTrackingUrl();
//////                            ServiceTask serviceTask = ServiceHandler.getInstance().executeTask(url, "", ServiceHandler.getInstance().getPostMethod(),
//////                                    null, param, null, false);
////                        }
////                    } else {
////                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
////                                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                            // TODO: Consider calling
////                            //    ActivityCompat#requestPermissions
////                            // here to request the missing permissions, and then overriding
////                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////                            //                                          int[] grantResults)
////                            // to handle the case where the user grants the permission. See the documentation
////                            // for ActivityCompat#requestPermissions for more details.
////                            return;
////                        }
////                        locationManager.removeUpdates(this);
////                    }
////                }
////                @Override
////                public void onStatusChanged(String provider, int status, Bundle extras) { }
////
////                @Override
////                public void onProviderEnabled(String provider) {}
////
////                @Override
////                public void onProviderDisabled(String provider) {}
////            };
////            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, locationListener);
////        }
//    }
//
//    public double getDistance(LatLng location, LatLng latLng) {
//        double distanceDouble = -1;
//        if (location != null && latLng != null)
//        {
//            Location locationB = new Location("point B");
//            locationB.setLatitude(location.latitude);
//            locationB.setLongitude(location.longitude);
//
//            Location locationA = new Location("point A");
//            locationA.setLatitude(latLng.latitude);
//            locationA.setLongitude(latLng.longitude);
//
//            float distance = locationA.distanceTo(locationB) ;
////            distanceDouble = distance;
//            distanceDouble = Math.round(distance * 1000)/(double)1000;
//
//        }
//        //Logger.getInstance().appendLog("getDistance" + distanceDouble);
//        return  distanceDouble;
////        double theta = location.longitude - latLng.longitude;
////        double dist = Math.sin(deg2rad(location.latitude)) * Math.sin(deg2rad(latLng.latitude)) +
////                Math.cos(deg2rad(location.latitude)) * Math.cos(deg2rad(latLng.latitude)) * Math.cos(deg2rad(theta));
////        dist = Math.acos(dist);
////        dist = rad2deg(dist);
////        dist = dist * 60 * 1.1515;
////        return (int)Math.round(dist * 100)/(double)100;
//    }
//
//    private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }
//
//    public byte[] getBase64ImageFromBitmap(Bitmap bitmap) {
//
//        byte[] imageBytes = new byte[0];
//        ByteArrayOutputStream os;
//        try {
//            os = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//            imageBytes = os.toByteArray();
//            os.flush();
//            os.close();
//        } catch (Exception e) {
////            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
//        }
//
//        return imageBytes;
//    }
//
//    public byte[] getBytesFromFile(File file) throws IOException
//    {
//        InputStream is = new FileInputStream(file);
//        long length = file.length();
//        if (length > Integer.MAX_VALUE)
//        {
//
//        }
//        byte[] bytes = new byte[(int)length];
//        int offset = 0;
//        int numRead = 0;
//        while (offset < bytes.length
//                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
//            offset += numRead;
//        }
//        if (offset < bytes.length) {
//            throw new IOException("Could not completely read file "+file.getName());
//        }
//        is.close();
//        return bytes;
//    }
//
////    public int findCar(String carId, List<Car> cars){
////        // iterate all the Book elements in the collection array
////        if (!carId.equalsIgnoreCase(""))
////        {
////            for(int i = 0; i < cars.size(); i++){
////                // check if the current book isbn matches the one provided argument
////                if (cars.get(i).getId().equalsIgnoreCase(carId))
////                    return i;
////            }
////        }
////
////        return -1;
////    }
//
//    public void deleteSavedDB(Activity activity) {
//        try
//        {
//            //delete saved data
////            Parking.deleteAllParking(activity);
////            DataHandler.getInstance().removeCars(activity);
//
////            ParkingStatus.deleteAllParkingStatus();
//        }
//        catch (SQLiteException e)
//        {
//
//        }
//
////        ComplexSharedPreferences.getComplexPreferences(activity).deleteObject(StatisticsActivity.LastStatisticsDataKey);
////        ComplexSharedPreferences.getComplexPreferences(activity).deleteObject(User.currentUserDataKey);
////        ComplexSharedPreferences.getComplexPreferences(activity).deleteObject(Car.CarActivatedId);
////        ComplexSharedPreferences.getComplexPreferences(activity).deleteObject(User.currentUserSessionId);
//    }
//
////    public String getStringColumnSeperatedByComma(ArrayList array, String object){
////        String objects = "";
////        for(int i=0; i< array.size(); i++){
////            try {
////                Class c = Parking.class;
////                Field f = c.getDeclaredField(object);
////                Parking parking = (Parking) array.get(i);
////                String parkingId = parking.getId();
////                if (i>0)
////                    objects += ",";
////                objects += parkingId;
////            } catch (NoSuchFieldException e) {
////                e.printStackTrace();
////            }
////        }
////        return objects;
////    }
//
//    public String getDateInFormat(Long date) {
//        Date testDate;
//        try {
//            testDate = getDateFromLong(date);
//            SimpleDateFormat formatter = new SimpleDateFormat("d/M/y h:mm a");
//            return formatter.format(testDate);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return "";
//    }
//    public long getCurrentTime()
//    {
//        long timeNow = getTimeNow();
////        TimeZone tz = TimeZone.getTimeZone("UTC");
////        Calendar c = Calendar.getInstance(tz);
////        timeNow = c.getTimeInMillis();
//        return timeNow;
//    }
//
//    public boolean deleteSDCardFile(String fileName)
//    {
//        File file = new File(fileName);
//        return file.delete();
//    }
//
//    public boolean renameSDCardFile(String newFileName, String oldFileName)
//    {
//        File file = new File(oldFileName);
//        File file2 = new File(newFileName);
//        return file.renameTo(file2);
//    }
//
//    public int dp(float value) {
//        return (int) Math.ceil(1 * value);
//    }
//
////    public MediaPlayer startPlay(final Activity activity, final CustomButton recordingIcon, String fileName) {
////        MediaPlayer mediaPlayer = null;
////        try {
////            recordingIcon.setText(activity.getResources().getString(R.string.play_sound_attachment_icon));
////            recordingIcon.setTextColor(activity.getResources().getColor(R.color.dollar_bill));
//////            Log.i("onCreate mediaPlayer", fileName);
////            mediaPlayer = new MediaPlayer();
////            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////            mediaPlayer.setDataSource(fileName);
////            mediaPlayer.prepare();
////            mediaPlayer.start();
////            final MediaPlayer finalMediaPlayer = mediaPlayer;
////            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
////                public void onCompletion(MediaPlayer mp) {
////                    stopPlay(activity, finalMediaPlayer, recordingIcon);
////                }
////            });
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////        return mediaPlayer;
////    }
//
////    public void stopPlay(Activity activity, MediaPlayer mediaPlayer, CustomButton recordingIcon) {
////        if (mediaPlayer != null) {
////            recordingIcon.setText(activity.getResources().getString(R.string.stop_sound_attachment_icon));
////            recordingIcon.setTextColor(activity.getResources().getColor(R.color.manatee1));
////            mediaPlayer.stop();
////        }
////    }
//
////    public void handleImageViewClickListener(final ImageView imageView, final Fragment fragment) {
////        imageView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                final ImageViewerFragment imageViewFragment = new ImageViewerFragment();
////                Drawable drawable =  imageView.getDrawable();
////                if (drawable != null)
////                {
////                    imageViewFragment.setBitmap(((BitmapDrawable) drawable).getBitmap());
//////                    imageViewFragment[0].setBitmap(bitmap);
////                    FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction();
////                    ft.hide(fragment);
////                    ft.addToBackStack(null);
////                    ft.add(R.id.fragment, imageViewFragment);
////                    ft.commit();
////                }
////
////            }
////        });
////    }
//
//    public void setLocle(String newLng, Context actionBarActivity) {
////        Log.e("setLocle", lang);
//        Locale locale = new Locale(newLng);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        actionBarActivity.getApplicationContext().getResources().updateConfiguration(config, null);
//    }
//
//    public String collectPayfortErrors(JSONObject object)
//    {
//        String message = "";
//        JSONObject error = null;
//        try {
//            error = object.getJSONObject("result");
//            message = error.getString("description") + '\n';
//
//            JSONArray parameterErrors = error.getJSONArray("parameterErrors");
//            for (int i = 0; i < parameterErrors.length(); i++) {
//                JSONObject errorsObject = (JSONObject) parameterErrors.get(i);
//                message += errorsObject.getString("name");
//                message += ": " + errorsObject.getString("message") + "\n";
//            }
////            error = object.getJSONObject("error");
////            message = error.getString("message") + '\n';
////            JSONObject detailsOfErrors = error.getJSONObject("extras");
////            Iterator<String> keys= detailsOfErrors.keys();
////            while (keys.hasNext())
////            {
////                String keyValue = (String)keys.next();
////                JSONArray valueString = detailsOfErrors.getJSONArray(keyValue);
////                message += keyValue + ": ";
////                for (int i = 0; i < valueString.length(); i++) {
////                    message += valueString.getString(i) +" , " ;
////                }
////                message +=  '\n';
////
////            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        return message;
//    }
//
//    public void restartActivity(Activity activity) {
//
//        Intent intent = activity.getIntent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        activity.finish();
//        activity.startActivity(intent);
//    }
//
////    public void changeLanguage(Activity activity, String newLng) {
//////        LanguagesHandler.getInstance().setCurrentLanguage(lang);
//////        handleSavedData();
////        CommonHandler.getInstance().setLocle(newLng, activity);
////        ComplexSharedPreferences complexPrefenreces = ComplexSharedPreferences.getComplexPreferences(activity);
////        complexPrefenreces.putObject(DataHandler.getInstance().getSavedLanguageString(), newLng);
////        complexPrefenreces.commit();
////    }
//
//    public String getDeviceName() {
//        String manufacturer = Build.MANUFACTURER;
//        String model = Build.MODEL;
//        if (model.startsWith(manufacturer)) {
//            return model;
//        }
//        return manufacturer + " " + model;
//    }
////
////    public void setupUI(final Activity activity, View view) {
////
////        //Set up touch listener for non-text box views to hide keyboard.
////        int viewId = view.getId();
////        boolean test = (viewId == R.id.chat_audio_send_button);
////        if((!(view instanceof EditText)) && !(viewId == R.id.chat_audio_send_button)) {
////
////            view.setOnTouchListener(new View.OnTouchListener() {
////
////                public boolean onTouch(View v, MotionEvent event) {
////                    hideSoftKeyboard(activity);
////                    return false;
////                }
////
////            });
////        }
////
////        //If a layout container, iterate over children and seed recursion.
////        if (view instanceof ViewGroup) {
////
////            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
////
////                View innerView = ((ViewGroup) view).getChildAt(i);
////
////                setupUI(activity, innerView);
////            }
////        }
////    }
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        if (activity.getCurrentFocus() != null)
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//    }
//
//    public String getStringResourceByName(Activity activity, String aString) {
//        String packageName = activity.getPackageName();
//        int resId = activity.getResources().getIdentifier(aString, "string", packageName);
//        return activity.getString(resId);
//    }
//    public void callPhoneNumber(Activity activity, String phoneNumber, boolean isBuyer, Bundle logParams) {
//        int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
//
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.CALL_PHONE},
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//            return;
//        }
//
//        Intent in=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//        try{
////            if (isBuyer)
////                //Logger.getInstance().logEvent(activity, "Buyer Call", logParams, 0);
////            else
////                //Logger.getInstance().logEvent(activity, "Seller Call", logParams, 0);
//            activity.startActivity(in);
//        }catch (android.content.ActivityNotFoundException ex){
//            Toast.makeText(activity.getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public String readTxtFile(Activity activity, String fileName) {
//        StringBuilder out = null;
//        AssetManager assetsManager = activity.getAssets();
//        try {
//            InputStream input = assetsManager.open(fileName);
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//            out = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                out.append(line).append("\n");
//            }
//            reader.close();
//            return out.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//    public long getTimeNow()
//    {
//        long timeNow = new Date().getTime();
////        if (DataHandler.getInstance().getNtpDateTime() != null && DataHandler.getInstance().ntpClientTimeNow != null)
////        {
////            timeNow = timeNow +
////                    (DataHandler.getInstance().getNtpDateTime().getTime() -
////                            DataHandler.getInstance().ntpClientTimeNow.getTime());
////        }
//
////        currentTimeNow + (ntpTime - ntpClientTImeNow)
//        return timeNow;
//
//    }
////    public long getDiffernceTime(Parking parking) {
//////        long timeNow = parking.getTimeNowLong();
////        long timeNow = getTimeNow();
////        return timeNow - parking.getStartTimeLong();
////    }
//    public Date getDateFromString(String dateStr)
//    {
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
//        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
////        String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";
//        Date date = null;
//        try {
//
//            date = formatter.parse(dateStr);
//            //Logger.getInstance().appendLog(String.valueOf(date));
//            //Logger.getInstance().appendLog(formatter.format(date));
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return date;
//    }
//
//    public void delete(String path) {
//        File f = new File(path) ;
//        if (f.isDirectory()) {
//            File [] files = f.listFiles();
////            for (File c : f.listFiles())
//            if (files != null && files.length > 0)
//            {
//                for (int i = 0; i < files.length; i++) {
//
//                    File c = files[i];
//                    if (!c.delete()) {
//                        new FileNotFoundException("Failed to delete file: " + f);
//                    }
//
//                }
//            }
//
//
//        } else if (f.getAbsolutePath().endsWith("FIR")) {
//            if (!f.delete()) {
//                new FileNotFoundException("Failed to delete file: " + f);
//            }
//        }
//    }
//
//    public void createDir() {
////        File wallpaperDirectory = new File(DataHandler.getInstance().getAbsolutePath());
////// have the object build the directory structure, if needed.
////        wallpaperDirectory.mkdirs();
//    }
//
//    public double getDoubleValueFromBundle(String str, Bundle extras) {
//        double creditDouble = 0;
//        if (extras.containsKey(str))
//        {
//            Object creditObj = extras.get(str);
//            try {
//                if (creditObj instanceof Double)
//                {
//                    creditDouble = (double) creditObj;
//                }
//                else if(creditObj instanceof String && CommonHandler.getInstance().isDouble((String) creditObj))
//                {
//                    creditDouble = Double.parseDouble((String) creditObj);
//                }
//            }
//            catch (ClassCastException e)
//            {
//                //Logger.getInstance().appendLog(String.valueOf(e));
////                Crashlytics.logException(e);
//            }
//        }
//
//        return creditDouble;
//    }
}
