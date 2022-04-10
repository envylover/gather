package com.example.gather.ViewModel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.example.gather.Friend;
import com.example.gather.MainApplication;
import com.example.gather.UserInfo;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


public class FriendsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Friend>> friends;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    public FriendsViewModel() {
        friends = new MutableLiveData<>();
    }
    public void setFriends(ArrayList<Friend> friends, UserInfo useinfo) {
        Optional<Friend> use = friends.stream().filter(f -> useinfo.uid.contains(f.uid)).findFirst();
        Friend currentUse = use.get();
        friends.removeIf(friend -> friend.uid.compareTo(useinfo.uid) == 0);
        if(use.get() == null) {
            return;
        }
        LatLng useLatlnt = new LatLng(currentUse.lat, currentUse.lnt);
            AtomicInteger taskCnt = new AtomicInteger();
            for (Friend i : friends) {
                LatLng friendsLatlnt = new LatLng(i.lat, i.lnt);
                executorService.execute(() -> {
                    float distance = AMapUtils.calculateLineDistance(useLatlnt, friendsLatlnt);
                    i.setDistance(Float.valueOf(distance).toString() + 'm');
                    GeocodeSearch geocoderSearch = null;
                    try {
                        geocoderSearch = new GeocodeSearch(MainApplication.context);
                        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(friendsLatlnt.latitude, friendsLatlnt.longitude), 20, GeocodeSearch.GPS);
                        RegeocodeAddress regeocodeAddress = geocoderSearch.getFromLocation(query);
                        i.location = regeocodeAddress.getFormatAddress();
                        taskCnt.getAndIncrement();
                        if(taskCnt.get() == friends.toArray().length) {
                            this.friends.postValue(friends);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
    }

    public ArrayList<Friend> getFriends() {
        return friends.getValue();
    }

    public void Observe(LifecycleOwner lifecycleOwner, Observer observer) {
        friends.observe(lifecycleOwner,observer);
    }

    public void removeObserve(Observer observer) {
        friends.removeObserver(observer);
    }


}
