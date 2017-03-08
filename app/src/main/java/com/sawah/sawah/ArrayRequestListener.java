package com.sawah.sawah;

/**
 * Created by root on 22/02/17.
 */
public interface ArrayRequestListener {

    void failResponse();

    void successResponse(Object[] response);


}
