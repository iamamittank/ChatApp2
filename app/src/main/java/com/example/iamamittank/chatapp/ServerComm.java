package com.example.iamamittank.chatapp;

import android.os.AsyncTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by iamamittank on 25-May-16.
 */
public class ServerComm {

    public void sendFcmToken(String token) {

        new sendToken().execute(token);

    }

    public class sendToken extends AsyncTask<String, Void,Void> {

        @Override
        protected Void doInBackground(String... params) {

            try {
                final String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/users/" + AppConfig.user_id.toString() + "/token/" + params[0];

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                HttpEntity<String> requestEntity = new HttpEntity<String>(params[0],requestHeaders);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                String response = responseEntity.getBody().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

    }

}
