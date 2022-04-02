package com.example.gather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelStoreOwner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MainApplication.activityManger.addActivity(this);
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        View view = null;
        if(window != null) view = window.getDecorView();
        if(view != null)
        {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainApplication.activityManger.removeActivity(this);
    }
}