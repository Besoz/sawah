package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.accorpa.sawah.Handlers.Utils;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.labo.kaji.fragmentanimations.FlipAnimation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlacesMapFragment extends Fragment implements OnMapReadyCallback {

    private ArrayList<Place> places;

    private PlaceListFragment.OnFragmentInteractionListener mListener;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private ClusterManager<Place> mClusterManager;

    private SupportMapFragment mapFragment;

    private GoogleMap map;
    private Bitmap pin;


    public PlacesMapFragment() {
        // Required empty public constructor
    }

    public static PlacesMapFragment newInstance() {
        PlacesMapFragment fragment = new PlacesMapFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View fragView = inflater.inflate(R.layout.fragment_places_map, container, false);
//
        mPager = (ViewPager) fragView.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.d("map frag", position+"");
                animateCameraToPlace(places.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int px = Utils.getInstance().dpToPx(25, getContext().getResources().getDisplayMetrics());

        pin = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        pin = Utils.getInstance().resizeBitmapInDp(pin, px, px, getResources().getDisplayMetrics());

        return fragView;
    }

    private void animateCameraToPlace(Place place) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getPosition(),
                20));
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return FlipAnimation.create(FlipAnimation.RIGHT, enter, 1000);

    }

        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PlaceListFragment.OnFragmentInteractionListener) {
            mListener = (PlaceListFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) ((View) mPager).getLayoutParams();


        if(!places.isEmpty()){
            googleMap.setPadding(0, 0, 0, lp.bottomMargin+mPager.getHeight());
            setUpCluster(googleMap);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {

            Fragment frag = new ScreenSlidePlaceFragment(places.get(position), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPlaceSelected(places.get(position));
                }
            });

            return frag;
        }

        @Override
        public int getCount() {
            return places.size();
        }
    }

    private void setUpCluster(GoogleMap googleMap) {
        // Position the map.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place marker : places) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 100; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);

        mClusterManager = new ClusterManager<Place>(getContext(), googleMap);

        mClusterManager.setRenderer(new PlaceRenderer(getContext(), googleMap, mClusterManager));

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Place>() {
            @Override
            public boolean onClusterItemClick(Place place) {

                animateCameraToPlace(place);

//                todo hash<place id, place position in pager> O(1)

                for(int i =0 ; i < places.size(); i++){
                    if (TextUtils.equals(places.get(i).getPlaceID(), place.getPlaceID())){
                        mPager.setCurrentItem(i, true);
                    }
                }

                return true;
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<Place>() {
            @Override
            public boolean onClusterClick(Cluster<Place> cluster) {

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Place marker : cluster.getItems()) {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();

                int padding = 100; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                map.animateCamera(cu);

                return true;
            }
        });
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        addItems();
    }

    private void addItems() {

        for (int i = 0; i < places.size(); i++) {

            mClusterManager.addItem(places.get(i));
        }
    }



    private class PlaceRenderer extends DefaultClusterRenderer<Place> {

        private BitmapDescriptor bitmapDescriptor;

        public PlaceRenderer(Context context, GoogleMap map, ClusterManager<Place> clusterManager) {
            super(context, map, clusterManager);


            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(pin);
        }

        @Override
        protected void onBeforeClusterItemRendered(Place place, MarkerOptions markerOptions) {

            Log.d("map frag", mPagerAdapter.getItemPosition(place)+" "+mPager.getCurrentItem()+" "+place.getPalceName());

            markerOptions.icon(bitmapDescriptor);

        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Place> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            super.onBeforeClusterRendered(cluster, markerOptions);
        }
    }

}
