package com.accorpa.sawah.Handlers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.accorpa.sawah.Authorization.EditProfileActivity;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomTextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 15/02/17.
 */
public class ViewHandler {

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private float mapZoom = 75;


    private static ViewHandler ourInstance = new ViewHandler();

    public static ViewHandler getInstance() {
        return ourInstance;
    }

    private ViewHandler() {
    }

    public Bitmap getBitmapFromURI(Context context, Uri uri) throws IOException {

        return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        // Log.d(TAG, String.valueOf(bitmap));

    }



    public void setWebViewText(WebView webView , String text)
    {
        if (android.os.Build.VERSION.SDK_INT < 16) {
            webView.setBackgroundColor(0x00000000);
        } else {
            webView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        webView.loadDataWithBaseURL("file:///android_asset/", text , "text/html", "UTF-8", null);
    }

    public void setActivityOrientation(Activity activity)
    {
        //        actionBarActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void restoreActionBar(final ActionBarActivity activity, String title, boolean showList) {

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.id.toolbar);
//        CustomTextView actionBarList = (CustomTextView) activity.findViewById(R.id.action_bar_list_btn);
//        actionBarList.setText(R.String.align_justify_icon);
//        if (showList)
//            actionBarList.setVisibility(View.GONE);
//
//
//        actionBarList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DrawerLayout mDrawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
//                View mFragmentContainerView = activity.findViewById(R.id.navigation_drawer);
//                if (mDrawerLayout != null) {
//                    if (mDrawerLayout.isDrawerOpen(mFragmentContainerView)) {
//                        mDrawerLayout.closeDrawer(mFragmentContainerView);
//                    } else {
//                        mDrawerLayout.openDrawer(mFragmentContainerView);
//                    }
//                }
//            }
//        });

        try {
            ActivityInfo activityInfo = activity.getPackageManager().getActivityInfo(activity.getComponentName(),
                    PackageManager.GET_META_DATA);
            title = activity.getString(activityInfo.labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView actionBarTitle = (TextView) activity.findViewById(R.id.action_bar_title);
        actionBarTitle.setText(title);

//        ActionBar actionBar = activity.getSupportActionBar();
//        actionBar.setDisplayUseLogoEnabled(false);
//        actionBar.setIcon(R.drawable.ic_drawer);
////        actionBar.setDisplayHomeAsUpEnabled(true);
////        actionBar.setHomeButtonEnabled(true);
//        actionBar.setNavigationMode(navigationModeList);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setCustomView(R.layout.action_bar_layout);
//        CustomTextView actionBarList = (CustomTextView) activity.findViewById(R.id.action_bar_list_btn);
//        actionBarList.setText(R.string.list_icon);
//        actionBar.setBackgroundDrawable(new ColorDrawable(activity.getResources().getColor(R.color.violet_blue)));
//        actionBar.setLogo(R.drawable.ic_drawer);
//        actionBar.setIcon(R.drawable.ic_drawer);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
//        actionBar.setDisplayShowTitleEnabled(true);

//        getSupportActionBar().setTitle(R.string.about_event_location);
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayUseLogoEnabled(true);
//        setActionbarTextColor(actionBar, activity.getResources().getColor(R.color.white));
//        CustomTextView actionBarTitle = (CustomTextView) activity.findViewById(R.id.action_bar_title);

//        if (!title.equalsIgnoreCase(""))
//        {
////            actionBarTitle.setText(title);
//            actionBar.setTitle(title);
//        }

//        actionBar.setBackground(activity.getResources().getColor(R.color.violet_blue));
//        actionBar.setIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

//        actionBar.setTitle(activity.getTitle());
    }

    private void setActionbarTextColor(ActionBar actBar, int color) {

        String title = actBar.getTitle().toString();
        Spannable spannablerTitle = new SpannableString(title);
        spannablerTitle.setSpan(new ForegroundColorSpan(color), 0, spannablerTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actBar.setTitle(spannablerTitle);

    }

    /*public View initButtonIcon(Activity activity, View parentView , int buttonId , int buttonIcon, int buttonText, int buttonColor)
    {
        ViewStub stub;
        if (parentView == null)
        {
            stub = (ViewStub) activity.findViewById(buttonId);
        }
        else
        {
            stub = (ViewStub) parentView.findViewById(buttonId);
        }

        View stubInflated = stub.inflate();

        LinearLayout linearLayout = (LinearLayout) stubInflated.findViewById(R.id.button_layout);
        GradientDrawable bgShape = (GradientDrawable) linearLayout.getBackground();
        bgShape.setColor(activity.getResources().getColor(buttonColor));

        CustomTextView buttonIconText = (CustomTextView) stubInflated.findViewById(R.id.button_icon);
        buttonIconText.setText(activity.getString(buttonIcon));

        CustomButton button = (CustomButton) stubInflated.findViewById(R.id.button);
        button.setText(activity.getString(buttonText));
        button.setTextColor(activity.getResources().getColor(R.color.white));
        return button;
    }

    public void showHelpPage(Activity activity, FragmentTransaction ft, String fileName)
    {
//        String fileName = WithdrawFragment.this.getActivity().getString(R.string.help_withdraw_policy_file);
        if (!fileName.equals("")) {
//            String text = CommonHandler.getInstance().readTxtFile(activity, fileName);
            HelpFragment helpFragment = new HelpFragment();
            helpFragment.fileName = fileName;
            ft.addToBackStack(null);
            ft.replace(R.id.fragment_details, helpFragment);
            ft.commit();
        }
    }

    public Switch initCustomCheckItem(Activity activity, View parentView , int itemId , int itemText)
    {
        ViewStub stub;
        if (parentView == null)
        {
            stub = (ViewStub) activity.findViewById(itemId);
        }
        else
        {
            stub = (ViewStub) parentView.findViewById(itemId);
        }

        View stubInflated = stub.inflate();

        CustomTextView buttonIconText = (CustomTextView) stubInflated.findViewById(R.id.check_item_title);
        buttonIconText.setText(activity.getString(itemText));

        Switch aSwitch = (Switch) stubInflated.findViewById(R.id.check_item_switch);
        return aSwitch;
    }

    public CustomEditText initPhoneNumber(int phone_number_id , View parentView, Activity activity, boolean isPhone) {

        ViewStub stub;
        if (parentView == null)
        {
            stub = (ViewStub) activity.findViewById(phone_number_id);
        }
        else
        {
            stub = (ViewStub) parentView.findViewById(phone_number_id);
        }

        View stubInflated = stub.inflate();
        CustomEditText phone_number = (CustomEditText) stubInflated.findViewById(R.id.phone_number_input);
        CustomTextView countryCode = (CustomTextView) stubInflated.findViewById(R.id.country_code);
        if (isPhone)
        {
            phone_number.setHint(activity.getString(R.string.prompt_phone));
//            countryCode.setText("+");// + CountryCodes.getCode(DataHandler.getInstance().getCountryIndex()));
        }



//        else
//        {
//            countryCode.setText("+");
//        }

        phone_number.setText("+" + CountryCodes.getCode(DataHandler.getInstance().getCountryIndex()));
//        CountryCodes cc = new CountryCodes( this );
//        spinner.setAdapter( cc);
//
//        if( DataHandler.getInstance().getCountryIndex() > -1 )
//        {
//            spinner.setSelection(DataHandler.getInstance().getCountryIndex());
//        }
//        else
//        {
//            spinner.setSelection(-1);
//        }
        return phone_number;
    }

    public View initSpinner(Activity activity, View parentView , int spinnerId,
                            int spinnerDataArrayId, int spinnerPrompt, int iconId, List spinnerData) {
        ViewStub stub;
        if (parentView == null)
        {
            stub = (ViewStub) activity.findViewById(spinnerId);
        }
        else
        {
            stub = (ViewStub) parentView.findViewById(spinnerId);
        }

        View stubInflated = stub.inflate();
        final Spinner subActivitySpinner = (Spinner) stubInflated.findViewById(R.id.custom_spinner);
        GradientDrawable bgShape = (GradientDrawable) stubInflated.getBackground();
        bgShape.setColor(activity.getResources().getColor(R.color.snow));
        CustomTextView spinnerIcon = (CustomTextView) stubInflated.findViewById(R.id.spinner_icon);
        if (iconId != -1)
        {

            spinnerIcon.setText(activity.getResources().getString(iconId));
        }
        else {
            spinnerIcon.setVisibility(View.GONE);
        }
        ArrayAdapter adapter = null;
        if (spinnerData == null)
        {
            adapter = ArrayAdapter.createFromResource(activity,
                    spinnerDataArrayId, android.R.layout.simple_spinner_item);
        }
        else
        {
//            adapter = new ArrayAdapter(activity,
//                    spinnerDataArrayId, spinnerData);

            if ( spinnerData.size() > 0)
            {
                adapter = new ArrayAdapter(activity,
                        android.R.layout.simple_spinner_item, spinnerData);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }


        }

        if (adapter != null)
        {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subActivitySpinner.setSelection(-1);
//        adapter.insert(getResources().getString(spinner_prompt), 0);
            subActivitySpinner.setAdapter(adapter);
            subActivitySpinner.setPrompt(activity.getResources().getString(spinnerPrompt));
            stubInflated.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    subActivitySpinner.performClick();
                }
            });
        }
        else
        {
            subActivitySpinner.setVisibility(View.GONE);
        }

        return stubInflated;

    }

    public void showMessage(Activity activity , String message) {
        if (activity != null && message != null && !message.equalsIgnoreCase(""))
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
//        View view;
//        TextView text;
//        Toast toast = null;
//        toast.makeText(this, message, Toast.LENGTH_SHORT);
//        view = toast.getView();
//        text = (TextView) view.findViewById(android.R.id.message);
//        text.setTextColor(activity.getResources().getColor(R.color.black));
//        text.setShadowLayer(0,0,0,0);
//        view.setBackgroundResource(R.color.white);
//        toast.show();

    }

    public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 5;
    }


    public CustomEditText initEditTextWithIcon(Activity activity, View parentView ,
                                               int editTextId, int promptStringId, int iconId) {
        ViewStub stub;
        if (parentView == null)
        {
            stub = (ViewStub) activity.findViewById(editTextId);
        }
        else
        {
            stub = (ViewStub) parentView.findViewById(editTextId);
        }

        View stubInflated = stub.inflate();
        CustomEditText editText = (CustomEditText) stubInflated.findViewById(R.id.edit_text_value);
        editText.setHint(activity.getResources().getString(promptStringId));
        CustomTextView iconTextView = (CustomTextView) stubInflated.findViewById(R.id.edit_text_icon);
        iconTextView.setText(activity.getResources().getString(iconId));
        return editText;
    }

    public View initTextViewWithIcon(Activity activity, View view, int buttonId,
                                     String buttonText, int buttonIcon, int color)
    {
        ViewStub stub;
        if (view == null)
        {
            stub = (ViewStub) activity.findViewById(buttonId);
        }
        else
        {
            stub = (ViewStub) view.findViewById(buttonId);
        }
        View stubInflated = stub.inflate();
        CustomTextView buttonIconTextView = (CustomTextView) stubInflated.findViewById(R.id.text_view_icon);
        buttonIconTextView.setText(activity.getResources().getString(buttonIcon));

        CustomTextView buttonTextTextView = (CustomTextView) stubInflated.findViewById(R.id.text_view_text);
        buttonTextTextView.setText(buttonText);
        if (color != -1)
        {
            buttonIconTextView.setTextColor(activity.getResources().getColor(color));
            buttonTextTextView.setTextColor(activity.getResources().getColor(color));
        }

        return stubInflated;

    }

    public void initCancelCreditView(Activity activity, View fragmentView, String successParkingTip,
                                     String creditTip, double cost) {

        ViewStub stub;
        if (fragmentView == null)
        {
            stub = (ViewStub) activity.findViewById(R.id.cancel_penality_view_stub);
        }
        else
        {
            stub = (ViewStub) fragmentView.findViewById(R.id.cancel_penality_view_stub);
        }
        View stubInflated = stub.inflate();
        LinearLayout cancelLayout = (LinearLayout) stubInflated.findViewById(R.id.cancel_penality_layout);
        cancelLayout.setVisibility(View.VISIBLE);
        CustomTextView parkingCostTip = (CustomTextView) stubInflated.findViewById(R.id.parking_cost_item_tip);
        CustomTextView totalCreditTip = (CustomTextView) stubInflated.findViewById(R.id.total_credit_tip);
        CustomTextView costItem = (CustomTextView) stubInflated.findViewById(R.id.parking_cost_item);
        CustomTextView totalCredit = (CustomTextView) stubInflated.findViewById(R.id.total_credit);

        parkingCostTip.setText(successParkingTip);
        totalCreditTip.setText(creditTip);
        costItem.setText(String.valueOf(cost)  + " " );//DataHandler.getInstance().getCurrentCurrency());
        totalCredit.setText(DataHandler.getInstance().getCurrentUser().getCreditCard());

    }

    public CustomButton initTextViewWithIcon1(Activity activity, View view, int buttonId,
                                              String buttonText, int buttonIcon, int color)
    {
        ViewStub stub;
        if (view == null)
        {
            stub = (ViewStub) activity.findViewById(buttonId);
        }
        else
        {
            stub = (ViewStub) view.findViewById(buttonId);
        }
        View stubInflated = stub.inflate();
        CustomButton buttonIconTextView = (CustomButton) stubInflated.findViewById(R.id.text_view_icon);
        buttonIconTextView.setText(activity.getResources().getString(buttonIcon));
        buttonIconTextView.setTextColor(activity.getResources().getColor(R.color.white));

        CustomTextView buttonTextTextView = (CustomTextView) stubInflated.findViewById(R.id.text_view_text);
        buttonTextTextView.setText(buttonText);
        if (color != -1)
        {
            buttonIconTextView.setTextColor(activity.getResources().getColor(color));
            buttonTextTextView.setTextColor(activity.getResources().getColor(color));
        }

        return buttonIconTextView;

    }
    public void disableTextViewWithIcon(View textViewWithIcon, Activity activity)
    {
        CustomTextView buttonIconTextView = (CustomTextView) textViewWithIcon.findViewById(R.id.text_view_icon);
        buttonIconTextView.setTextColor(activity.getResources().getColor(R.color.lavender_gray));
        CustomTextView buttonTextTextView = (CustomTextView) textViewWithIcon.findViewById(R.id.text_view_text);
        buttonTextTextView.setTextColor(activity.getResources().getColor(R.color.lavender_gray));
    }

    public void showTakePictureView(final Activity activity) {

        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                activity);
        myAlertDialog.setTitle(activity.getString(R.string.upload_image_option));
        myAlertDialog.setMessage(activity.getString(R.string.upload_image_message));

        myAlertDialog.setPositiveButton(activity.getString(R.string.gallery_option),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = DataHandler.StoragePermission;

                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                    return null;
                        }
                        else
                        {
                            Intent pictureActionIntent = null;

                            pictureActionIntent = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            activity.startActivityForResult(
                                    pictureActionIntent,
                                    GALLERY_PICTURE);
                        }


                    }
                });

        myAlertDialog.setNegativeButton(activity.getString(R.string.camera_option),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = DataHandler.CameraPermission;

                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//                    return null;
                        }
                        else
                        {
                            Intent intent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment
                                    .getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(f));
                            intent.putExtra("outputX", dpToPixel(activity, DataHandler.getInstance().getImageWidth()));
                            intent.putExtra("outputY", dpToPixel(activity, DataHandler.getInstance().getImageHeight()));
                            activity.startActivityForResult(intent,
                                    CAMERA_REQUEST);
                        }


                    }
                });
        myAlertDialog.show();
    }

    public float dpToPixel(Activity activity, int dp) {
        Resources r = activity.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String filePath,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public Bitmap getImageFromCameraOrGallery(Activity activity, int requestCode, int resultCode,
                                              Intent data, int width, int height) {

        Bitmap bitmap = null;
        String selectedImagePath = null;

        try {

//        int width = 50;
//        int height = 50;
            if (resultCode == activity.RESULT_OK && requestCode == CAMERA_REQUEST) {

                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                if (!f.exists()) {

                    Toast.makeText(activity.getBaseContext(),
                            "Error while capturing image", Toast.LENGTH_LONG).show();
                    return bitmap;
                }


                selectedImagePath = f.getAbsolutePath();
                bitmap = decodeSampledBitmapFromFile(selectedImagePath, width, height);
//                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

//                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

//                int rotate = 0;
//                try {
//                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
//                    int orientation = exif.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION,
//                            ExifInterface.ORIENTATION_NORMAL);
//
//                    switch (orientation) {
//                        case ExifInterface.ORIENTATION_ROTATE_270:
//                            rotate = 270;
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            rotate = 180;
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            rotate = 90;
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Matrix matrix = new Matrix();
//                matrix.postRotate(rotate);
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                        bitmap.getHeight(), matrix, true);
                //storeImageTosdCard(bitmap);


            } else if (resultCode == activity.RESULT_OK && requestCode == GALLERY_PICTURE) {
                if (data != null) {

                    Uri selectedImage = data.getData();
                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = activity.getContentResolver().query(selectedImage, filePath,
                            null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    selectedImagePath = c.getString(columnIndex);
                    c.close();

                    if (selectedImagePath != null) {
//                    txt_image_path.setText(selectedImagePath);
//                        bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                        bitmap = decodeSampledBitmapFromFile(selectedImagePath, width, height);
                    }


                    // preview image
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cancelled",
                            Toast.LENGTH_SHORT).show();
                }
            }
//            if (bitmap != null)
//            {
////            int w  = bitmap.getWidth();
////            int h = bitmap.getHeight();
//                bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
//            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
    public Marker drawMarker(LatLng currentPosition, GoogleMap mMap, String title){
        Marker mm = mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + currentPosition.latitude + "Lng:" + currentPosition.longitude)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .title(title));

        return mm;
    }

    public Marker drawCarMarker(LatLng currentPosition, GoogleMap mMap, String title){
        Marker mm = mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + currentPosition.latitude + "Lng:" + currentPosition.longitude)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon))
                .title(title));

        return mm;
    }

    public Marker drawMarkerWithIcon(LatLng currentPosition, GoogleMap mMap, String title, int customIcon){
        Marker mm = mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + currentPosition.latitude + "Lng:" + currentPosition.longitude)
                .icon(BitmapDescriptorFactory.fromResource(customIcon))
                .title(title));

        return mm;
    }

    public View init_parking_car_info(final Fragment fragment, View fragmentView, Car car, Parking parking, String tip)
    {
        View parkingCarInfostub = (View) fragmentView.findViewById(R.id.parking_car_info);
//        View parkingCarInfostubInflated = parkingCarInfostub.inflate();
        //handle information for car
        if (car != null)
        {
            if (!car.getImageUrl().equalsIgnoreCase(""))
            {
                String imageUrl = ServiceUrlsHandler.getInstance().appendServerUrl(car.getImageUrl());
                final ImageView carImage = (ImageView) parkingCarInfostub.findViewById(R.id.car_image);

                carImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ImageViewerFragment imageViewFragment = new ImageViewerFragment();
                        imageViewFragment.setBitmap(((BitmapDrawable)carImage.getDrawable()).getBitmap());
                        FragmentTransaction ft = fragment.getActivity().getSupportFragmentManager().beginTransaction();
                        ft.hide(fragment);
                        ft.addToBackStack(null);
                        ft.add(R.id.fragment, imageViewFragment);
                        ft.commit();
                    }
                });

                ImageDownloader imageDownloader = new ImageDownloader();
//            imageDownloader.setFragment(imageViewFragment);
                imageDownloader.setDownloadedImg(carImage);
                imageDownloader.execute(imageUrl);
            }

            CustomTextView carPlateNumber = (CustomTextView) parkingCarInfostub.findViewById(R.id.car_plate_number);
            GradientDrawable bgShape = (GradientDrawable) carPlateNumber.getBackground();
            bgShape.setColor(fragment.getActivity().getResources().getColor(R.color.anti_flash_white));

            carPlateNumber.setText(car.getPlateNumber());
            CustomTextView carColorView = (CustomTextView) parkingCarInfostub.findViewById(R.id.color_view);
            if (car.getColor() != null)
            {
                try {
                    carColorView.setBackgroundColor(Color.parseColor(car.getColor()));
                }catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
//                carColorView.setBackgroundColor(Integer.parseInt(car.getColor()));
            }
            CustomTextView carBrand = (CustomTextView) parkingCarInfostub.findViewById(R.id.car_brand);
            carBrand.setText(car.getBrand());
        }
        return parkingCarInfostub;
    }
    public void updateDistance(Activity activity, View parkingCarInfostubInflated, Parking parking, LatLng currLocation)
    {
        //handle distance
        Logger.getInstance().appendLog("HeadingToCarFragment - updateDistanceView" + currLocation);
        if (activity != null && currLocation != null && parking != null && parkingCarInfostubInflated != null)
        {
            double distance = CommonHandler.getInstance().getDistance(currLocation, parking.getLocation());
            CustomTextView parkingDistance = (CustomTextView) parkingCarInfostubInflated.findViewById(R.id.parking_distance);
            String meter = activity.getString(R.string.meters);
            if (parkingDistance != null)
                parkingDistance.setText(String.valueOf(distance) + " " + meter);
        }

    }
    public CustomButton setButtonView(int color, View view, int buttonId)
    {
        CustomButton cancelOfferButton = (CustomButton) view.findViewById(buttonId);
        GradientDrawable bgShape = (GradientDrawable) cancelOfferButton.getBackground();
        bgShape.setColor(color);
        return cancelOfferButton;
    }
    public void initParkingListRow(Parking parking, View view, Activity activity, TimerInterface interfaceClass) {
        // calculate distance between cureent location and parking location
        if (parking != null)
        {
            LatLng currentLocation = DataHandler.getInstance().getLocation();
            if (currentLocation != null)
            {
                String distance = parking.getDistanceStr(currentLocation.latitude, currentLocation.longitude);
                CustomTextView distanceTextView = (CustomTextView) view.findViewById(R.id.parking_row_distance);
                distanceTextView.setText(distance);
            }

            CustomTextView costTextView = (CustomTextView) view.findViewById(R.id.parking_row_cost);
            if (!Double.isNaN(parking.getCost()))
            {
                costTextView.setText(String.valueOf(parking.getCost()));
            }

//        CustomTextView currency = (CustomTextView) view.findViewById(R.id.current_currency);
//        currency.setText(DataHandler.getInstance().getCurrentCurrency());

            initRemainingTime(activity, parking, view, interfaceClass, null);

            ProgressBar downloadRecordProgress = (ProgressBar) view.findViewById(R.id.sound_progress);
//
//            if (CommonHandler.getInstance().isNotNull(parking.getSoundUrl()))
//            {
//                View recordingstubInflated = initParkingSound(view);
//                ServiceHandler.getInstance().downloadSoundService(parking, downloadRecordProgress,
//                        recordingstubInflated);
//            }
//            else
//            {
            downloadRecordProgress.setVisibility(View.GONE);
//            }


//        CustomTextView descriptionTextView = (CustomTextView) view.findViewById(R.id.parking_row_place);
//        descriptionTextView.setText(parking.getDescription());
            ViewHandler.getInstance().initTextViewWithIcon(activity, view, R.id.parking_location, parking.getDescription(), R.string.location_icon, -1);


//        ViewStub stub = (ViewStub) view.findViewById(R.id.parking_row_recording);
//        View stubInflated = stub.inflate();
        }

    }
    public long getDiffMinutes(long diff)
    {
        return diff / (60 * 1000) % 60;
    }
    public CountDownTimer initRemainingTime(Activity activity, final Parking parking, View view,
                                            final TimerInterface interfaceClass, final WheelIndicatorView timeIndicator )
    {
        final int wheelPercent = 100;
//        double rate = 1.00d;
        int lastMinute;
        CountDownTimer downTimer = null;
        final CustomTextView timeTextView = (CustomTextView) view.findViewById(R.id.parking_row_time);
        if (parking != null && parking.getStartTimeLong() != null)
        {
//            long diff = new Date().getTime() - CommonHandler.getInstance().getDateFromLong(parking.getStartTimeLong()).getTime();
            long diff = CommonHandler.getInstance().getDiffernceTime(parking);
            long diffSeconds = 60 - diff / 1000 % 60;
            final long diffMinutes = getDiffMinutes(diff);
            long diffHours = (diff/(1000*60*60) % 24);
            lastMinute = (int) (parking.getLeavingTimeMinutes() - diffMinutes - 1);
            final double rate = wheelPercent/ ((double) ( lastMinute * 60));
            final int lastSum = lastMinute * 60;
            if (diffHours <= 0 && diffMinutes < parking.getLeavingTimeMinutes() )
            {
                if (timeIndicator != null)
                {
                    timeIndicator.setFilledPercent(wheelPercent);
                    timeIndicator.notifyDataSetChanged();
                }

                timeTextView.setText(String.valueOf(parking.getLeavingTimeMinutes() - diffMinutes) + ":" + String.valueOf(60 - diffSeconds));
                downTimer = new CountDownTimer(((parking.getLeavingTimeMinutes() -  diffMinutes - 1) * 60 + diffSeconds) * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        int seconds = (int) (millisUntilFinished / 1000) % 60 ;
                        int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);

                        if (timeIndicator != null)
                        {
                            int sum = (minutes * 60 + seconds);
                            int hh = (int) ((lastSum - sum) * rate);
                            timeIndicator.setFilledPercent(wheelPercent - (hh));
                            timeIndicator.notifyDataSetChanged();
                        }

                        String text;
                        if (seconds < 10)
                            text = minutes + ":0" + seconds;
                        else
                            text = minutes + ":" + seconds;
                        timeTextView.setText(text);
                    }

                    public void onFinish() {
                        timeTextView.setText("00:00");
                        if (interfaceClass != null)
                            interfaceClass.afterFinishTimer();

                    }
                }.start();
            }
            else
            {
                timeTextView.setText("00:00");
                if (interfaceClass != null)
                    interfaceClass.afterFinishTimer();
            }

        }
        else
        {
            timeTextView.setText("00:00");
            if (interfaceClass != null)
                interfaceClass.afterFinishTimer();
//            timeTextView.setText(String.valueOf(parking.getLeavingTimeMinutes()));
        }
        return  downTimer;
    }


    public void zoomCameraToCurrentLocation(LatLng currentLocation, GoogleMap mMap)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLocation);
        zoomToLatLngBounds(builder, mMap);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, mapZoom));
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    public void zoomToLatLngBounds(LatLngBounds.Builder builder, final GoogleMap mMap) {
        LatLngBounds bounds = builder.build();
        LatLng center = bounds.getCenter();
        LatLng northEast = move(center, 400, 400);
        LatLng southWest = move(center, -400, -400);
        builder.include(southWest);
        builder.include(northEast);
        final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), 50);
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
//                mMap.animateCamera(cu);
                mMap.moveCamera(cu);
            }
        });
    }

    public LatLng getNE( LatLng center)
    {
        return move(center, 400, 400);
    }

    public LatLng getSW(LatLng center)
    {
        return move(center, -400, -400);
    }

    private static final double EARTHRADIUS = 6366198;

    private static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }

    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTHRADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }


    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTHRADIUS;
        return Math.toDegrees(rad);
    }

    public void initSuccessfullScreenView(View fragmentView, String successMainTip, String successParkingTip,
                                          String creditTip, double cost) {

        CustomTextView successfullTip = (CustomTextView) fragmentView.findViewById(R.id.successfull_tip);
        successfullTip.setText(successMainTip);

        CustomTextView parkingCostTip = (CustomTextView) fragmentView.findViewById(R.id.parking_cost_item_tip);
        parkingCostTip.setText(successParkingTip);

        CustomTextView totalCreditTip = (CustomTextView) fragmentView.findViewById(R.id.total_credit_tip);
        totalCreditTip.setText(creditTip);


        CustomTextView costItem = (CustomTextView) fragmentView.findViewById(R.id.parking_cost_item);
        costItem.setText(String.valueOf(cost)  + "  ");

        CustomTextView totalCredit = (CustomTextView) fragmentView.findViewById(R.id.total_credit);
        totalCredit.setText(DataHandler.getInstance().getCurrentUser().getCreditCard());

    }

    public void initCancelViewText(View view, String mainTipText, String descTipText, String descTipText1) {
        CustomTextView mainTip = (CustomTextView) view.findViewById(R.id.cancel_main_tip);
        mainTip.setText(mainTipText);
        CustomTextView descTip = (CustomTextView) view.findViewById(R.id.cancel_desc_tip);
        descTip.setText(descTipText);
        CustomTextView descTip1 = (CustomTextView) view.findViewById(R.id.cancel_desc_tip1);
        descTip1.setText(descTipText1);
    }

    public AlertDialog.Builder createAlertDialog(String title, String message, Activity activity) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                activity);

        myAlertDialog.setTitle(title);
        myAlertDialog.setMessage(message);
        return myAlertDialog;
    }

    public void disableRefreshAndCurrentParking(Activity activity) {
        LinearLayout currParking = (LinearLayout) activity.findViewById(R.id.current_parking);
        currParking.setVisibility(View.GONE);

    }
//    public void ShowRefreshPopUp(Activity activity) {
////        LinearLayout currParking = (LinearLayout) activity.findViewById(R.id.current_parking);
////        currParking.setVisibility(View.GONE);
//        CustomTextView goToParking = (CustomTextView) activity.findViewById(R.id.go_to_parking);
//        goToParking.setVisibility(View.GONE);
//        CustomTextView refreshParking = (CustomTextView) activity.findViewById(R.id.refresh_parking);
//        refreshParking.setVisibility(View.VISIBLE);
//
//    }

    //    public void ShowParkingPopUp(Activity activity) {
//        CustomTextView goToParking = (CustomTextView) activity.findViewById(R.id.go_to_parking);
//        goToParking.setVisibility(View.VISIBLE);
//        CustomTextView refreshParking = (CustomTextView) activity.findViewById(R.id.refresh_parking);
//        refreshParking.setVisibility(View.GONE);
//    }
//
    public void hideRefreshPopUp(Activity activity) {
        CustomTextView refreshParking = (CustomTextView) activity.findViewById(R.id.refresh_parking);
        if (refreshParking != null)
            refreshParking.setVisibility(View.GONE);
    }

    public void showRefreshPopUp(Activity activity) {
        CustomTextView refreshParking = (CustomTextView) activity.findViewById(R.id.refresh_parking);
        if (refreshParking != null)
            refreshParking.setVisibility(View.VISIBLE);
    }


    public void showMessageFromJson(Activity activity, String result) {
        try {
            JSONObject object = new JSONObject(result);
            String statusRegistration = object.getString("message");
            ViewHandler.getInstance().showMessage(activity , statusRegistration);

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


//    public void initiateTimerWheel(WheelIndicatorView timeIndicator, Parking currParking,
//                                    CountDownTimer downTimer, RelativeLayout finishTimeLayout,
//                                    View waitingTimeWheelInflated, CustomButton cancelOffer)
//    {
//        int wheelPercent = 100;
//
//        timeIndicator.setFilledPercent(wheelPercent);
//        int lastMinute = currParking.getLeavingTimeMinutes();
//        double rate = wheelPercent/ ((double) ( lastMinute * 60));
//        int lastSum = lastMinute * 60;
//        timeIndicator.notifyDataSetChanged();
//        CustomTextView firstLineTimeSelected = (CustomTextView) waitingTimeWheelInflated.findViewById(R.id.first_line_time_selected);
//
//        downTimer = new CountDownTimer(lastMinute * 60 * 1000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                int seconds = (int) (millisUntilFinished / 1000) % 60 ;
//                int minutes = (int) ((millisUntilFinished / (1000*60)) % 60);
//                int sum = (minutes * 60 + seconds);
//                int hh = (int) ((lastSum - sum) * rate);
//                timeIndicator.setFilledPercent(wheelPercent - (hh));
//                timeIndicator.notifyDataSetChanged();
//
//                String text;
//                if (seconds < 10)
//                    text = minutes + ":0" + seconds;
//                else
//                    text = minutes + ":" + seconds;
//                firstLineTimeSelected.setText(text);
//            }
//
//            public void onFinish() {
//                firstLineTimeSelected.setText("00:00");
//
//                if (finishTimeLayout != null)
//                {
//                    finishTimeLayout.setVisibility(View.VISIBLE);
//                    cancelOffer.setVisibility(View.GONE);
//                }
//            }
//        }.start();
//    }

    public void initParkingInfoLayout(Activity activity, Parking parking, Fragment fragment, View fragmentView )
    {
        boolean isSound = false;
        boolean isImage = false;
        //handle info for parking description(parking slot , location and car size)
        ViewHandler.getInstance().initTextViewWithIcon(fragment.getActivity(), fragmentView, R.id.parking_location, parking.getDescription(), R.string.location_icon, -1);
        ViewHandler.getInstance().initTextViewWithIcon(fragment.getActivity(), fragmentView, R.id.parking_slot, parking.getParkingSlot(), R.string.parking_slot_icon, -1);
        ViewHandler.getInstance().initTextViewWithIcon(fragment.getActivity(), fragmentView, R.id.parking_car_size, parking.getCarSize(), R.string.car_icon, -1);


        // handle media view for parking
        View parkingMediaView = fragmentView.findViewById(R.id.parking_media_info);
        CustomButton recordButton = (CustomButton) parkingMediaView.findViewById(R.id.audio_play_button);
        ProgressBar audioProgress = (ProgressBar) parkingMediaView.findViewById(R.id.audio_progress);
//        audioProgress.setVisibility(View.GONE);
        if (!parking.getSoundUrl().equalsIgnoreCase(""))
        {
            isSound = true;
            File file = new File(parking.getRecordingFileName());
            if(file.exists())
            {
                //hide progress and show record view
                audioProgress.setVisibility(View.GONE);
                recordButton.setVisibility(View.VISIBLE);
            }
            else
            {
                //show progress and hide record view
                ServiceHandler.getInstance().downloadSoundService(activity, parking, audioProgress,
                        recordButton);
            }
        }
        else
        {
            audioProgress.setVisibility(View.GONE);
        }

        ImageView parkingImage = (ImageView) parkingMediaView.findViewById(R.id.parking_image);
        CommonHandler.getInstance().handleImageViewClickListener(parkingImage, fragment);
        Bitmap parkingBitmap = parking.getParkingbitmap();

        if (parkingBitmap != null)
        {
            isImage = true;
            try
            {
                parkingImage.setVisibility(View.VISIBLE);
                parkingImage.setImageBitmap(parkingBitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else
        {
            if (!parking.getImageUrl().equalsIgnoreCase("")) {
                isImage = true;
                parkingImage.setVisibility(View.VISIBLE);
                CommonHandler.getInstance().handleImageViewClickListener(parkingImage, fragment);
                ServiceHandler.getInstance().downloadImageService(parking, parkingImage);
            }
        }

        if (!isSound)
        {
//            parkingImage.setLayoutParams(Re);
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams)parkingImage.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
            parkingImage.setLayoutParams(params);
        }
        if (!isImage)
        {
//            parkingImage.setLayoutParams(Re);
            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) recordButton.getLayoutParams();

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            recordButton.setLayoutParams(params);

        }
    }



    public void handleCanNotReachAction(final Activity activity, View fragmentView,
                                        final com.accorpa.xpark.interfaces.ServiceTask fragment,
                                        final Parking parking, final CountDownTimer timer) {

        if (activity != null &&  DataHandler.getInstance().getLocation() != null && parking.getLocation() != null)
        {

//            parking.getLocation();
//            DataHandler.getInstance().getLocation();
            double distance = CommonHandler.getInstance().getDistance(parking.getLocation(), DataHandler.getInstance().getLocation());
            if ( distance <= DataHandler.getInstance().getCanNotReachDistance())
            {
                handleCanNotReachView(activity, fragmentView, fragment, parking, timer);
            }
        }
    }

    public int fixSignUpComponentsView(final Activity activity, View fragmentView, final FragmentTransaction ft)
    {

        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();

        ArrayList<String> countryList=new ArrayList<String>();
        int countryIndex = 0;
        String[] locales = Locale.getISOCountries();

        for (String countryCode1 : locales) {
            Locale obj = new Locale("", countryCode1);
            System.out.println("Country Name = " + obj.getDisplayCountry());
            String displayCountry = obj.getDisplayCountry();
            if (countryCode.equalsIgnoreCase(countryCode1))
            {
                countryIndex= countryList.size();
            }
            countryList.add(displayCountry);
        }

        View spinnerView = ViewHandler.getInstance().initSpinner(activity, fragmentView, R.id.country_model_spinner,
                -1, R.string.prompt_car_model_spinner, R.string.globe_alt_1icon, countryList);

        CustomTextView acceptTermsText = (CustomTextView) fragmentView.findViewById(R.id.terms_of_agreement);
        acceptTermsText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String fileName = activity.getString(R.string.terms_and_conditions_file);

                if (!fileName.equals("")) {
//                    String text = CommonHandler.getInstance().readTxtFile(activity, fileName);
                    HelpFragment helpFragment = new HelpFragment();
                    helpFragment.fileName = fileName;
                    ft.addToBackStack(null);
                    ft.replace(R.id.fragment, helpFragment);
                    ft.commit();
                }
            }
        });
        return countryIndex;
    }

    public void handleCanNotReachView(final Activity activity, View fragmentView,
                                      final com.accorpa.xpark.interfaces.ServiceTask fragment,
                                      final Parking parking, final CountDownTimer timer) {
        CustomTextView farFrom70TextView = (CustomTextView) fragmentView.findViewById(R.id.far_from_70_text_view);
        farFrom70TextView.setVisibility(View.GONE);
        CustomButton cannotReachUser = ViewHandler.getInstance().setButtonView(activity.getResources().getColor(R.color.coral_pink),
                fragmentView, R.id.action_cannot_reach);

        cannotReachUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = ViewHandler.getInstance().createAlertDialog(activity.getResources().getString(R.string.arrival_alert_title_prompt),
                        activity.getResources().getString(R.string.confirm_message_to_cannot_reach_parking),
                        activity);

                final EditText input = new EditText(activity);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                int padding = (int) activity.getResources().getDimension(R.dimen.activity_horizontal_margin);
                int inputPadding = padding * 3;
                input.setPadding(inputPadding, padding, inputPadding, padding);
                lp.setMargins(padding, padding, padding, padding);
                input.setLayoutParams(lp);
                myAlertDialog.setView(input);
                final AlertDialog OptionDialog = myAlertDialog.create();
                myAlertDialog.setPositiveButton(activity.getResources().getString(R.string.ok_prompt),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(timer != null)
                                    timer.cancel();
                                ParkingStatus parkingStatus = new ParkingStatus(parking.getId(),
                                        DataHandler.getInstance().getCurrentUserId(), ParkingStatus.CAN_NOT_REACH);

                                String url = ServiceUrlsHandler.getInstance().getParkingStatusUrl();
                                ArrayList params = ServiceUrlsHandler.getInstance().getParkingStatusCancellingParam(parkingStatus,
                                        "", String.valueOf(input.getText()));
                                ServiceHandler.getInstance().executeTask(url, "", ServiceHandler.getInstance().getPostMethod(),
                                        fragment, params, null, false);

                            }
                        });
                myAlertDialog.setNegativeButton(activity.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                OptionDialog.cancel();
                            }
                        });

                myAlertDialog.show();



            }
        });

//        ProgressBar callUserProgress = (ProgressBar) fragmentView.findViewById(R.id.cannot_reach_call_user);
        CustomButton callUser = ViewHandler.getInstance().setButtonView(activity.getResources().getColor(R.color.jade),
                fragmentView, R.id.action_call_user);
        callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParkingStatus parkingStatus = new ParkingStatus(parking.getId(),
                        DataHandler.getInstance().getCurrentUserId(), ParkingStatus.CALL_OTHER_PARTY);

//                callUserProgress.setVisibility(View.VISIBLE);
                String url = ServiceUrlsHandler.getInstance().getParkingStatusUrl();
                ArrayList params = ServiceUrlsHandler.getInstance().getParkingStatusParam(parkingStatus);
                ServiceHandler.getInstance().executeTask(url, "", ServiceHandler.getInstance().getPostMethod(),
                        fragment, params, null, false);

                if(parking.getUserPhoneNumber() != null)
                {
                    Bundle parameters = new Bundle();
                    parameters.putString(DataHandler.LogUserId, DataHandler.getInstance().getCurrentUserId());
                    parameters.putString(DataHandler.LogParkingId, parking.getId());
                    CommonHandler.getInstance().callPhoneNumber(activity, parking.getUserPhoneNumber(), true, parameters);
                }

            }
        });
    }

    public FrameLayout initCannotReachView(View view)
    {
        ViewStub canNotReachStub = (ViewStub) view.findViewById(R.id.cannot_reach_layout_view);
        return  (FrameLayout) canNotReachStub.inflate();
    }


    public SwipeMenuItem getSwipeMenuItem(Context context,String itemTitle, int itemColor, int textColor) {
        float menuSize = 120;
        SwipeMenuItem swipeItem = new SwipeMenuItem(context);
        // set item background
//        int color1 = context.getResources().getColor(itemColor);
//        swipeItem.setBackground(color1);
        // set item width
        swipeItem.setWidth(CommonHandler.getInstance().dp(menuSize));
        // set item title
        swipeItem.setTitle(itemTitle);
        // set item title fontsize
        swipeItem.setTitleSize(18);
        // set item title font color
        swipeItem.setTitleColor(context.getResources().getColor(textColor));

        swipeItem.setBackground(new ColorDrawable(context.getResources().getColor(itemColor)));
        return swipeItem;
    }*/
}
