package com.example.gather.util.location;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.gather.Friend;
import com.example.gather.UsePosition;

import java.util.ArrayList;



public class UsePositionViewModel extends ViewModel {
    private MutableLiveData<UsePosition> usePositionM;
    public UsePositionViewModel() {
        usePositionM = new MutableLiveData<>();
    }
    public void postUsePositionM(UsePosition s) {
        this.usePositionM.postValue(s);
    }
    public void setUsePositionM(UsePosition s) {
        this.usePositionM.setValue(s);
    }
    public UsePosition getUsePosition() {
        return usePositionM.getValue();
    }

    public void Observe(LifecycleOwner lifecycleOwner, Observer observer) {
        usePositionM.observe(lifecycleOwner,observer);
    }

    public void removeObserve(Observer observer) {
        usePositionM.removeObserver(observer);
    }
}
