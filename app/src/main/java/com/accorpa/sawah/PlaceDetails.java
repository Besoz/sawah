package com.accorpa.sawah;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.accorpa.sawah.custom_views.CustomTextView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class PlaceDetails extends BaseActivity {


    private CustomTextView bioTextView;


    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place_details);


        String placeJSONObject = (String) getIntent().getSerializableExtra("PlaceJSONObject");

        ObjectMapper mapper = new ObjectMapper();

        try {
            place = mapper.readValue(placeJSONObject, Place.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        bioTextView = (CustomTextView) findViewById(R.id.bio_textview);
        bioTextView.setText(place.getBio());

    }


    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_place_details;
    }

}
