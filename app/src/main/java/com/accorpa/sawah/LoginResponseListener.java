package com.accorpa.sawah;

import android.util.Log;

import com.android.volley.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by bassem on 12/01/17.
 */

public class LoginResponseListener implements Response.Listener<JSONObject>{

    private LoginActivity loginActivity;

    public LoginResponseListener(LoginActivity loginActivity){
        this.loginActivity = loginActivity;
    }
    @Override
    public void onResponse(JSONObject jsonResponse) {
//        response.
        ObjectMapper mapper = new ObjectMapper();

        Log.d("gg", jsonResponse.toString());
        ServiceResponse response = null;
        try {
            response = mapper.readValue(jsonResponse.toString(), ServiceResponse.class);

            if(response.isStatusSuccess()){
                loginActivity.loginSuccess();
            }else{
                loginActivity.loginFailed(response.getMessage());
            }

        } catch (IOException e) {
            loginActivity.loginError();
        }
        

    }
}
