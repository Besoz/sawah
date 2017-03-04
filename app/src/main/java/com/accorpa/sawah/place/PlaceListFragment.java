package com.accorpa.sawah.place;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.accorpa.sawah.R;
import com.accorpa.sawah.RecycleAdapterListener;
import com.accorpa.sawah.models.Place;
import com.labo.kaji.fragmentanimations.FlipAnimation;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceListFragment extends Fragment implements RecycleAdapterListener {

//    private PlacesAdapter adapter;
    private View listFragment;
//    private GridView mListView;

    private RecyclerView mRecyclerView;
    private PlaceRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private OnFragmentInteractionListener mListener;

    private ArrayList<Place> places;
    private boolean addLikeButton, specialPlaceLayout, addDeleteButton, showEmptyText;


    public PlaceListFragment() {
    }

    public PlaceListFragment(boolean addLikeButton, boolean specialPlaceLayout) {
        // Required empty public constructor

        this.addLikeButton = addLikeButton;
        this.specialPlaceLayout = specialPlaceLayout;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlaceListFragment.
     * @param addLikeButton
     * @param specialPlaceLayout
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceListFragment newInstance(boolean addLikeButton, boolean specialPlaceLayout) {
        PlaceListFragment fragment = new PlaceListFragment(addLikeButton, specialPlaceLayout);
        return fragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listFragment = inflater.inflate(R.layout.fragment_place_list, container, false);

        mRecyclerView = (RecyclerView) listFragment.findViewById(R.id.list);

//        mListView = (GridView) listFragment.findViewById(R.id.list);


//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
//                onPlaceSelected((Place) mListView.getAdapter().getItem(position));
//            }
//        });
        if(showEmptyText) {
            showHideEmptyText();
        }
        mLayoutManager = new LinearLayoutManager(getContext());

        GridLayoutManager specialGridLayoutManager = new GridLayoutManager(getContext(), 2);
        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return places.get(position).isSpecial() ? 2 : 1;
            }
        };
        specialGridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);

        GridLayoutManager normalGridLayoutManager = new GridLayoutManager(getContext(), 1);


        // specify an adapter (see also next example)
        mAdapter = new PlaceRecycleAdapter(getContext(), this, addLikeButton, specialPlaceLayout, addDeleteButton);


        mRecyclerView.setLayoutManager(specialPlaceLayout?
                specialGridLayoutManager : normalGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

//        adapter = new PlacesAdapter(getContext());
//        mListView.setAdapter(adapter);

        return listFragment;
    }
    @Override
    public void showHideEmptyText()
    {

            listFragment.findViewById(R.id.empty_list).setVisibility(View.VISIBLE);
    }

    public void setEmptyListText()
    {
        showEmptyText = true;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onPlaceSelected(Place place) {
        if (mListener != null) {
            mListener.onPlaceSelected(place);
        }
    }



    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return FlipAnimation.create(FlipAnimation.RIGHT, enter, 1000);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    @Override
    public void itemSelected(Object object) {
        mListener.onPlaceSelected((Place) object);
    }

    public void setShowDeleteButton(boolean showDeleteButton) {
        addDeleteButton = showDeleteButton;
        mAdapter.setShowDeletionButton(showDeleteButton);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPlaceSelected(Place place);
        ArrayList<Place> getPlaces();
    }

    public void setPlacesList(ArrayList<Place> arr){
//        adapter.setDataSource(arr);
//        adapter.notifyDataSetChanged();
        places = arr;
        mAdapter.setDataSource(arr);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onResume() {
        super.onResume();
        places = mListener.getPlaces();

        setPlacesList(places);
        Log.d("Fragment", "+++++++++++++++++=");
    }
}
