package com.example.iamamittank.chatapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.iamamittank.model.ChatListItem;
import com.example.iamamittank.model.Friend;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by iamamittank on 16-May-16.
 */
public class FriendListFragment extends Fragment {

    private TextView username;
    private TextView email;
    private ListView friends_listview;
    private ArrayList<ChatListItem> al_friends;
    private ChatArrayAdapter caa;
    List<Friend> friendArrayList;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please Wait");
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("Fetching friend list...");
        progressDialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = (TextView) getView().findViewById(R.id.list_username);
        email = (TextView) getView().findViewById(R.id.list_last_message);
        friends_listview = (ListView) getView().findViewById(R.id.friends_listview);

        al_friends = new ArrayList<ChatListItem>();

        new getFriendsList().execute(AppConfig.user_id.toString());

        friends_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle userBundle = new Bundle();
                userBundle.putLong("friend_id", al_friends.get(position).getUser_id());
                intent.putExtras(userBundle);
                getActivity().startActivity(intent);
            }
        });
    }

    public class getFriendsList extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/users/" + params[0].toString();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            Friend[] friends = restTemplate.getForObject(url, Friend[].class);
            friendArrayList = Arrays.asList(friends);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            for(int i=0; i<friendArrayList.size(); i++) {
                Friend friend = new Friend();
                friend = friendArrayList.get(i);
                Log.i("name" , friend.getUserName().toString());
                ChatListItem chatListItem = new ChatListItem(friend.getId(), friend.getUserName(), friend.getUserEmail(), false);
                al_friends.add(chatListItem);
                progressDialog.dismiss();
            }

            caa = new ChatArrayAdapter(getActivity(), al_friends);
            friends_listview.setAdapter(caa);

        }
    }

}
