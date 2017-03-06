package com.sawah.sawah.place;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sawah.sawah.Handlers.DataHandler;
import com.sawah.sawah.Handlers.NavigationHandler;
import com.sawah.sawah.Handlers.Utils;
import com.sawah.sawah.R;
import com.sawah.sawah.custom_views.CustomButton;
import com.sawah.sawah.models.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FavouritePlacesList extends BasePlacesListActivity {

//    private ListView mListView;
    private int nextResID, currentResID;
//    private FavouritePlacesAdapter adapter;
    private boolean showDeleteButton;
    private MenuItem editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        specialPlaceLayout = false;
        addLikeButton = false;

        super.onCreate(savedInstanceState);
        Utils.getInstance().changeStatusBarColor(this);

        nextResID = R.drawable.cross;
        currentResID = R.drawable.editfav;

        if(getPlaces().size() == 0) {
            mapToggleButton.setVisibility(View.GONE);
            listFragment.setEmptyListText();
            if(editButton != null)
                editButton.setVisible(false);
        }
//
//
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
//                Place selectedPlace= (Place) mListView.getAdapter().getItem(position);
//
//                NavigationHandler.getInstance().startPlaceDetailsActivity(FavouritePlacesList.this, selectedPlace);
//            }
//        });
        //        todo move to datahandeler

//        List<Place> places = DataHandler.getInstance(this).loadAllPlaceFromDataBase();
//
//        adapter = new FavouritePlacesAdapter(this, places);
//        mListView.setAdapter(adapter);


//        showProgress(true);

        Log.d("fav plcs", "on create");
    }

    @Override
    public void onResume() {
        super.onResume();

//        todo move to datahandeler

//        List<Place> places = DataHandler.getInstance(this).loadAllPlaceFromDataBase();
//
//        adapter.setDataSouce(places);
//        adapter.notifyDataSetChanged();

    }

//    @Override
//    protected int getLayoutResourceId() {
//        return R.layout.activity_favourite_places_list;
//    }

//    public void recieveFavouritePlacesList(List<Place> places) {
//        showProgress(false);
//        mListView.setAdapter(new FavouritePlacesAdapter(this, places.toArray(new Place[places.size()])));
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        editButton = menu.findItem(R.id.toggle_deletion_activation);
        if(getPlaces().size() == 0)
            editButton.setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.toggle_deletion_activation) {
            item.setIcon(nextResID);

            int temp = nextResID;
            nextResID = currentResID;
            currentResID = temp;

            showDeleteButton = !showDeleteButton;

            if(listFragment.isVisible()){
                listFragment.setShowDeleteButton(showDeleteButton);
            }else{
                listFragment = showListFragment();
                listFragment.setShowDeleteButton(showDeleteButton);

                mapToggleButton.setText(R.string.view_map);
                mapView = false;
            }
            if(showDeleteButton)
            {
                findViewById(R.id.delete_text).setVisibility(View.VISIBLE);
            }
            else
                findViewById(R.id.delete_text).setVisibility(View.GONE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.favourite_places_list;
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.favourites);
    }

    @Override
    public ArrayList<Place> getPlaces() {

        places = DataHandler.getInstance(this).loadAllPlaceFromDataBase();
        return places;
    }

}
