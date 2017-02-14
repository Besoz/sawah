package com.accorpa.sawah.place;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;
import com.labo.kaji.fragmentanimations.FlipAnimation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceListFragment extends Fragment {

    private PlacesAdapter adapter;
    private View listFragment;
    private GridView mListView;

    private OnFragmentInteractionListener mListener;

    public PlaceListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlaceListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceListFragment newInstance() {
        PlaceListFragment fragment = new PlaceListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        listFragment = inflater.inflate(R.layout.fragment_place_list, container, false);

        mListView = (GridView) listFragment.findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                onPlaceSelected((Place) mListView.getAdapter().getItem(position));
            }
        });

        adapter = new PlacesAdapter(getContext());
        mListView.setAdapter(adapter);

        return listFragment;
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
        Place[] getPlaces();
    }

    public void setPlacesList(Place[] arr){
        adapter.setDataSource(arr);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();

        setPlacesList(mListener.getPlaces());
        Log.d("Fragment", "+++++++++++++++++=");
    }
}
