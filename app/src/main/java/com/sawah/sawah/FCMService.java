package com.sawah.sawah;

import android.util.Log;

import com.sawah.sawah.Handlers.DataHandler;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Bassem on 14/01/17.
 */

public class FCMService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCMService============", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        DataHandler.getInstance(this.getApplicationContext()).updateDeviceToken(refreshedToken);

        // TODO: send any registration to your app's servers.

    }
}
