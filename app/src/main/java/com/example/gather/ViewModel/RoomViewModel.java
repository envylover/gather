package com.example.gather.ViewModel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.gather.Friend;
import com.example.gather.RoomInfo;

import java.util.ArrayList;

public class RoomViewModel extends ViewModel {
    private MutableLiveData<RoomInfo> roomInfo;
    public RoomViewModel() {
        roomInfo = new MutableLiveData<>();
    }
    public void setRoomInfo(RoomInfo roomInfo) {
        this.roomInfo.postValue(roomInfo);
    }

    public RoomInfo getRoomInfo() {
        return roomInfo.getValue();
    }

    public void Observe(LifecycleOwner lifecycleOwner, Observer observer) {
        roomInfo.observe(lifecycleOwner,observer);
    }

    public void removeObserve(Observer observer) {
        roomInfo.removeObserver(observer);
    }
}
