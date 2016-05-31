package com.example.iamamittank.chatapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.iamamittank.model.SessionClient;
import com.example.iamamittank.model.User;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

/**
 * Created by iamamittank on 26-Apr-16.
 */
public class HomeActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    User user;
    String regId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = bundle.getParcelable("user_info");

        AppConfig.user_id = user.getId();

        String fcmToken = FirebaseInstanceId.getInstance().getToken();

        SessionClient sessionClient = new SessionClient(AppConfig.user_id, fcmToken);

        ServerComm serverComm = new ServerComm();
        serverComm.sendFcmToken(sessionClient);

        Log.i("Token" , FirebaseInstanceId.getInstance().getToken());

        Bundle user_bundle = new Bundle();
        user_bundle.putParcelable("user_info", user);
        UserProfile user_info = new UserProfile();
        user_info.setArguments(user_bundle);
        getIntent().putExtras(user_bundle);

        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.bottom_bar, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(@IdRes int menuItemId) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItemId) {
                    case R.id.friends:
                        FriendListFragment friendListFragment = new FriendListFragment();
                        fragmentTransaction.replace(R.id.placeholder, friendListFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.chats:
                        ChatHome chatHome = new ChatHome();
                        fragmentTransaction.replace(R.id.placeholder,chatHome);
                        fragmentTransaction.commit();
                        break;
                    case R.id.profile:
                        UserProfile userProfile = new UserProfile();
                        fragmentTransaction.replace(R.id.placeholder,userProfile);
                        fragmentTransaction.commit();
                        break;
                    default:
                        finish();
                }
            }
        });
        bottomBar.setActiveTabColor("#C2185B");
        bottomBar.useDarkTheme(true);
        bottomBar.setDefaultTabPosition(1);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite_friends:
                return true;
            case R.id.settings:
                return true;
            case R.id.fb_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you want to logout ?");
                alertDialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(HomeActivity.this);
                        progressDialog.setTitle("Logout");
                        progressDialog.setMessage("Facebook logging out...");
                        progressDialog.setIndeterminate(false);
                        progressDialog.show();
                        AppConfig.user_id = null;
                        LoginManager.getInstance().logOut();
                        startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        progressDialog.dismiss();
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
