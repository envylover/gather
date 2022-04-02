package com.example.gather;

import android.app.Application;
import android.content.Context;
import com.example.gather.util.ActivityManager;

public class MainApplication extends Application {

    public static Context context;
    public static ActivityManager activityManger;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        activityManger = new ActivityManager();
    }
}
