package com.example.gather.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.example.gather.Interface.Promise;

import javax.security.auth.callback.Callback;



public class CheckLogin {
    private Promise promise;
    public CheckLogin(Context context, String phoneNum, String password) {
        new Thread(()->{
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(promise != null) {
                    promise.onSuccess(null);
                }
            }).start();
    }
    public void setCheckedListener(Promise promise) {
        this.promise = promise;
    }
}
