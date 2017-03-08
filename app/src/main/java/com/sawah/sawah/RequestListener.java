package com.sawah.sawah;

/**
 * Created by root on 22/02/17.
 */
public interface RequestListener {

    void failResponse(Object response);

    void successResponse(Object response);


}
