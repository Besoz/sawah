package com.accorpa.sawah;

import android.util.Log;

import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by root on 15/01/17.
 */
public class SignupResponseListener implements Response.Listener<JSONObject> {
    private SignupActivity SignupActivity;

    public SignupResponseListener(SignupActivity signupActivity) {
        this.SignupActivity = SignupActivity;
    }

    @Override
    public void onResponse(JSONObject response) {
        ObjectMapper mapper = new ObjectMapper();

        Log.d("gg", response.toString());
////        ServiceResponse response = null;
//        try {
////            response = mapper.readValue(response.toString(), ServiceResponse.class);
//
//            if(response.isStatusSuccess()){
////                loginActivity.loginSuccess();
//            }else{
////                loginActivity.loginFailed(response.getMessage());
//            }
//
//        } catch (IOException e) {
////            loginActivity.loginError();
//        }
    }


}
