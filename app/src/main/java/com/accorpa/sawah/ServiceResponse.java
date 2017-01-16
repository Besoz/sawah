package com.accorpa.sawah;

import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * Created by bassem on 12/01/17.
 */

public class ServiceResponse {

    private static final String STATUS_SUCCESS = "Success";
    private static final String STATUS_ERROR =  "Error";


    @JsonProperty("Status")
    private String status;


    @JsonProperty("Message")
    private String message;


    public void setUser(User user) {
        this.user = user;
    }

    public void setDraftPointID(String draftPointID) {
        this.draftPointID = draftPointID;
    }

    @JsonProperty("Data")
    private User user;

    @JsonProperty("DraftPointID")
    private String draftPointID;


    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatusSuccess(){
        if(status.equals(STATUS_SUCCESS)){
            return true;
        }else{
            return false;
        }
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
