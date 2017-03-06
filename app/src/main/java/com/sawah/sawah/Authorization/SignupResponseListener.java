package com.sawah.sawah.Authorization;

import android.util.Log;

import com.sawah.sawah.ServiceResponse;
import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by root on 15/01/17.
 */
public class SignupResponseListener implements Response.Listener<JSONObject> {
    private SignupActivity signupActivity;

    public SignupResponseListener(SignupActivity signupActivity) {
        this.signupActivity = signupActivity;
    }

    @Override
    public void onResponse(JSONObject jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();

        Log.d("gg", "Signup");
        Log.d("gg", jsonResponse.toString());
        ServiceResponse response = null;
        try {
//            Log.d("gg", jsonResponse.getString("Data"));

            response = mapper.readValue(jsonResponse.toString(), ServiceResponse.class);
//            Log.d("ggrr", jsonResponse.getString("Data"));

//            user = mapper.readValue(jsonResponse.getString("Data"), User.class);
//            Log.d("gg", user.getUserID());
//
//            Log.d("gg", response.getUser().getUserID());

            if(response.isStatusSuccess()){
                signupActivity.signupSuccess(response.getUser().getUserID());
            }else{
                signupActivity.signupFailed(response.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
            signupActivity.signupError();
        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
    }


}
