package com.sawah.sawah;

/**
 * Created by root on 22/02/17.
 */
public interface ArrayRequestListener<T> {

    void failResponse();

    void successResponse(T[] response);


}
