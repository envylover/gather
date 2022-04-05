package com.example.gather.util;

import android.content.Context;

import com.example.gather.Friend;
import com.example.gather.Interface.Promise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CheckRoom {
    private Promise promise;
    private Context context;
    private String roomNum;
    private String password;
    public CheckRoom(Context context, String roomNum, String password) {
        this.roomNum = roomNum;
        this.password = password;
        this.context = context;
    }
    public void setCheckedListener(Promise promise) {
        this.promise = promise;
    }
    public void check() {
        new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(promise != null) {
                ArrayList<Friend> friends = new ArrayList<>();
                for(int i = 0; i < 20;++i) {
                    friends.add(new Friend("1000000" + i,"1000000" + i, "join" + i));
                }
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<Friend>>(){}.getType();
                promise.onSuccess(gson.toJson(friends,type));
            }
        }).start();
    }
}