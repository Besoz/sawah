package com.accorpa.sawah.place;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.ListActivity;
import com.accorpa.sawah.R;
import com.accorpa.sawah.models.Place;
import com.accorpa.sawah.models.PlaceComment;
import com.accorpa.sawah.models.PlaceImage;

import java.util.List;


public class FavouritePlacesList extends ListActivity {

    private ListView mListView;
    private int nextResID, currentResID;
    private FavouritePlacesAdapter adapter;
    private boolean showDeleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nextResID = R.drawable.cross;
        currentResID = R.drawable.ic_action_edit;

        mListView = (ListView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Place selectedPlace= (Place) mListView.getAdapter().getItem(position);

                NavigationHandler.getInstance().startPlaceDetailsActivity(FavouritePlacesList.this, selectedPlace);
            }
        });
        //        todo move to datahandeler

        List<Place> places = (List<Place>) Place.listAll(Place.class);
        for(int i = 0; i < places.size(); i++){
            List<PlaceImage> images = PlaceImage.find(PlaceImage.class, "place = ?",
                    places.get(i).getId()+"");

            List<PlaceComment> comments = PlaceComment.find(PlaceComment.class, "place = ?",
                    places.get(i).getId()+"");

            places.get(i).setPlaceImages(images.toArray(new PlaceImage[images.size()]));

            places.get(i).setComments(comments.toArray(new PlaceComment[comments.size()]));

        }
        adapter = new FavouritePlacesAdapter(this, places);
        mListView.setAdapter(adapter);
//        showProgress(true);

        Log.d("fav plcs", "on create");
    }

    @Override
    protected void onResume() {
        super.onResume();

//        todo move to datahandeler
        List<Place> places = (List<Place>) Place.listAll(Place.class);
        for(int i = 0; i < places.size(); i++){
            List<PlaceImage> images = PlaceImage.find(PlaceImage.class, "place = ?",
                    places.get(i).getId()+"");

            List<PlaceComment> comments = PlaceComment.find(PlaceComment.class, "place = ?",
                    places.get(i).getId()+"");

            places.get(i).setPlaceImages(images.toArray(new PlaceImage[images.size()]));

            places.get(i).setComments(comments.toArray(new PlaceComment[comments.size()]));

        }
        adapter.setDataSouce(places);
        adapter.notifyDataSetChanged();

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_favourite_places_list;
    }

//    public void recieveFavouritePlacesList(List<Place> places) {
//        showProgress(false);
//        mListView.setAdapter(new FavouritePlacesAdapter(this, places.toArray(new Place[places.size()])));
//
//    }

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

            adapter.setShowDeleteButton(showDeleteButton);
            adapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getActionBarMenuLayout() {
        return R.menu.favourite_places_list;
    }
}
