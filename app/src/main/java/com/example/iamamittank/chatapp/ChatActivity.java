package com.example.iamamittank.chatapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.iamamittank.model.User;
import com.example.iamamittank.model.UserClient;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private List<UserClient> userClientList;
    private User user1, user2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Long friend_id = bundle.getLong("friend_id");

        userClientList = new ArrayList<UserClient>();

        new getUserData().execute(friend_id);
    }

    public class getUserData extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            try {
                final String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/users/" + AppConfig.user_id.toString() + "/" + params[0].toString();

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                UserClient[] users = restTemplate.getForObject(url, UserClient[].class);
                userClientList = Arrays.asList(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserClient userClient1, userClient2;
            userClient1 = userClientList.get(0);
            userClient2 = userClientList.get(1);
            try {

                byte[] pubKey1 = android.util.Base64.decode(userClient1.getPublicKey(), android.util.Base64.DEFAULT);
                byte[] privKey1 = android.util.Base64.decode(userClient1.getPrivateKey(), android.util.Base64.DEFAULT);
                KeyFactory kf1 = KeyFactory.getInstance("RSA", "BC");
                PublicKey publicKey1 = kf1.generatePublic(new X509EncodedKeySpec(pubKey1));
                PrivateKey privateKey1 = kf1.generatePrivate(new PKCS8EncodedKeySpec(privKey1));

                byte[] pubKey2 = android.util.Base64.decode(userClient2.getPublicKey(), android.util.Base64.DEFAULT);
                byte[] privKey2 = android.util.Base64.decode(userClient2.getPrivateKey(), android.util.Base64.DEFAULT);
                KeyFactory kf2 = KeyFactory.getInstance("RSA", "BC");
                PublicKey publicKey2 = kf1.generatePublic(new X509EncodedKeySpec(pubKey2));
                PrivateKey privateKey2 = kf1.generatePrivate(new PKCS8EncodedKeySpec(privKey2));

                user1 = new User(userClient1.getId(), userClient1.getName(), userClient1.getEmail(), publicKey1, privateKey1);
                user2 = new User(userClient2.getId(), userClient2.getName(), userClient2.getEmail(), publicKey2, privateKey2);
                Log.i("User 1", user1.getName());
                Log.i("User 2", user2.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
