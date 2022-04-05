package com.example.gather.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gather.Friend;
import com.example.gather.LoginView;
import com.example.gather.UserInfo;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<UserInfo> user;

    public LoginViewModel() {
        user = new MutableLiveData<>();
    }

    public void setUser(UserInfo user) {
        this.user.postValue(user);
    }

    public UserInfo getUser() {
        return user.getValue();
    }
}
