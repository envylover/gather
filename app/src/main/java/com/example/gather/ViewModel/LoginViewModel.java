package com.example.gather.ViewModel;

import androidx.lifecycle.ViewModel;
import com.example.gather.Friend;
import com.example.gather.UserInfo;

public class LoginViewModel extends ViewModel {
    private UserInfo user;

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public UserInfo getUser() {
        return user;
    }
}
