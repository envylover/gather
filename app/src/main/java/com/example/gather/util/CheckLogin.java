package com.example.gather.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.example.gather.Interface.Promise;

import javax.security.auth.callback.Callback;



public class CheckLogin {
    private Context context;
    private String password;
    private String roomNum;
    private Promise promise;
    public CheckLogin(Context context, String roomNum, String password) {
        this.roomNum = roomNum;
        this.password = password;
        this.context = context;
    }
    public void check() {
        new Thread(()->{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(promise != null) {
                    promise.onSuccess("{\"phone\": \"12312313113\",\"name\":\"john\" }");
                }
            }).start();
    }
    public void setCheckedListener(Promise promise) {
        this.promise = promise;
    }
}
