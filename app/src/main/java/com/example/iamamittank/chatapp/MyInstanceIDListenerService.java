package com.example.iamamittank.chatapp;

import android.util.Log;

import com.example.iamamittank.model.SessionClient;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("Firebase Token", refreshedToken);

        SessionClient sessionClient = new SessionClient(AppConfig.user_id, refreshedToken);

        ServerComm serverComm = new ServerComm();
        serverComm.sendFcmToken(sessionClient);
    }
}
