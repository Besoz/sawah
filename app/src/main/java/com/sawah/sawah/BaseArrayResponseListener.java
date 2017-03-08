package com.sawah.sawah;

import android.util.Log;

import com.android.volley.Response;
import com.sawah.sawah.Handlers.JacksonHelper;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by root on 16/02/17.
 */

public class BaseArrayResponseListener implements Response.Listener<JSONArray> {

    private Class<?> cls;

    private ArrayRequestListener onResponseListener;

    public BaseArrayResponseListener(Class<?> cls) {
        this.cls = cls;
    }

    public void setOnResponseListener(ArrayRequestListener onResponseListener) {
        this.onResponseListener = onResponseListener;
    }

    @Override
    public void onResponse(JSONArray response) {

        Object[] arr = new Object[0];

        try {
            arr = JacksonHelper.getInstance().convertToArray(response.toString(), cls);
            Log.d("gg", String.valueOf(arr.length));
            onResponseListener.successResponse(arr);


        } catch (IOException e) {
            e.printStackTrace();
            onResponseListener.failResponse();
        }
    }
}
