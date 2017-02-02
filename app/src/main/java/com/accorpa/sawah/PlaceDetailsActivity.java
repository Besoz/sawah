package com.accorpa.sawah;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;

import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomTextView;
import com.android.volley.toolbox.NetworkImageView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class PlaceDetailsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener{


    private static final int VIEW_COMMENTS_COUNT = 2;
    private CustomTextView bioTextView, titleArabic, titleEnglish, rating;
    private NetworkImageView placeImage;

    private ImageButton shareButtton, callButton, openSiteButton, checkInButton;

    private CustomButton addCommentButton, moreCommentsButton;

    private View commentsView;

    private LinearLayout linearLayout;


    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView simpleExpandableListView;

    private String groupItems[] = {"Animals"};
    private String[][] childItems = {{"Dog", "Cat", "Tiger"}};

    private Place place;

    private static final String NAME = "NAME";

    ExpandableRelativeLayout mExpandLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place_details);


        String placeJSONObject = (String) getIntent().getSerializableExtra("PlaceJSONObject");

        ObjectMapper mapper = new ObjectMapper();

        try {
            place = mapper.readValue(placeJSONObject, Place.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bioTextView = (CustomTextView) findViewById(R.id.bio_text);
        bioTextView.setText(place.getBio());

        titleArabic = (CustomTextView) findViewById(R.id.title_ar);
        titleArabic.setText(place.getPalceNameArb());

        titleEnglish = (CustomTextView) findViewById(R.id.title_en);
        titleEnglish.setText(place.getPalceNameEng());

        rating = (CustomTextView) findViewById(R.id.rating);
        rating.setText(place.getRatingID());

        placeImage = (NetworkImageView) findViewById(R.id.imageView2);
        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");
        placeImage.setImageUrl(imageUrl,
                ServiceHandler.getInstance(this.getApplicationContext()).getImageLoader());

        shareButtton = (ImageButton) findViewById(R.id.share_button);
        shareButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingHandler.getInstance().share(PlaceDetailsActivity.this, place.getPalceNameArb());
            }
        });

        callButton = (ImageButton) findViewById(R.id.call_button);
        if(place.haveContactNumber()){
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingHandler.getInstance().callNumber(PlaceDetailsActivity.this, place.getContactNumber());
                }
            });
        }else{
            callButton.setImageResource(R.drawable.call_disabled);
        }


        openSiteButton = (ImageButton) findViewById(R.id.website_button);
        if(place.haveWebSite()){
            openSiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingHandler.getInstance().openWebsite(PlaceDetailsActivity.this,
                            URLUtil.guessUrl(place.getWebSite()));
                }
            });
        }else{
            openSiteButton.setImageResource(R.drawable.globe_disabled);
        }

        addCommentButton = (CustomButton) findViewById(R.id.add_comment_button);
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHandler dataHandler = DataHandler.getInstance(PlaceDetailsActivity.this);
                if(dataHandler.userExist()){
                    NavigationHandler.getInstance().startCommentActivity(PlaceDetailsActivity.this,
                            place.getPlaceID());
                }else{
//                    show not signed in dialog
                }
            }
        });


        if(place.getCommentsCount() != 0){

            commentsView = findViewById(R.id.comments_view);
            commentsView.setVisibility(View.VISIBLE);

            linearLayout = (LinearLayout) findViewById(R.id.comments_list);

            PlaceComment[] comments = place.getComments();

            int totalCommentsCount,remainingCommentsCount;
            totalCommentsCount = remainingCommentsCount = place.getCommentsCount();

            CommentsAdapter adapter =  new CommentsAdapter(this, comments);

            for(int i = 0; i < Math.min(VIEW_COMMENTS_COUNT, totalCommentsCount); i++){
                linearLayout.addView(adapter.getView(i, null, null));
                remainingCommentsCount --;
            }

            moreCommentsButton = (CustomButton) findViewById(R.id.more_comments_button);

            if(remainingCommentsCount > 0){
                moreCommentsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataHandler dataHandler = DataHandler.getInstance(PlaceDetailsActivity.this);
                        NavigationHandler.getInstance().startCommentsListActivity(PlaceDetailsActivity.this,
                                PlaceDetailsActivity.this.place.getComments());
                    }
                });
            }else{
                moreCommentsButton.setVisibility(View.GONE);
            }
            
        }




//        checkInButton = (ImageButton) findViewById(R.id.checkin_button);
//        checkInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharingHandler.getInstance().callNumber(PlaceDetailsActivity.this, place.getContactNumber());
//            }
//        });

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        for (String name: place.getAppointments().keySet()){

            String key = name.toString();
            String value = place.getAppointments().get(name).toString();
           Log.d("gg", "++++++++++++++++++ " + key + " " + value);


        }
//
//        Button mExpandButton = (Button) findViewById(R.id.expandButton);
////        Button mMoveChildButton = (Button) findViewById(R.id.moveChildButton);
//        Button mMoveChildButton2 = (Button) findViewById(R.id.moveChildButton2);
//        Button  mMoveTopButton = (Button) findViewById(R.id.moveTopButton);
//        Button mSetCloseHeightButton = (Button) findViewById(R.id.setCloseHeightButton);
//        mExpandLayout = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
//        mExpandLayout.setOnClickListener(this);
//        mExpandButton.setOnClickListener(this);
////        mMoveChildButton.setOnClickListener(this);
//        mMoveChildButton2.setOnClickListener(this);
//        mMoveTopButton.setOnClickListener(this);
//        mSetCloseHeightButton.setOnClickListener(this);
    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_place_details;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin);

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, 100, 100), Matrix.ScaleToFit.CENTER);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        LatLng placeLocation = new LatLng(place.getLattitude(), place.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(placeLocation).icon(bitmapDescriptor));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SharingHandler.getInstance().openMapIntent(PlaceDetailsActivity.this, place.getLattitude(),
                        place.getLongitude());
            }
        });


    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.expandButton:
//                mExpandLayout.toggle();
//                break;
//            case R.id.expandableLayout:
//                mExpandLayout.toggle();
//                break;
////            case R.id.moveChildButton:
////                mExpandLayout.moveChild(0);
////                break;
//            case R.id.moveChildButton2:
//                mExpandLayout.moveChild(1);
//                break;
//            case R.id.moveTopButton:
//                mExpandLayout.move(0);
//                break;
//            case R.id.setCloseHeightButton:
//                mExpandLayout.setClosePosition(mExpandLayout.getCurrentPosition());
//                break;
//        }
    }
}
