package com.accorpa.sawah.place;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = (ListView) findViewById(R.id.list);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                Place selectedPlace= (Place) mListView.getAdapter().getItem(position);

                NavigationHandler.getInstance().startPlaceDetailsActivity(FavouritePlacesList.this, selectedPlace);
            }
        });
        List<Place> places = (List<Place>) Place.listAll(Place.class);
        for(int i = 0; i < places.size(); i++){
            List<PlaceImage> images = PlaceImage.find(PlaceImage.class, "place = ?",
                    places.get(i).getId()+"");

            List<PlaceComment> comments = PlaceComment.find(PlaceComment.class, "place = ?",
                    places.get(i).getId()+"");

            places.get(i).setPlaceImages(images.toArray(new PlaceImage[images.size()]));

            places.get(i).setComments(comments.toArray(new PlaceComment[comments.size()]));

        }
        Log.d("Loaded List", places.get(0).getId()+"");

        mListView.setAdapter(new PlacesAdapter(this, places.toArray(new Place[places.size()])));
//        showProgress(true);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_favourite_places_list;
    }

    public void recieveFavouritePlacesList(List<Place> places) {
        showProgress(false);
        mListView.setAdapter(new PlacesAdapter(this, places.toArray(new Place[places.size()])));

    }
}
