package com.example.gather.Interface;

public interface Promise {
    public void onSuccess(String data);
    public void onFail(String error);
}
