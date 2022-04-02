package com.example.gather;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.gather.util.location.CheckPermissions;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class MainActivity extends AppCompatActivity  {
   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
       MainApplication.activityManger.addActivity(this);
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
       SharedPreferences refs = getSharedPreferences("data", 0);
       boolean res = refs.getBoolean("hasLogin", false);
       Intent intent;
       if(res)
       {
           intent = new Intent(this, UserActivity.class);
       } else {
           intent = new Intent(this, LoginActivity.class);
       }
       startActivity(intent);
       finish();
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.activityManger.removeActivity(this);
    }
}

