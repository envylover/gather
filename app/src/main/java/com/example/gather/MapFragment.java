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
import android.widget.Toast;

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
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.example.gather.Interface.Promise;
import com.example.gather.ViewModel.FriendViewModel;
import com.example.gather.ViewModel.FriendsViewModel;
import com.example.gather.ViewModel.LoginViewModel;
import com.example.gather.util.location.UpdataPosition;
import com.example.gather.util.location.UsePositionViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MapFragment extends Fragment implements View.OnTouchListener, AMap.OnMapTouchListener, AMap.OnMyLocationChangeListener, Promise, AMap.OnMarkerClickListener {

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
    private FriendViewModel friend;
    private Friend DestPoint;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendsViewModel = new ViewModelProvider(requireActivity()).get(FriendsViewModel.class);
        useInfo = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        usePosition = new ViewModelProvider(requireActivity()).get(UsePositionViewModel.class);
        friend = new ViewModelProvider(requireActivity()).get(FriendViewModel.class);
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
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setMyLocationEnabled(false);
        aMap.setOnMapTouchListener(this);
        aMap.setOnMyLocationChangeListener(this);
        friendsViewModel.Observe(requireActivity(), friends -> {
            for (Friend f : (ArrayList<Friend>)friends)
            {
                LatLng latLng = new LatLng(f.lat,f.lnt);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.setFlat(false);
                markerOptions.title(f.name);
                final Marker marker = aMap.addMarker(markerOptions);
                marker.showInfoWindow();
                marker.setObject(f);
                aMap.setOnMarkerClickListener(this);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (this.friend != null) {
            Friend friend = this.friend.getFriend();
            if (friend != null) {
                CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                        new CameraPosition(new LatLng(friend.lat, friend.lnt), 20, 0, 0));
                //带动画的移动，aMap添加动画监听时，会有动画效果。不添加不会开启动画
                aMap.moveCamera(mCameraUpdate);
                isFollow = false;
            }
            else {
                aMap.setMyLocationEnabled(true);
                return;
            }
        }
        aMap.setMyLocationEnabled(false);
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
    public void onDetach() {
        super.onDetach();
        if(friend != null) friend.clear();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.naviButton) {
            if(DestPoint == null) Toast.makeText(requireActivity(), "请设置终点", Toast.LENGTH_SHORT).show();
            else {
                Poi start = new Poi(useInfo.getUser().name, new LatLng(usePosition.getUsePosition().lat, usePosition.getUsePosition().lnt),null);
                Poi end = new Poi(DestPoint.name, new LatLng(DestPoint.lat, DestPoint.lnt),null);
                AmapNaviParams params = new AmapNaviParams(start, null, end, AmapNaviType.DRIVER, AmapPageType.ROUTE);
                AmapNaviPage.getInstance().showRouteActivity(requireActivity().getApplicationContext(), params, null);
                DestPoint = null;
            }

        } else if(v.getId() == R.id.locationButton) {
            if(!isFollow) {
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);
                aMap.setMyLocationStyle(myLocationStyle);
                aMap.setMyLocationEnabled(true);
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        DestPoint = (Friend) marker.getObject();
        return false;
    }
}

