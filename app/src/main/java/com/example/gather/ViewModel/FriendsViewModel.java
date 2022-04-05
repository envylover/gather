package com.example.gather.ViewModel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.gather.Friend;

import java.util.ArrayList;


public class FriendsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Friend>> friends;
    public FriendsViewModel() {
        friends = new MutableLiveData<>();
    }
    public void setFriends(ArrayList<Friend> friends) {
        this.friends.postValue(friends);
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
