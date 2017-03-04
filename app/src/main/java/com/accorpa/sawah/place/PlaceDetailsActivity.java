package com.accorpa.sawah.place;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.webkit.URLUtil;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import com.accorpa.sawah.BaseActivity;
import com.accorpa.sawah.CommentActivity;
import com.accorpa.sawah.CommentsAdapter;
import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.ServiceHandler;
import com.accorpa.sawah.Handlers.SharingHandler;
import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomCheckBox;
import com.accorpa.sawah.custom_views.CustomRotatingButton;
import com.accorpa.sawah.custom_views.CustomTextView;
import com.accorpa.sawah.custom_views.ExpandArrow;
import com.accorpa.sawah.models.Place;
import com.accorpa.sawah.models.PlaceComment;
import com.accorpa.sawah.models.WorkTime;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.toolbox.NetworkImageView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.techery.properratingbar.ProperRatingBar;


public class PlaceDetailsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener{


    private static final int VIEW_COMMENTS_COUNT = 2, IMAGES_OFFSCREEN_COUNT= 4;
    private CustomTextView bioTextView, titleArabic, titleEnglish, rating;
    private NetworkImageView placeImage;

    private ImageButton shareButtton, callButton, openSiteButton, checkInButton;

    private CustomCheckBox likeButton;

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

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    private Fragment[] imagesFragments;
    private int mCurrRotation = 0;

    private LinearLayout header;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);
        removeNavigationDrawer();
        String placeJSONObject = (String) getIntent().getSerializableExtra("PlaceJSONObject");

        ObjectMapper mapper = new ObjectMapper();

        try {
            place = mapper.readValue(placeJSONObject, Place.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setToolbarTitle(place.getPalceNameArb()                                                                                                     );

        DataHandler.getInstance(this).loadPlaceFromDataBase(place);

//       todo sperate view intialization in other methods

        bioTextView = (CustomTextView) findViewById(R.id.bio_text);
        bioTextView.setText(place.getBio());

        titleArabic = (CustomTextView) findViewById(R.id.title_ar);
        titleArabic.setText(place.getPalceNameArb());

        titleEnglish = (CustomTextView) findViewById(R.id.title_en);
        titleEnglish.setText(place.getPalceNameEng());

        rating = (CustomTextView) findViewById(R.id.rating);
        rating.setText(place.getRatingID());

//        placeImage = (NetworkImageView) findViewById(R.id.imageView2);
//        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");
//        placeImage.setImageUrl(imageUrl,
//                ServiceHandler.getInstance(this.getApplicationContext()).getImageLoader());

        imagesFragments = new Fragment[place.getPlaceImages().length];

        for (int i = 0; i < imagesFragments.length; i++){
            imagesFragments[i] = new ScreenSlideImageFragment(place.getPlaceImages()[i].getImageURL());
        }

        mPager = (ViewPager) findViewById(R.id.imagePager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
//        mPager.setOffscreenPageLimit(IMAGES_OFFSCREEN_COUNT);

        shareButtton = (ImageButton) findViewById(R.id.share_button);
        shareButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharingHandler.getInstance().share(PlaceDetailsActivity.this,
                        PlaceDetailsActivity.this.getString(R.string.share_msg_a)+" "+
                        place.getPalceNameArb()+" "+
                                PlaceDetailsActivity.this.getString(R.string.share_msg_b) );
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

                    if(TextUtils.isEmpty(dataHandler.getUser().getFullName())){
                        new MaterialDialog.Builder(PlaceDetailsActivity.this)
                                .title(R.string.user_data)
                                .content(R.string.must_user_data_add_comment)
                                .positiveText(R.string.agree)
                                .negativeText(R.string.close)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog,
                                                        @NonNull DialogAction which) {
                                        NavigationHandler.getInstance().
                                                startEditProfileActivity(PlaceDetailsActivity.this);
                                    }
                                }).autoDismiss(true)
                                .titleGravity(GravityEnum.CENTER)
                                .contentGravity(GravityEnum.CENTER)
                                .show();

                    }else{
                        NavigationHandler.getInstance().startCommentActivity(PlaceDetailsActivity.this,
                            place.getPlaceID());
                    }
                }else{
                    new MaterialDialog.Builder(PlaceDetailsActivity.this)
                            .title(R.string.action_sign_in_short)
                            .content(R.string.must_sign_in_to_add_comment)
                            .positiveText(R.string.agree)
                            .negativeText(R.string.close)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog,
                                                    @NonNull DialogAction which) {
                                    NavigationHandler.getInstance().
                                            startLoginActivity(PlaceDetailsActivity.this);
                                }
                            }).autoDismiss(true)
                            .titleGravity(GravityEnum.CENTER)
                            .contentGravity(GravityEnum.CENTER)
                            .show();
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

        likeButton = (CustomCheckBox) this.findViewById(R.id.like_button);
        likeButton.setVisibility(DataHandler.getInstance(this).userExist() ? View.VISIBLE : View.GONE);
        likeButton.setBackgroundResIDs(R.drawable.heart_active, R.drawable.heart);

        if(place.isFavourite()){
            likeButton.setChecked();
        }else{
            likeButton.setUnChecked();
        }

        likeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DataHandler.getInstance(PlaceDetailsActivity.this).togglePlaceFavourite(place);
                likeButton.toggleState();
            }
        });

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



        final CustomRotatingButton arrow = (CustomRotatingButton) findViewById(R.id.arrow);
        final ExpandableRelativeLayout body
                = (ExpandableRelativeLayout) findViewById(R.id.body);
        header = (LinearLayout) findViewById(R.id.header);

        CustomTextView headerTitle = (CustomTextView) findViewById(R.id.work_time_title);
        CustomTextView headerTitleTime = (CustomTextView) findViewById(R.id.work_time_title_time);

        WorkTime[] workTimes = place.getWorkTimes();
        Arrays.sort(workTimes);

        int dayNo = Utils.getInstance().getTodayDayNumber();
        String time = "";
        if(workTimes.length >= dayNo){
            time = workTimes[dayNo -1].getTime();
        }
        if (TextUtils.isEmpty(time)) {
            headerTitle.setText(getString(R.string.work_time));
        }else{
            headerTitleTime.setText(time);
            headerTitle.setText(getString(R.string.work_time_today));
        }
        WorkTimeAdapter adapter =  new WorkTimeAdapter(this, workTimes);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.work_time_table);

        for(int i = 0; i < workTimes.length; i++){
            tableLayout.addView(adapter.getView(i, null, null));
        }

        body.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                header.setVisibility(header.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                header.setVisibility(header.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            }
        });


        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow.rotate();
                body.toggle();
            }
        });

        ProperRatingBar priceLevel = (ProperRatingBar) findViewById(R.id.price_level_bar);
        priceLevel.setRating(5 - (int) place.getPriceLevel());

    }



    @Override
    protected void onResume() {
        super.onResume();

        List<Place> places = Place.find(Place.class, "point_id = ?", this.place.getPlaceID());
        if(places.size() > 0){
            likeButton.setChecked();
        }else{
            likeButton.setUnChecked();

        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_place_details;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        todo refactor this part
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin);

        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, 100, 100), Matrix.ScaleToFit.CENTER);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        googleMap.addMarker(new MarkerOptions().position(place.getPosition()).icon(bitmapDescriptor));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getPosition()));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                new BottomSheet.Builder(PlaceDetailsActivity.this)
                        .setSheet(R.menu.map_application_options)
                        .setTitle(R.string.open_map_with_text)
                        .setNegativeButton(R.string.close)
                        .setListener(new BottomSheetListener() {
                            @Override
                            public void onSheetShown(@NonNull BottomSheet bottomSheet) {

                            }

                            @Override
                            public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem) {
                                onMenuItemClick(menuItem);
                            }

                            @Override
                            public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @DismissEvent int i) {

                            }
                        }).setStyle(R.style.MyBottomSheetStyle)
                        .show();
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


//    public void showMenu(View v) {
//        PopupMenu popup = new PopupMenu(this, v);
//
//        // This activity implements OnMenuItemClickListener
//        popup.setOnMenuItemClickListener(this);
//        popup.inflate(R.menu.map_application_options);
//        popup.show();
//    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_google_maps:

                SharingHandler.getInstance().openMapIntent(PlaceDetailsActivity.this,
                        place.getLattitude(), place.getLongitude());

                return true;
            case R.id.request_uber:
                SharingHandler.getInstance().requestUberRide(PlaceDetailsActivity.this,
                        place.getLattitude(), place.getLongitude());
                return true;
            default:
                return false;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {

            return imagesFragments[position];
        }

        @Override
        public int getCount() {
            return place.getPlaceImages().length;
        }
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.back_tool_bar;
    }

}
