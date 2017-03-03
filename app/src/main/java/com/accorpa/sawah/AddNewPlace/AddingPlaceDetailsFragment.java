package com.accorpa.sawah.AddNewPlace;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.accorpa.sawah.R;
import com.accorpa.sawah.custom_views.CustomButton;
import com.accorpa.sawah.custom_views.CustomEditText;
import com.accorpa.sawah.models.Place;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddingPlaceDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddingPlaceDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddingPlaceDetailsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Place place;
    
    private CustomEditText placeNameEditText, placeDescriptioEditText;
    
    private ImageButton selectPhotosButton;
    private CustomButton sendButton;

    public AddingPlaceDetailsFragment() {
        // Required empty public constructor
    }
    
    public static AddingPlaceDetailsFragment newInstance() {
        AddingPlaceDetailsFragment fragment = new AddingPlaceDetailsFragment();
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
        View view =  inflater.inflate(R.layout.fragment_adding_place_details, container, false);
        placeNameEditText = (CustomEditText) view.findViewById(R.id.place_name_edittext);
        placeNameEditText.setText(place.getPalceName());
        
        placeDescriptioEditText = (CustomEditText) view.findViewById(R.id.place_desciption_edittext);
        
        selectPhotosButton = (ImageButton) view.findViewById(R.id.places_photos_button);
        selectPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.selectPlaceImages();
            }
        });

        sendButton = (CustomButton) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAddNewPlace();
            }
        });

        
        return view;
    }

    private void attemptAddNewPlace() {
        // Reset errors.
        placeDescriptioEditText.setError(null);
        placeNameEditText.setError(null);

        // Store values at the time of the login attempt.
        String nameStr = placeNameEditText.getText().toString();
        String desciptionStr = placeDescriptioEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(desciptionStr)){
            placeDescriptioEditText.setError(getString(R.string.error_field_required));
            focusView = placeDescriptioEditText;
            cancel = true;
        }

        if(!mListener.isImageSelected()){
//            selectPhotosButton.setError(getString(R.string.error_must_select_one_image));
            Log.d("add new Place", "imge error");
            focusView = selectPhotosButton;
            cancel = true;
        }

        if (TextUtils.isEmpty(nameStr)) {
            placeNameEditText.setError(getString(R.string.error_field_required));
            focusView = placeNameEditText;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {
            Log.d("latlong",place.getLongitude()+ " "+ place.getLongitude());

            mListener.addNewPlace(place);
        }

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

    public void setPlace(Place place) {
        this.place = place;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void selectPlaceImages();

        void addNewPlace(Place place);

        boolean isImageSelected();
    }
}
