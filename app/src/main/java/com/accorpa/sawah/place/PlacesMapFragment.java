package com.accorpa.sawah.place;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;

public class PlacesMapFragment extends Fragment {

    private Place[] places;

    private PlaceListFragment.OnFragmentInteractionListener mListener;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;


    public PlacesMapFragment() {
        // Required empty public constructor
        this.places = places;
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

        mPager = (ViewPager) fragView.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        return fragView;




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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePlaceFragment(places[position]);
        }

        @Override
        public int getCount() {
            return places.length;
        }
    }

}
