package com.sawah.sawah.AddNewPlace;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.sawah.sawah.BitmapImage;
import com.sawah.sawah.Handlers.DialogHelper;
import com.sawah.sawah.R;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.custom_views.CustomEditText;
import com.sawah.sawah.models.Place;
import com.sawah.sawah.place.PlaceRecycleAdapter;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

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

    private RecyclerView imageRecyclerView;
    private ImageRecycleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        imageRecyclerView = (RecyclerView) view.findViewById(R.id.image_recycle_view);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new ImageRecycleAdapter(getContext());
        imageRecyclerView.setLayoutManager(layoutManager);
        imageRecyclerView.setAdapter(mAdapter);


        return view;
    }

    private void attemptAddNewPlace() {
        // Reset errors.
        placeDescriptioEditText.setError(null);
        placeNameEditText.setError(null);

        // Store values at the time of the login attempt.
        String nameStr = placeNameEditText.getText().toString();
        String descriptionString = placeDescriptioEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(descriptionString)){
            placeDescriptioEditText.setError(getString(R.string.please_add_place_description));
            focusView = placeDescriptioEditText;
            cancel = true;
        }

        if(!mListener.isImageSelected()){
//            selectPhotosButton.setError(getString(R.string.error_must_select_one_image));
            Log.d("add new Place", "imge error");
            focusView = selectPhotosButton;
            cancel = true;

            showSelectImageDialog();
        }

        if (TextUtils.isEmpty(nameStr)) {
            placeNameEditText.setError(getString(R.string.enter_place_name));
            focusView = placeNameEditText;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {
            Log.d("latlong",place.getLongitude()+ " "+ place.getLongitude());
            place.setBiography(descriptionString);
            mListener.addNewPlace(place);
        }

    }

    private void showSelectImageDialog() {
        DialogHelper.getInstance().showSelectImageDialog(getContext());
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

    public void showSelectedImages(ArrayList<BitmapImage> bitmaps){
        mAdapter.setDataSource(bitmaps);
    }

    private class ImageRecycleAdapter extends RecyclerView.Adapter<ImageRecycleAdapter.ViewHolder> {

        private Context mContext;
        //    private LayoutInflater mInflater;
        private View convertView;
        private ArrayList<BitmapImage> mDataSource;

        public ImageRecycleAdapter(Context context) {
            super();
            mDataSource = new ArrayList<>();
        }

        public void setDataSource(ArrayList<BitmapImage> bitmaps){
            this.mDataSource = bitmaps;
            notifyDataSetChanged();
        }


        @Override
        public ImageRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_item, parent, false);
            ImageRecycleAdapter.ViewHolder vh = new ImageRecycleAdapter.ViewHolder(convertView);
            return vh;
        }

        @Override
        public void onBindViewHolder(ImageRecycleAdapter.ViewHolder holder, int position) {
            holder.imageView.setImageBitmap(mDataSource.get(position).getBitmap());
        }


        @Override
        public int getItemCount() {
            return mDataSource.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView imageView;

            public ViewHolder(View v){
                super(v);

                this.imageView = (ImageView) v.findViewById(R.id.image);

            }
        }
    }
}
