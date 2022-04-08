package com.example.gather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.maps.MapsInitializer;
import com.example.gather.Interface.Promise;
import com.example.gather.Model.GetUseModel;
import com.example.gather.ViewModel.*;
import com.example.gather.util.CheckRoom;
import com.example.gather.util.location.CheckPermissions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class UserActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, StartGameDialogFragment.OnEntryRoomClickListener {

    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private Button head_button;
    private LoginViewModel userInfo;
    private FriendsViewModel friends;
    private MenuView menuView;
    private RoomViewModel roomInfo;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainApplication.activityManger.addActivity(this);
        super.onCreate(savedInstanceState);
        CheckPermissions checkPermissions = new CheckPermissions(null);
        if (!checkPermissions.isHasChecked()) checkPermissions.requestPermissions(this);
        setContentView(R.layout.activity_user);
        init();
    }

    public void init() {
        String data = getIntent().getStringExtra("user_info");
        userInfo = new ViewModelProvider(this).get(LoginViewModel.class);
        menuView = findViewById(R.id.menu_view);
        roomInfo = new ViewModelProvider(this).get(RoomViewModel.class);
        friends = new ViewModelProvider(this).get(FriendsViewModel.class);
        userInfo.user.observe(this, userInfo -> {
            if(menuView != null) {
                menuView.setMenuName(userInfo.name);
            }
        });
        roomInfo.Observe(this, roomInfo -> {
            if(roomInfo != null) {
                RoomInfo room = (RoomInfo) roomInfo;
                CheckRoom checkRoom = new CheckRoom(this, room.roomName, room.password);
                checkRoom.setCheckedListener(new Promise() {
                    @Override
                    public void onSuccess(String data) {
                        if(progressDialog != null) {
                            runOnUiThread(()->{
                                if(progressDialog.isShowing())
                                    progressDialog.hide();
                                progressDialog = null;
                            });
                        }
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Friend>>(){}.getType();
                        ArrayList<Friend> friendList = gson.fromJson(data, type);
                        friends.setFriends(friendList,userInfo.user.getValue());
                    }

                    @Override
                    public void onFail(String error) {
                        if(progressDialog != null) {
                            runOnUiThread(()->{
                                if(progressDialog.isShowing())
                                    progressDialog.hide();
                                progressDialog = null;
                            });
                        }
                    }
                });
                checkRoom.check(userInfo.user.getValue().uid);
            }
        });
        if(data != null) {
            Gson gson = new Gson();
            GetUseModel userInfo = gson.fromJson(data, GetUseModel.class);
            SharedPreferences ref = getSharedPreferences("current_use", 0);
            ref.edit().putString("current_useInfo_name", userInfo.data[0].name).putString("useInfo", gson.toJson(userInfo.data[0])).apply();
            this.userInfo.setUser(userInfo.data[0]);
        } else {
            SharedPreferences ref = getSharedPreferences("current_use", 0);
            String name = ref.getString("current_useInfo_name", "");
            String data1 = ref.getString("useInfo", "");
            if(data1.compareTo("")!= 0){
                Gson gson = new Gson();
                UserInfo userInfo1 = gson.fromJson(data1, UserInfo.class);
                this.userInfo.setUser(userInfo1);
            }
            if(name !="") {
                menuView.setMenuName(name);
            }
        }
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Toolbar toolbar = findViewById(R.id.toolBar);
        // setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.map_fragment);
        drawerLayout = findViewById(R.id.drawerLayout);
        head_button = findViewById(R.id.head_button);
        head_button.setOnClickListener(view -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        if(userInfo.getUser() != null)
            ((TextView)findViewById(R.id.menu_name)).setText(userInfo.getUser().name);
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getItemId()) {
            case R.id.map_fragment:
//                bottomNavigationView.setSelectedItemId(item.getItemId());
                Fragment fragment1 = new MapFragment();
                transaction.replace(R.id.fragment_container_view, fragment1);
                break;
            case R.id.friends:
                Fragment fragment2 = new FriendsGroupView();
                transaction.replace(R.id.fragment_container_view, fragment2);
                break;
            default:
                break;
        }
        transaction.commit();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.activityManger.removeActivity(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_item, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_friends:
                StartGameDialogFragment startGameDialogFragment = new  StartGameDialogFragment();
                startGameDialogFragment.setEntryRoomClick(this);
                startGameDialogFragment.show(getSupportFragmentManager(), "加入房间");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void OnEntryRoomClick(String name, String password) {
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.roomName = name;
        roomInfo.password = password;
        this.roomInfo.setRoomInfo(roomInfo);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage("正在进入房间");
        progressDialog.show();
    }

    @Override
    public void OnCancelClick() {

    }

    public void setBottomMapNavigationSelect(){
        bottomNavigationView.setSelectedItemId(R.id.map_fragment);
    }
}