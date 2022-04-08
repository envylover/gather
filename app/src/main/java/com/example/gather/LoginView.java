package com.example.gather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.gather.Interface.Promise;
import com.example.gather.Model.GetUseModel;
import com.example.gather.ViewModel.LoginViewModel;
import com.example.gather.util.CheckLogin;
import com.example.gather.util.Md5;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginView extends LinearLayout implements View.OnClickListener, RegisterDialog.OnEntryRoomClickListener, Promise{

    private EditText editPhone;
    private EditText editPassword;
    private Button loginButton;
    private CheckBox isRem;
    private Context context;
    private UserInfo userInfo = new UserInfo();
    private ProgressDialog progressDialog;
    CheckLogin checkLogin;
    @SuppressLint("ResourceType")
    public LoginView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.denglu,this);
        editPhone = findViewById(R.id.editTextPhone);
        editPassword = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);
        isRem = findViewById(R.id.isRem);
        SharedPreferences refs = context.getSharedPreferences("data", 0);
        String phone = refs.getString("phone","");
        String password = refs.getString(phone, "");
        String uid = refs.getString("uid", "");
        checkLogin = new CheckLogin();
        checkLogin.setUid(uid);
        checkLogin.setphone(phone);
        boolean isRem = refs.getBoolean("remember", false);
        this.context = context;
        if(phone != ""){
            editPhone.setText(phone);
        }
        if(password != "") {
            editPassword.setText(password);
        }
        this.isRem.setChecked(isRem);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data", 0).edit();
        if(!isRem.isChecked()){
            editor.clear();
        }else if(editPhone.getText().toString().compareTo(checkLogin.getphone()) != 0) {
            editor.putString("uid", "");
        }
        editor.putString("phone", editPhone.getText().toString());
        editor.putString(editPhone.getText().toString(), editPassword.getText().toString());
        editor.putBoolean("remember", true);
        editor.apply();
        editor.putBoolean("hasLogin", true);
        editor.apply();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage("登陆中");
        progressDialog.show();
        checkLogin.setPassword(editPassword.getText().toString());
        checkLogin.setphone(editPhone.getText().toString());
        checkLogin.setCheckedListener(this);
        checkLogin.check();
    }

    @Override
    public void OnEntryRoomClick(String name, String password) {
        Md5 md5 = new Md5();
        String str = md5.md5(new String[]{name, password});
        String passwordMd5 = md5.md5(new String[]{editPassword.getText().toString()});
        new Thread(()-> {
            checkLogin.register(str,name,passwordMd5);
        }).start();
    }

    @Override
    public void OnCancelClick() {

    }

    @Override
    public void onSuccess(String data) {
        Gson gson = new Gson();
        GetUseModel useModel = gson.fromJson(data,GetUseModel.class);
        if(useModel.data != null) {
            Intent intent = new Intent(this.context, UserActivity.class);
            intent.putExtra("user_info", data);
            SharedPreferences.Editor editor = context.getSharedPreferences("data", 0).edit();
            if (!isRem.isChecked()) {
                editor.clear();
            } else {
                editor.putString("phone", editPhone.getText().toString());
                editor.putString(editPhone.getText().toString(), editPassword.getText().toString());
                editor.putBoolean("remember", true);
                editor.putString("uid", checkLogin.getUid());
                editor.apply();
            }
            ((Activity) context).runOnUiThread(() -> {
                progressDialog.hide();
                context.startActivity(intent);
                ((Activity) context).finish();
            });
        }else if("success".compareTo(useModel.result) == 0) {
           checkLogin.check();
        }
    }

    @Override
    public void onFail(String error) {
        ((Activity) context).runOnUiThread(()->{
            if(error.compareTo("不存在") == 0) {
                RegisterDialog registerDialog = new RegisterDialog();
                registerDialog.setEntryRoomClick(this);
                registerDialog.show(((AppCompatActivity)context).getSupportFragmentManager() , "加入房间");
            }
            else if(error.compareTo("连接失败") == 0) {
                Toast.makeText(getContext(),"网络连接失败", Toast.LENGTH_LONG).show();

            }
            else if(error.compareTo("密码错误") == 0) {
                Toast.makeText(getContext(),"密码错误", Toast.LENGTH_LONG).show();
            }
            else if(error.compareTo("参数错误") == 0)  {
                Toast.makeText(getContext(),error, Toast.LENGTH_LONG).show();
            }
            SharedPreferences.Editor editor = context.getSharedPreferences("data", 0).edit();
            editor.putBoolean("hasLogin", false);
            progressDialog.hide();
        });
    }
}
