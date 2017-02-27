package com.accorpa.sawah;

/**
 * Created by root on 22/02/17.
 */
public interface BaseRequestStateListener {

    void failResponse(ServiceResponse response);

    void successResponse(ServiceResponse response);


}
