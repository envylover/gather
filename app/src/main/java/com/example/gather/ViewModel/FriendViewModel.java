package com.example.gather.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gather.Friend;

public class FriendViewModel extends ViewModel {
    public MutableLiveData<Friend> friend;

    public FriendViewModel() {
        friend = new MutableLiveData<>();
    }

    public void setFriend(Friend friend) {
        this.friend.setValue(friend);
    }

    public Friend getFriend() {
        return friend.getValue();
    }

    public void clear() {
        friend.setValue(null);
    }

}
