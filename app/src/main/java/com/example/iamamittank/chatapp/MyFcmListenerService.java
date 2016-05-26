package com.example.iamamittank.chatapp;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;


public class MyFcmListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map<String,String> rawData = remoteMessage.getData();
        String data = rawData.get("message");

        Log.i("Raw Data", String.valueOf(rawData.size()));
        Log.i("From", from);
        Log.i("Data", data);
    }
}
