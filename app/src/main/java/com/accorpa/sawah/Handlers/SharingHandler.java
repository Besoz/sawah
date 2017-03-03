package com.accorpa.sawah.Handlers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.R;

import java.util.List;

/**
 * Created by bassem on 29/12/16.
 */
public class SharingHandler {

    private static SharingHandler ourInstance = new SharingHandler();

    public static SharingHandler getInstance() {


        return ourInstance;
    }

    private SharingHandler() {


    }

    public void shareAppOnFacebook(Context context){

//        List<Intent> targetedShareIntents = new ArrayList<Intent>();

        Intent facebookIntent = getShareIntent("facebook", "", context.getString(R.string.share_msg_a), context);
// subject may not work, but if you have a url place it in text_or_url
//        if(facebookIntent != null)
//            targetedShareIntents.add(facebookIntent);

        Intent chooser = Intent.createChooser(facebookIntent,  "Share with");

//        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

        context.startActivity(chooser);
    }

    public void shareAppOnWhatsapp(Context context){

        PackageManager pm= context.getPackageManager();

        Intent waIntent = getShareIntent("com.whatsapp", "", context.getString(R.string.share_msg_a), context);

        if(waIntent == null)
            Toast.makeText(context, R.string.share_msg_a, Toast.LENGTH_SHORT)
                    .show();
        else
            context.startActivity(Intent.createChooser(waIntent, "Share with"));


    }
    public void shareAppOnTwitter(Context context){
//        TwitterHelper.getInstance().composeTweet(context, context.getString(R.string.share_msg_a));
    }

    private Intent getShareIntent(String type, String subject, String text, Context context)
    {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
        System.out.println("resinfo: " + resInfo);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  subject);
                    share.putExtra(Intent.EXTRA_TEXT,     text);
                    if(type != "") share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;

            return share;
        }
        return null;
    }

    public void share(Context context, String placeName){

        Intent i=new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");

        i.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_msg_subject));
        i.putExtra(android.content.Intent.EXTRA_TEXT, placeName);
        context.startActivity(Intent.createChooser(i,"Share via"));
    }

    public void callNumber(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }



    public void openMapIntent(Context context, double placeLat, double placLong){

        Uri gmmIntentUri = Uri.parse("geo:"+placeLat+","+placLong+"?q="+placeLat+","+placLong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }

    public void requestUberRide(Context context, double dropLat, double dropLong){
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
            String uri = "uber://?dropoff[latitude]="+dropLat+"&dropoff[longitude]="+dropLong;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            context.startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
            String url = "https://m.uber.com/sign-up?client_id=<CLIENT_ID>";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    }

    public void contactSawah(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("application/octet-stream");
//        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@sawahapp.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_msg_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT   , "من نظام Android");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openWebsite(Context context, String website){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(website));
        context.startActivity(intent);
    }

    public void openTwitterPage(Context context){

        try {
            openWebsite(context, context.getString(R.string.twitter_app_page_uri));

        }catch (Exception e) {
            openWebsite(context, context.getString(R.string.twitter_page_url));

        }
    }


}
