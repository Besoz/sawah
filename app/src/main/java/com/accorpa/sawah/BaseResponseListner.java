package com.accorpa.sawah;

import android.util.Log;

import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by root on 16/02/17.
 */

public class BaseResponseListner implements Response.Listener<JSONObject> {

    boolean statusSuccess;

    @Override
    public void onResponse(JSONObject jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();

        Log.d("gg", jsonResponse.toString());
        ServiceResponse response = null;
        try {
            response = mapper.readValue(jsonResponse.toString(), ServiceResponse.class);

            if(response.isStatusSuccess()){
                statusSuccess = true;
            }else{
                statusSuccess = false;
            }

        } catch (IOException e) {

        }
    }

    public boolean isStatusSuccess(){
        return statusSuccess;
    }
}
