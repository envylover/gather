package com.example.gather.Model;

import com.example.gather.UserInfo;

import java.io.Serializable;

public class GetUseModel implements Serializable {
    public UserInfo[] data;
    public String errorMsg;
    public String result;
}
