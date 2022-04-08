package com.example.gather;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.gather.Interface.Promise;
import com.example.gather.ViewModel.FriendsViewModel;
import com.example.gather.ViewModel.LoginViewModel;
import com.example.gather.util.location.UpdataPosition;
import com.example.gather.util.location.UsePositionViewModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment implements View.OnTouchListener, AMap.OnMapTouchListener, AMap.OnMyLocationChangeListener, Promise {

    private MapView mapView;
    private AMap aMap;
    private ImageButton naviButton;
    private UiSettings uiSettings;
    private ImageButton locationButton;
    private MyLocationStyle myLocationStyle;
    private FriendsViewModel friendsViewModel;
    private boolean isFollow = true;
    private LoginViewModel useInfo;
    private AMapLocationClient mLocationClient = null;
    private UsePositionViewModel usePosition;
    private UpdataPosition updataPosition = new UpdataPosition();
    private ExecutorService singThread = Executors.newSingleThreadExecutor();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsViewModel = new ViewModelProvider(requireActivity()).get(FriendsViewModel.class);
        friendsViewModel.Observe(requireActivity(), friends -> {

        });
        useInfo = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        usePosition = new ViewModelProvider(requireActivity()).get(UsePositionViewModel.class);
        usePosition.Observe(requireActivity(), usePosition->{
            UsePosition usePosition1 = (UsePosition) usePosition;
            singThread.execute(() ->{
                updataPosition.Updata(useInfo.getUser(), usePosition1.lnt, usePosition1.lat, this);
            });
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.gather_map, container,false);
        naviButton = view.findViewById(R.id.naviButton);
        locationButton = view.findViewById(R.id.locationButton);
        naviButton.setOnTouchListener(this);
        mapView = view.findViewById(R.id.map_view);
        mapView.setOnTouchListener(this);
        mapView.onCreate(savedInstanceState);
        locationButton.setOnTouchListener(this);
        if(null != mapView) {
            aMap = mapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        uiSettings = aMap.getUiSettings();
        // uiSettings.setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMapTouchListener(this);
        aMap.setOnMyLocationChangeListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.naviButton) {

        } else if(v.getId() == R.id.locationButton) {
            if(!isFollow) {
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
                aMap.setMyLocationStyle(myLocationStyle);
                isFollow = true;
            }
        }
        return false;
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if(isFollow) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            aMap.setMyLocationStyle(myLocationStyle);
            isFollow = false;
        }
    }


    @Override
    public void onSuccess(String data) {
    }

    @Override
    public void onFail(String error) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        UsePosition usePosition = new UsePosition(location.getLatitude(),location.getLongitude());
        this.usePosition.setUsePositionM(usePosition);
    }
}

