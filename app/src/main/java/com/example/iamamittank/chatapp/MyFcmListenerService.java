package com.example.iamamittank.chatapp;

import android.util.Base64;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class MyFcmListenerService extends FirebaseMessagingService {

    private String key;
    private String sender;
    private String body;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String from = remoteMessage.getFrom();
        Map<String, String> rawData = remoteMessage.getData();
        sender = rawData.get("sender");
        body = rawData.get("body");
        key = rawData.get("key");

        if (Long.parseLong(sender) == ChatActivity.user2.getId()) {
            try {
                byte[] encryptedMsg = Base64.decode(body, Base64.DEFAULT);
                byte[] encryptedKey = Base64.decode(key, Base64.DEFAULT);

                Cipher decrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                decrypt.init(Cipher.DECRYPT_MODE, ChatActivity.user1.getPrivateKey());
                SecretKey decryptedKey = new SecretKeySpec(decrypt.doFinal(encryptedKey), "AES");

                Cipher decipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                decipher.init(Cipher.DECRYPT_MODE, decryptedKey);
                String decryptedMsg = new String(decipher.doFinal(encryptedMsg), StandardCharsets.UTF_8);
                ChatActivity.al_strings.add(decryptedMsg);
                ChatActivity.aa_strings.notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i("Sender", sender);
        Log.i("Body", body);
        Log.i("Key", key);
    }

}
