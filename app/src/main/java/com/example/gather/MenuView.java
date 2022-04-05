package com.example.gather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.gather.ViewModel.LoginViewModel;


public class MenuView extends LinearLayout {

    private ImageButton quitImgButton;
    private Button quitButton;
    private Context context;
    private TextView menuName;
    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.menu_layout, this);
        quitImgButton = findViewById(R.id.quit);
        quitButton = findViewById(R.id.quitButton);
        menuName = findViewById(R.id.menu_name);
        quitButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = context.getSharedPreferences("data", 0).edit();
            editor.remove("hasLogin");
            editor.apply();
            MainApplication.activityManger.finishAll();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
        quitImgButton.setOnClickListener(view -> {
            SharedPreferences.Editor editor = context.getSharedPreferences("data", 0).edit();
            editor.remove("hasLogin");
            editor.apply();
            MainApplication.activityManger.finishAll();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
    }

    public void setMenuName(String name) {
        if(menuName != null) {
            menuName.setText(name);
        }
    }
}
