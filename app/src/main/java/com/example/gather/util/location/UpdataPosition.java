package com.example.gather.util.location;

import android.util.Log;

import com.example.gather.Interface.Promise;
import com.example.gather.UserInfo;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class UpdataRes {
    public String errorMsg;
    public String result;
}

public class UpdataPosition {

    private final String baseUrl = "http://123.56.31.104:3303/";
    public OkHttpClient client = new OkHttpClient();

    public void Updata(UserInfo userInfo,Double lnt, Double lat, Promise promise) {
        synchronized (this){
            String Url = baseUrl + "?type=updataPosition";// uid;
            FormBody body = new FormBody.Builder().add("uid", userInfo.uid).add("lnt",lnt.toString()).add("lat", lat.toString()).build();
            Request request = new Request.Builder().url(Url) .addHeader("charset", "utf-8").post(body).build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String res = null;
            try {
                if(response == null) return;
                res = response.body().string();
                Gson gson = new Gson();
                UpdataRes updataRes = gson.fromJson(res,UpdataRes.class);
                if(updataRes.result != null && "success".compareTo(updataRes.result) == 0) {
                    promise.onSuccess("success");
                }else {
                    promise.onFail("error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
