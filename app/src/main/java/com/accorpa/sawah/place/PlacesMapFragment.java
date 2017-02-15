package com.accorpa.sawah.place;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.accorpa.sawah.Handlers.GoogleMapHelper;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.labo.kaji.fragmentanimations.FlipAnimation;

import java.util.Arrays;
import java.util.Set;

public class PlacesMapFragment extends Fragment implements OnMapReadyCallback {

    private Place[] places;

    private PlaceListFragment.OnFragmentInteractionListener mListener;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private ClusterManager<Place> mClusterManager;

    private SupportMapFragment mapFragment;

    private GoogleMap map;


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
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(places[position].getPosition(),
                        20));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return fragView;
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

    public void setPlaces(Place[] places) {
        this.places = places;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpClusterer(googleMap);
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {

            Fragment frag = new ScreenSlidePlaceFragment(places[position], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPlaceSelected(places[position]);
                }
            });

            return frag;
        }

        @Override
        public int getCount() {
            return places.length;
        }
    }

    private void setUpClusterer(GoogleMap googleMap) {
        // Position the map.
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Place marker : places) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);

        mClusterManager = new ClusterManager<Place>(getContext(), googleMap);

        mClusterManager.setRenderer(new PlaceRenderer(getContext(), googleMap, mClusterManager));

        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);

        addItems();
    }

    private void addItems() {

        for (int i = 0; i < places.length; i++) {
            mClusterManager.addItem(places[i]);
        }
    }



    private class PlaceRenderer extends DefaultClusterRenderer<Place> {

        private BitmapDescriptor bitmapDescriptor;

        public PlaceRenderer(Context context, GoogleMap map, ClusterManager<Place> clusterManager) {
            super(context, map, clusterManager);


            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin);

            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0,
                    0, 100, 100), Matrix.ScaleToFit.CENTER);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

            bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
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
