package com.example.iamamittank.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.iamamittank.model.User;
import com.example.iamamittank.model.UserClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class FbLoginFragment extends android.support.v4.app.Fragment {

    ProgressDialog progressDialog;
    private CallbackManager callbackManager;
    ImageView imageView;

    public FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        private ProfileTracker profileTracker;

        @Override
        public void onSuccess(final LoginResult loginResult) {
            String accessToken = loginResult.getAccessToken().getToken();
            Log.i("Access Token", accessToken);
            if (Profile.getCurrentProfile() == null) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                    }
                };
                profileTracker.startTracking();
            } else {
                Profile profile = Profile.getCurrentProfile();
                Log.i("name", profile.getName());
                Log.i("userId", profile.getId());
            }
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    JSONObject json = response.getJSONObject();
                    try {
                        User user = new User((long) json.getDouble("id"), json.getString("name"), json.getString("email"));
                        Log.i("id", Long.toString(user.getId()));
                        Log.i("name", user.getName());
                        Log.i("email" , user.getEmail());
                        new getUserData().execute(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();

        }

        @Override
        public void onCancel() {
            Log.i("Facebook - Cancel", "cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.i("Facebook - Error", error.getMessage());
        }
    };

    public FbLoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        imageView = (ImageView) getActivity().findViewById(R.id.imageView);
        AccessToken token;
        token = AccessToken.getCurrentAccessToken();
        if (token != null) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            getActivity().startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fb_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class getUserData extends AsyncTask<User, Void, UserClient> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Logging in");
            progressDialog.setIndeterminate(false);
            progressDialog.setMessage("Fetching data...");
            progressDialog.show();
        }

        @Override
        protected UserClient doInBackground(User... params) {

            try {
                final String url = "http://" + AppConfig.ip_address + ":8080/chatapp-server-01/webapi/users/" + params[0].getId();

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(new MediaType("application", "json"));
                HttpEntity<User> requestEntity = new HttpEntity<User>(params[0], requestHeaders);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ResponseEntity<UserClient> responseEntity = restTemplate.exchange(url, HttpMethod.POST,requestEntity,UserClient.class);
                UserClient userClient = new UserClient(responseEntity.getBody().getId(), responseEntity.getBody().getName(), requestEntity.getBody().getEmail(), responseEntity.getBody().getPublicKey(), responseEntity.getBody().getPrivateKey());

                return userClient;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserClient userClient) {
            super.onPostExecute(userClient);
            try {
                byte[] pubKey = android.util.Base64.decode(userClient.getPublicKey(), android.util.Base64.DEFAULT);
                byte[] privKey = android.util.Base64.decode(userClient.getPrivateKey(), android.util.Base64.DEFAULT);
                KeyFactory kf = KeyFactory.getInstance("RSA","BC");
                PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(pubKey));
                PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privKey));
                User user = new User(userClient.getId(), userClient.getName(),userClient.getEmail(),publicKey,privateKey);

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                Bundle userBundle = new Bundle();
                userBundle.putParcelable("user_info", user);
                UserProfile userProfile = new UserProfile();
                userProfile.setArguments(userBundle);
                intent.putExtras(userBundle);
                getActivity().startActivity(intent);
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
