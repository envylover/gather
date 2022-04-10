package com.example.gather.util;

import android.content.Context;

import com.example.gather.Friend;
import com.example.gather.Interface.Promise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class CheckResponse {
    public Friend[] data;
    public String errorMsg;
    public String result;
}

public class CheckRoom {
    private Promise promise;
    private Context context;
    private String roomNum;
    private String password;
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "http://123.56.31.104:3303/";
    public CheckRoom(Context context, String roomNum, String password) {
        this.roomNum = roomNum;
        this.password = password;
        this.context = context;
    }

    public CheckRoom() { }

    public void setCheckedListener(Promise promise) {
        this.promise = promise;
    }
    public void check(String uid) {
        new Thread(()->{
            joinRoom(uid);
        }).start();
    }
    public void joinRoom(String uid) {
        String Url = baseUrl + "?type=checkRoom";
        Md5 md5 = new Md5();
        password = md5.md5(new String[]{password});
        FormBody body = new FormBody.Builder().add("rid", roomNum).add("password", password).build();
        Request request = new Request.Builder().url(Url)
                .addHeader("charset", "utf-8")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            Gson gson = new Gson();
            CheckResponse result = gson.fromJson(res, CheckResponse.class);
            if(result.result != null && result.result.compareTo("success") == 0) {
                join(uid);
            } else {
                if(promise !=null) promise.onFail("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void join(String uid) {
        String Url = baseUrl + "?type=joinRoom";
        FormBody body = new FormBody.Builder().add("rid", roomNum).add("uid", uid).build();
        Request request = new Request.Builder().url(Url)
                .addHeader("charset", "utf-8")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            Gson gson = new Gson();
            CheckResponse result = gson.fromJson(res, CheckResponse.class);
            if(result.result != null && result.result.compareTo("success") == 0) {
                getFriends(uid, roomNum);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFriends(String uid, String rid) {
        String Url = baseUrl + "?type=getUseInfoRoom";
        FormBody body = new FormBody.Builder().add("rid", rid).add("uid", uid).build();
        Request request = new Request.Builder().url(Url)
                .addHeader("charset", "utf-8")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            Gson gson = new Gson();
            CheckResponse result = gson.fromJson(res, CheckResponse.class);
            if(result.data != null && result.data.length > 0 ) {
                if(promise != null) promise.onSuccess(gson.toJson(result.data));
            }else if(promise !=null) promise.onFail(result.errorMsg != null ? result.errorMsg : result.result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createRoom(String roomNum, String password, String uid) {
        this.roomNum = roomNum;
        this.password = password;
        String Url = baseUrl + "?type=createRoom";
        Md5 md5 = new Md5();
        password = md5.md5(new String[]{password});
        FormBody body = new FormBody.Builder().add("rid", roomNum).add("password", password).build();
        Request request = new Request.Builder().url(Url)
                .addHeader("charset", "utf-8")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            Gson gson = new Gson();
            CheckResponse result = gson.fromJson(res, CheckResponse.class);
            if(result.result != null && result.result.compareTo("success") == 0) {
                 if(promise != null) promise.onSuccess("success");
            } else {
                if(promise !=null) promise.onFail("error");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void quitRoom(String rid, String uid) {
        String Url = baseUrl + "?type=quit";
        if(rid == null || uid == null ) return;
        FormBody body = new FormBody.Builder().add("rid", rid).add("uid", uid).build();
        Request request = new Request.Builder().url(Url)
                .addHeader("charset", "utf-8")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            Gson gson = new Gson();
            CheckResponse result = gson.fromJson(res, CheckResponse.class);
//            if(result.data != null && result.data.length > 0 ) {
//                if(promise != null) promise.onSuccess(gson.toJson(result.data));
//            }else if(promise !=null) promise.onFail(result.errorMsg != null ? result.errorMsg : result.result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}