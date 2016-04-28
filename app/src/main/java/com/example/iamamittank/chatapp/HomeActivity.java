package com.example.iamamittank.chatapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.iamamittank.model.Message;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by iamamittank on 26-Apr-16.
 */
public class HomeActivity extends AppCompatActivity {

    Button btn;
    EditText txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        txt = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpRequestTask().execute();
            }
        });
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Message> {

        @Override
        protected Message doInBackground(Void... params) {
            try {
                final String url = getUrl();
                //final String url = "http://172.19.3.43:8080/chatapp-server-01/webapi/messages/2";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Message msg = restTemplate.getForObject(url, Message.class);
                return msg;
            } catch (Exception e) {
                Log.e("HomeActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Message message) {
            TextView messageId = (TextView) findViewById(R.id.message_id);
            TextView sender = (TextView) findViewById(R.id.sender);
            TextView receiver = (TextView) findViewById(R.id.receiver);
            TextView messageBody = (TextView) findViewById(R.id.message_body);

            messageId.setText(String.valueOf(message.getMessageId()));
            sender.setText(String.valueOf(message.getSenderId()));
            receiver.setText(String.valueOf(message.getReceiverId()));
            messageBody.setText(String.valueOf(message.getMessageBody()));
        }
    }

    private String getUrl() {
        int num = Integer.parseInt(txt.getText().toString());
        return "http://192.168.1.2:8080/chatapp-server-01/webapi/messages/" + String.valueOf(num);
    }
}
