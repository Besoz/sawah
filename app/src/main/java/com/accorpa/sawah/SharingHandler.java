package com.accorpa.sawah;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

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
        i.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.share_msg_a)+" "+
                placeName+" "+context.getString(R.string.share_msg_b));
        context.startActivity(Intent.createChooser(i,"Share via"));
    }

    public void callNumber(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }

    public void openWebsite(Context context, String website){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(website));
        context.startActivity(intent);
    }

    public void openMapIntent(Context context, double placeLat, double placLong){

        Uri gmmIntentUri = Uri.parse("geo:"+placeLat+","+placLong+"?q="+placeLat+","+placLong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);

    }

}
