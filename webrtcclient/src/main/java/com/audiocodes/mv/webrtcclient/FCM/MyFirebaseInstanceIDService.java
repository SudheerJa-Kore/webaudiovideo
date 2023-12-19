package com.audiocodes.mv.webrtcclient.FCM;

import android.util.Log;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(FCMWrapper.TAG, "FCM push token: " + token);
        FCMWrapper.getInstance().updatePushToken();
    }

}
