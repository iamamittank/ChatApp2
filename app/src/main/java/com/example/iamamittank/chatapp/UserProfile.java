package com.example.iamamittank.chatapp;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iamamittank.model.User;

import java.io.InputStream;


public class UserProfile extends Fragment {

    HomeActivity homeActivity;
    User user;
    TextView tv_name;
    TextView tv_email;
    ImageView iv_profile_pic;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActivity = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        Bundle user_bundle = intent.getExtras();
        user = new User();
        user = user_bundle.getParcelable("user_info");
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_name = (TextView) view.findViewById(R.id.name);
        tv_email = (TextView) view.findViewById(R.id.email);
        iv_profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        tv_name.setText(user.getName());
        tv_email.setText(user.getEmail());
        String user_id = String.valueOf(user.getId());
        new FbProfilePicture(iv_profile_pic).execute(user_id);
    }

    public class FbProfilePicture extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        public FbProfilePicture(ImageView image) {
            this.imageView = image;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int px = Math.round(300 * (displayMetrics.xdpi/DisplayMetrics.DENSITY_DEFAULT));
            String url = "https://graph.facebook.com/" + params[0] + "/picture?width=" + String.valueOf(px) + "&height=" + String.valueOf(px);
            Bitmap img = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                img = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

}
