package com.sawah.sawah.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.URLUtil;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TableLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.arasthel.asyncjob.AsyncJob;
import com.bumptech.glide.Glide;
//import com.facebook.shimmer.ShimmerFrameLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
//import com.nikitakozlov.pury.Pury;
import com.nikitakozlov.pury.Pury;
import com.sawah.sawah.BaseActivity;
import com.sawah.sawah.BaseRequestStateListener;
import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.SharingHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.R;
import com.sawah.sawah.ServiceResponse;
import com.sawah.sawah.comment.CommentsAdapter;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.custom_views.CustomCheckBox;
import com.sawah.sawah.custom_views.CustomRotatingButton;
import com.sawah.sawah.custom_views.CustomTextView;
import com.sawah.sawah.models.Place;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kennyc.bottomsheet.BottomSheet;
import com.sawah.sawah.models.PlaceComment;
import com.sawah.sawah.models.WorkTime;

import java.io.IOException;
import java.util.Arrays;

import io.techery.properratingbar.ProperRatingBar;


public class PlaceDetailsActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener{


    private static final int VIEW_COMMENTS_COUNT = 7, IMAGES_OFFSCREEN_COUNT= 4;
    private static final float CHECK_IN_MAX_DISTANCE_METER = 1000;
    private static final float PIN_DIM = 60;
    private static final float MAP_ZOOM = 15;
    private CustomTextView bioTextView, titleArabic, titleEnglish, rating, tags;
    private ImageView placeImage;

    private ImageButton shareButtton, callButton, openSiteButton, checkInButton;

    private CustomCheckBox likeButton;

    private CustomButton addCommentButton, moreCommentsButton;

    private View commentsView;

    private LinearLayout linearLayout;


    private SimpleExpandableListAdapter mAdapter;
    ExpandableListView simpleExpandableListView;

    private Place place;

    private static final String NAME = "NAME";

    ExpandableRelativeLayout mExpandLayout;

    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;

    private Fragment[] imagesFragments;
    private int mCurrRotation = 0;

    private LinearLayout header;
    private BaseRequestStateListener checkInStateListner;


    private ShimmerFrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Pury.startProfiling( "Place details", "on Create", 0, 1);

        Pury.startProfiling( "Place details", "supercreate", 1, 1);

        super.onCreate(savedInstanceState);

        Pury.stopProfiling( "Place details", "supercreate", 1);


        Pury.startProfiling( "Place details", "extras", 1, 1);

        Utils.getInstance().changeStatusBarColor(this);
        removeNavigationDrawer();

        String placeJSONObject = (String) getIntent().getSerializableExtra("PlaceJSONObject");
        Pury.stopProfiling( "Place details", "extras", 1);

        Pury.startProfiling( "Place details", "parse", 1, 1);
        place = parseJSONPlace(placeJSONObject);
        Pury.stopProfiling( "Place details", "parse", 1);


        setToolbarTitle(place.getPalceNameArb());

        Pury.startProfiling( "Place details", "view", 1, 1);

        initializeViewPart1();

        Pury.stopProfiling( "Place details", "view", 1);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                lazyLoadHeavyView();
                container.stopShimmerAnimation();
            }
        }, 50);


        Pury.stopProfiling( "Place details", "on Create", 1);
    }

    private void lazyLoadHeavyView() {

        Pury.startProfiling( "lazy loading", "lazyLoadHeavyView", 0, 1);
        inflatePart2ViewStubs();

        initializeViewPart2();

        Pury.stopProfiling( "lazy loading", "lazyLoadHeavyView", 1);

    }

    private void inflatePart2ViewStubs() {

        ViewStub stub = (ViewStub) findViewById(R.id.extra_details);
        View part2 = stub.inflate();
        part2.setVisibility(View.VISIBLE);

        ViewStub frameStub = (ViewStub) findViewById(R.id.rating_stub);
        View frameView = frameStub.inflate();
        frameView.setVisibility(View.VISIBLE);

    }

    private void initializeViewPart2() {

        bioTextView.setText(place.getBio());
        bioTextView.setTextColor(Color.BLACK);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(place.getTags().length() > 0)
        {
            String tagsTitle = getString(R.string.tags);
            String tagsText = tagsTitle +  place.getTags();

            SpannableString sstagsText=  new SpannableString(tagsText);

            sstagsText.setSpan(new RelativeSizeSpan(1.2f), 0, tagsTitle.length(), 0); // set size

            sstagsText.setSpan(new ForegroundColorSpan(getResources()
                    .getColor(R.color.colorPrimary)), 0, tagsTitle.length(), 0);// set color

            tags = (CustomTextView) findViewById(R.id.tags_layout);
            tags.setVisibility(View.VISIBLE);
            tags.setText(sstagsText);
        }

        rating = (CustomTextView) findViewById(R.id.rating);
        rating.setText(place.getRatingID());

//        placeImage = (NetworkImageView) findViewById(R.id.imageView2);
//        String imageUrl= place.getImageLocation().replaceAll(" ", "%20");
//        placeImage.setImageUrl(imageUrl,
//                ServiceHandler.getInstance(this.getApplicationContext()).getImageLoader());


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(mPager, true);

        if(place.getImages().length > 1)
            tabLayout.setVisibility(View.VISIBLE);
        if(place.getImages().length == 0)
        {
            mPager.setBackgroundResource(R.drawable.demoitem);
        }


        mPager.setOffscreenPageLimit(IMAGES_OFFSCREEN_COUNT);

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

                    if(TextUtils.isEmpty(dataHandler.getUser().getFullName())
                            || TextUtils.isEmpty(dataHandler.getUser().getBirthDate())
                            || TextUtils.isEmpty(dataHandler.getUser().getSex())){
                        new MaterialDialog.Builder(PlaceDetailsActivity.this)
                                .title(R.string.alert_title)
                                .content(R.string.must_user_data_add_comment)
                                .positiveText(R.string.edit_your_profile)
                                .negativeText(R.string.cancel)
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

            ViewStub stub = (ViewStub) findViewById(R.id.comments_view);
            commentsView = stub.inflate();
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
                        NavigationHandler.getInstance()
                                .startCommentsListActivity(PlaceDetailsActivity.this,
                                        place.getCommentsCount(), place.getPlaceID(),
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

        checkInButton = (ImageButton) findViewById(R.id.checkin_button);

        checkInStateListner = new BaseRequestStateListener() {
            @Override
            public void failResponse(ServiceResponse response) {

                showProgress(false);
                showCheckInFail(response.getMessage());
            }

            @Override
            public void successResponse(ServiceResponse response) {
                showProgress(false);
                showCheckInSuccessful();
            }
        };

        if(DataHandler.getInstance(this).userExist())
        {
            checkInButton.setImageResource(R.drawable.check);
            checkInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if(DataHandler.getInstance(PlaceDetailsActivity.this).isUserLoacationSynced()){
                        Log.d("Request location", "Synced");

                        Location userLocation = DataHandler.getInstance(PlaceDetailsActivity.this)
                                .getUserLocation();

                        double dist  = Utils.getInstance().distance(userLocation.getLatitude(),
                                userLocation.getLongitude(), place.getLattitude(),
                                place.getLongitude());


                        if( dist < CHECK_IN_MAX_DISTANCE_METER){
                            String userID =
                                    DataHandler.getInstance(PlaceDetailsActivity.this).getUser().getUserID();

                            DataHandler.getInstance(PlaceDetailsActivity.this)
                                    .checkInPlace(place.getPlaceID(), userID, checkInStateListner,
                                            PlaceDetailsActivity.this);
                        }else {
                            showCheckAlert(dist);
                        }

                    }else{

                        mRequestingLocationUpdates = true;
                        if(!mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.connect();

                        }

                        else if(shouldShowExplaination()){
                            showPermissionDialog();
                        }


                        showWaitingLocationUpdate();

                    }
                }
            });
        }else{
            checkInButton.setImageResource(R.drawable.check_disabled);
        }

        final RelativeLayout arrow_layout = (RelativeLayout) findViewById(R.id.work_time);
        WorkTime[] workTimes = place.getWorkTimes();

        if(workTimes.length > 0){
            initializeWorkTimeLayout(workTimes, arrow_layout);
        }else{
            arrow_layout.setVisibility(View.GONE);
        }


        ProperRatingBar priceLevel = (ProperRatingBar) findViewById(R.id.price_level_bar);
        priceLevel.setRating(5 - (int) place.getPriceLevel());

    }

    private Place parseJSONPlace(String placeJSONObject) {

        Log.d("PlaceJson", placeJSONObject);

        ObjectMapper mapper = new ObjectMapper();

        Place place = null;
        try {
            place = mapper.readValue(placeJSONObject, Place.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return place;
    }


    private void initializeViewPart1()
    {
        DataHandler.getInstance(this).loadPlaceFromDataBase(place);

        titleArabic = (CustomTextView) findViewById(R.id.title_ar);
        titleArabic.setText(place.getPalceNameArb());

        titleEnglish = (CustomTextView) findViewById(R.id.title_en);
        titleEnglish.setText(place.getPalceNameEng());

        bioTextView = (CustomTextView) findViewById(R.id.bio_text);

        bioTextView.setTextColor(ContextCompat.getColor(this, R.color.grey));

        container =
                (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
        container.startShimmerAnimation();

        SpannableString str = new SpannableString(bioTextView.getText());
        str.setSpan(new BackgroundColorSpan(ContextCompat.getColor(this, R.color.grey)), 0, str.length(), 0);
        bioTextView.setText(str);

        imagesFragments = new Fragment[place.getPlaceImages().length];

        for (int i = 0; i < imagesFragments.length; i++){
            imagesFragments[i] = new ScreenSlideImageFragment(place.getPlaceImages()[i].getImageURL());
        }

        mPager = (ViewPager) findViewById(R.id.imagePager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

//

    }

    private void initializeWorkTimeLayout(WorkTime[] workTimes, RelativeLayout arrow_layout) {

        final CustomRotatingButton arrow = (CustomRotatingButton) findViewById(R.id.arrow);
        final ExpandableLinearLayout body
                = (ExpandableLinearLayout) findViewById(R.id.body);
        header = (LinearLayout) findViewById(R.id.header);

        CustomTextView headerTitle = (CustomTextView) findViewById(R.id.work_time_title);
        CustomTextView headerTitleTime = (CustomTextView) findViewById(R.id.work_time_title_time);

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

        final TableLayout tableLayout = (TableLayout) findViewById(R.id.work_time_table);

        for(int i = 0; i < workTimes.length; i++){
            tableLayout.addView(adapter.getView(i, null, null));
        }

        body.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                tableLayout.invalidate();
                tableLayout.refreshDrawableState();
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


        arrow_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrow.rotate();
                body.toggle();
            }
        });

    }

    private void showCheckInFail(String message) {

        new MaterialDialog.Builder(PlaceDetailsActivity.this)
                .title(R.string.check_in_fail_title_text)
                .content(message)
                .positiveText(R.string.agree)
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    private void showCheckInSuccessful(){
        new MaterialDialog.Builder(PlaceDetailsActivity.this)
                .title(R.string.check_in_done_title_text)
                .content(R.string.check_in_done_text)
                .positiveText(R.string.agree)
                .autoDismiss(true)
                .titleGravity(GravityEnum.CENTER)
                .contentGravity(GravityEnum.CENTER)
                .show();
    }

    private void showWaitingLocationUpdate() {

    }

    @Override
    public void onResume() {
        Log.d("Request location", "onResume 2");

        super.onResume();
        Log.d("Request location", "onResume 2");

        Log.d("Location", mRequestingLocationUpdates+" uuuuuuuuuuuuuuuuuuu" );

        if (mRequestingLocationUpdates){
            mGoogleApiClient.connect();
            Log.d("Location", mRequestingLocationUpdates+" in " );

        }

//        List<Place> places = Place.find(Place.class, "point_id = ?", this.place.getPlaceID());
//        if(places.size() > 0){
//            likeButton.setChecked();
//        }else{
//            likeButton.setUnChecked();
//
//        }

        DataHandler.getInstance(this).incrementPlaceVisits(place.getPlaceID());


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_place_details;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {


        Log.d("Google Map", "Map ready");
//        todo refactor this part
        Bitmap pin = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        pin = Utils.getInstance().resizeBitmapInDp(pin, PIN_DIM, PIN_DIM, getResources().getDisplayMetrics());
        final BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(pin);


        if(place.getPlaceLocations().length < 2){
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(place.getPosition()).icon(bitmapDescriptor));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getPosition(), MAP_ZOOM));
        }else{

//           todo wait on OnGlobalLayoutListener
//            Note that this does not guarantee that the map has undergone layout. Therefore, the map's size may not have been determined by the time the callback method is called. If you need to know the dimensions or call a method in the API that needs to know the dimensions, get the map's View and register an ViewTreeObserver.OnGlobalLayoutListener as well.
//
//                    Do not chain the OnMapReadyCallback and OnGlobalLayoutListener listeners, but instead register and wait for both callbacks independently, since the callbacks can be fired in any order.
//
//            As an example, if you want to update the map's camera using a LatLngBounds without dimensions, you should wait until both OnMapReadyCallback and OnGlobalLayoutListener have completed. Otherwise there is a race condition that could trigger an IllegalStateException.

            final Bitmap finalPin = pin;
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();

                    for (int i = 0; i < place.getPlaceLocations().length; i++) {

                        LatLng placeLatLng = place.getPlaceLocations()[i].getLaLng();

                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(placeLatLng).icon(bitmapDescriptor));

                        builder.include(placeLatLng);
                    }

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),
                            finalPin.getHeight()));
                }
            });
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                openSheet(marker.getPosition());
                return true;
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                openSheet(null);
            }
        });

        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.getUiSettings().setAllGesturesEnabled(false);


        Log.d("Google Map", "Marker added");

    }

    protected void openSheet(LatLng position)
    {
        double lat = place.getLattitude(), lng = place.getLongitude();
        if(position != null){
            lat = position.latitude;
            lng = position.longitude;
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView;
        try {
            contentView = inflater.inflate(R.layout.bottom_sheet, null, false);
            new BottomSheet.Builder(PlaceDetailsActivity.this)
//                .setSheet(R.menu.map_application_options)
                    .setView(contentView)
                    .setTitle(R.string.open_map_with_text)
                    .setNegativeButton(R.string.close)
                    .show();
            CustomButton map = (CustomButton) contentView.findViewById(R.id.open_maps);
            final double finalLng = lng;
            final double finalLat = lat;
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingHandler.getInstance().openMapIntent(PlaceDetailsActivity.this,
                            finalLat, finalLng);
                }
            });
            CustomButton uber = (CustomButton) contentView.findViewById(R.id.open_uber);
            uber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingHandler.getInstance().requestUberRide(PlaceDetailsActivity.this,
                            finalLat, finalLng, place.getPalceNameEng());
                }
            });
        }
        catch (InflateException e)
        {
            System.out.println(e.getMessage());
        }
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
                        place.getLattitude(), place.getLongitude(), place.getPalceNameEng());
                return true;
            default:
                return false;
        }
    }

    public BitmapDescriptor getMakerBitmapDescriptor() {

//        int px = Utils.getInstance().dpToPx(PIN_DIM, getResources().getDisplayMetrics());

        Bitmap pin = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        pin = Utils.getInstance().resizeBitmapInDp(pin, PIN_DIM, PIN_DIM, getResources().getDisplayMetrics());

        return  BitmapDescriptorFactory.fromBitmap(pin);
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

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

            Glide.clear( ((ScreenSlideImageFragment) object).getNetworkImageView() );


        }
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.back_tool_bar;
    }


    @Override
    public void onLocationChanged(final Location location) {
        super.onLocationChanged(location);

        DataHandler.getInstance(PlaceDetailsActivity.this).assertUserLoacationSynced(location);
        stopLocationUpdates();


        double dist  = Utils.getInstance().distance(location.getLatitude(),
                location.getLongitude(), place.getLattitude(),
                place.getLongitude());

        if( dist < CHECK_IN_MAX_DISTANCE_METER){

            final String userID = DataHandler.getInstance(this).getUser().getUserID();

            showProgress(true);
            DataHandler.getInstance(this)
                    .syncDefaultCityAndLocation(DataHandler.getInstance(this).getDefaultCityID(),
                            location.getLatitude() + "", location.getLongitude() + "",userID
                            ,
                            new BaseRequestStateListener() {
                                @Override
                                public void failResponse(ServiceResponse response) {
                                    showProgress(false);

                                }

                                @Override
                                public void successResponse(ServiceResponse response) {


                                    DataHandler.getInstance(PlaceDetailsActivity.this)
                                            .checkInPlace(place.getPlaceID(), userID,
                                                    checkInStateListner, PlaceDetailsActivity.this);

                                }
                            });
        }else{
            showCheckAlert(dist);
        }



    }

    private void showCheckAlert(double dist) {

        DialogHelper.getInstance().showAlert(PlaceDetailsActivity.this,
                getString(R.string.cant_checkin)+ " " + ((int)(dist/1000)) + "km");
    }


}
