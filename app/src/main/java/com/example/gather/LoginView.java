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

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.gather.Interface.Promise;
import com.example.gather.ViewModel.LoginViewModel;
import com.example.gather.util.CheckLogin;
import com.google.gson.Gson;

public class LoginView extends LinearLayout implements View.OnClickListener {

    private EditText editPhone;
    private EditText editPassword;
    private Button loginButton;
    private CheckBox isRem;
    private Context context;
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
        }else {
            editor.putString("phone", editPhone.getText().toString());
            editor.putString(editPhone.getText().toString(), editPassword.getText().toString());
            editor.putBoolean("remember", true);
            editor.apply();
        }
        Intent intent = new Intent(this.context, UserActivity.class);

        editor.putBoolean("hasLogin", true);
        editor.apply();
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage("登陆中");
        progressDialog.show();
        CheckLogin checkLogin = new CheckLogin(context,editPhone.getText().toString(),editPassword.getText().toString());
        checkLogin.setCheckedListener(new Promise() {
            @Override
            public void onSuccess(String data) {
                intent.putExtra("user_info", data);
                ((Activity) context).runOnUiThread(()->{
                    progressDialog.hide();
                    context.startActivity(intent);
                    ((Activity) context).finish();
                });

            }

            @Override
            public void onFail(String error) {
                ((Activity) context).runOnUiThread(()->{
                    progressDialog.hide();
                });
            }
        });
    }
}
