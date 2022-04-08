package com.example.gather.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

import com.example.gather.Interface.Promise;
import com.example.gather.Model.GetUseModel;
import com.google.gson.Gson;

import java.io.IOException;

import javax.security.auth.callback.Callback;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CheckLogin {
    private Context context;
    private String password;
    private String phone;
    private String uid = "";
    private Promise promise;
    private String baseUrl = "http://123.56.31.104:3303/";
    public CheckLogin(){}
    public CheckLogin(Context context, String phone, String password) {
        this.phone = phone;
        this.password = password;
        this.context = context;
    }

    public String getphone() {
        return phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void check() {
        new Thread(()->{
            String res = null;
                try {
                    res = getUseInfo(phone);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(promise != null) {
                    if(res != null) {
                        Gson gson = new Gson();
                        GetUseModel getUseModel = gson.fromJson(res, GetUseModel.class);
                        if(getUseModel.data == null || getUseModel.data.length == 0)
                            promise.onFail(getUseModel.errorMsg == null ? getUseModel.result: getUseModel.errorMsg);
                        else {
                            promise.onSuccess(res);
                        }
                    }
                    else
                        promise.onFail("连接失败");
                }
            }).start();
    }
    public void setCheckedListener(Promise promise) {
        this.promise = promise;
    }

    public String getUseInfo(String phone) {
        OkHttpClient client = new OkHttpClient();
        String Url = baseUrl + "?type=getUseInfo";// uid;
        Md5 md5 = new Md5();
        String passwordMd5 = md5.md5(new String[]{password});
        FormBody body = new FormBody.Builder().add("phone", phone).add("password", passwordMd5).build();
        Request request = new Request.Builder().url(Url) .addHeader("charset", "utf-8").post(body).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if(response != null){
            String res = null;
            try {
                res  = response.body().string();
                return res;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
    public String setUseInfo(String uid, String name, String password) {
        OkHttpClient client = new OkHttpClient();
        String Url = baseUrl + "?type=setUseInfo"; // "&uid=" + uid + "&name=" + name +"&password=" + password + "&phone=" + roomNum;
        FormBody body = new FormBody.Builder().add("uid", uid).add("name", name).add("password", password).add("phone", phone).build();
        Request request = new Request.Builder().url(Url)
                .addHeader("charset", "utf-8")
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if(response != null){
            String res = null;
            try {
                res  = response.body().string();
                return res;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        else
            return null;
    }
    public void register(String uid, String name, String password) {
        this.uid = uid;
        new Thread(()->{
            String res = null;
            try {
                res = setUseInfo(uid, name, password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(promise != null) {
                if(res != null) {
                    Gson gson = new Gson();
                    GetUseModel getUseModel = gson.fromJson(res, GetUseModel.class);
                    if(getUseModel.result == "fair")
                        promise.onFail(getUseModel.errorMsg);
                    else
                        promise.onSuccess(res);
                }
                else
                    promise.onFail("连接失败");
            }
        }).start();
    }
}
