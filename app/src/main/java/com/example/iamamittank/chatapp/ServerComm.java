package com.example.iamamittank.chatapp;

import android.os.AsyncTask;

import com.example.iamamittank.model.SessionClient;

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

    public void sendFcmToken(SessionClient session) {

        new sendToken().execute(session);

    }

    public class sendToken extends AsyncTask<SessionClient, Void,Void> {

        @Override
        protected Void doInBackground(SessionClient... params) {

            try {
                final String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/users/session";

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application","json"));
                HttpEntity<SessionClient> requestEntity = new HttpEntity<SessionClient>(params[0],requestHeaders);

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
