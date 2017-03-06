package com.sawah.sawah;

import android.util.Log;

import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by root on 16/02/17.
 */

public class BaseResponseListener implements Response.Listener<JSONObject> {

    boolean statusSuccess;

    private ServiceResponse response;

    private BaseRequestStateListener onResponseListner;

    @Override
    public void onResponse(JSONObject jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();

        Log.d("gg", jsonResponse.toString());
        response = null;
        try {
            response = mapper.readValue(jsonResponse.toString(), ServiceResponse.class);

            if(response.isStatusSuccess()){
                statusSuccess = true;

                if(onResponseListner!=null)
                    onResponseListner.successResponse(response);

            }else{
                statusSuccess = false;
                if(onResponseListner!=null)
                    onResponseListner.failResponse(response);

            }

        } catch (IOException e) {

        }
    }

    public boolean isStatusSuccess(){
        return statusSuccess;
    }

    public ServiceResponse getResponse(){
        return response;
    }

    public void setOnResponseListner(BaseRequestStateListener onResponseListner) {
        this.onResponseListner = onResponseListner;
    }
}
