package com.example.iamamittank.chatapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.iamamittank.model.MessageClient;
import com.example.iamamittank.model.User;
import com.example.iamamittank.model.UserClient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ChatActivity extends AppCompatActivity {

    private List<UserClient> userClientList;
    static User user1, user2;
    private EditText msg_text;
    private Button msg_send;
    private ListView lv_mainlist;
    static ArrayList<String> al_strings;
    static ArrayAdapter<String> aa_strings;
    List<MessageClient> msgsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Long friend_id = bundle.getLong("friend_id");

        userClientList = new ArrayList<UserClient>();
        msgsArrayList = new ArrayList<MessageClient>();

        new getUserData().execute(friend_id);

        msg_text = (EditText) findViewById(R.id.msg_text);
        msg_send = (Button) findViewById(R.id.msg_send);
        lv_mainlist = (ListView) findViewById((R.id.chat_list));

        al_strings = new ArrayList<String>();
        aa_strings = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,al_strings);
        lv_mainlist.setAdapter(aa_strings);

        lv_mainlist.setDivider(null);
        lv_mainlist.setDividerHeight(0);

        msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg_text.getText().toString() != null) {
                    new sendMessage(msg_text.getText().toString()).execute();
                    al_strings.add(msg_text.getText().toString());
                    msg_text.setText(null);
                    aa_strings.notifyDataSetChanged();
                }
            }
        });
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
                PublicKey publicKey2 = kf2.generatePublic(new X509EncodedKeySpec(pubKey2));
                PrivateKey privateKey2 = kf2.generatePrivate(new PKCS8EncodedKeySpec(privKey2));

                user1 = new User(userClient1.getId(), userClient1.getName(), userClient1.getEmail(), publicKey1, privateKey1);
                user2 = new User(userClient2.getId(), userClient2.getName(), userClient2.getEmail(), publicKey2, privateKey2);

                new getMessages().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public class getMessages extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/messages/" + AppConfig.user_id + "/" + String.valueOf(user2.getId());

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            MessageClient[] messages = restTemplate.getForObject(url, MessageClient[].class);
            msgsArrayList = Arrays.asList(messages);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(msgsArrayList != null) {
                for(int i=0; i<msgsArrayList.size(); i++) {
                    MessageClient msgClient = new MessageClient();
                    msgClient = msgsArrayList.get(i);

                    try {
                        byte[] encryptedMsg = Base64.decode(msgClient.getMessageBody(), Base64.DEFAULT);
                        byte[] encryptedKey = Base64.decode(msgClient.getKey(), Base64.DEFAULT);

                        Cipher decrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                        if(msgClient.getSenderId() == AppConfig.user_id)
                            decrypt.init(Cipher.DECRYPT_MODE, user2.getPrivateKey());
                        else
                            decrypt.init(Cipher.DECRYPT_MODE, user1.getPrivateKey());
                        SecretKey decryptedKey = new SecretKeySpec(decrypt.doFinal(encryptedKey), "AES");

                        Cipher decipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                        decipher.init(Cipher.DECRYPT_MODE, decryptedKey);
                        String decryptedMsg = new String(decipher.doFinal(encryptedMsg), StandardCharsets.UTF_8);

                        al_strings.add(decryptedMsg);
                        aa_strings.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public class sendMessage extends AsyncTask<Void, Void, Void> {

        String msg;
        MessageClient message;

        public sendMessage(String msg) {
            this.msg = msg;
        }

        @Override
        protected void onPreExecute() {

            try {
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES","BC");
                keyGenerator.init(128);

                Key key = keyGenerator.generateKey();

                Log.i("AES Key", key.toString());

                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encryptMsg = cipher.doFinal(msg.getBytes(StandardCharsets.UTF_8));

                Cipher encrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                encrypt.init(Cipher.ENCRYPT_MODE, user2.getPublicKey());
                byte[] encryptedKey = encrypt.doFinal(key.getEncoded());
                Log.i("Encrypted Message Bytes", encryptMsg.toString());

                String str_msg = Base64.encodeToString(encryptMsg, Base64.DEFAULT);
                Log.i("Encrypted Message",str_msg);

                String str_key = Base64.encodeToString(encryptedKey, Base64.DEFAULT);
                Log.i("Encrypted AES Key", str_key);

                message = new MessageClient(user1.getId(),user2.getId(),str_msg,str_key);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                final String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/messages/" + String.valueOf(user1.getId()) + "/" + String.valueOf(user2.getId());

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                HttpEntity<MessageClient> requestEntity = new HttpEntity<MessageClient>(message, requestHeaders);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add((new StringHttpMessageConverter()));

                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                String response = responseEntity.getBody().toString();

                Log.i("Message Response", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
