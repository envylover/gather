package com.example.gather.util;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityManager {
    private ArrayList<Activity> activities;

    public ActivityManager() {
        activities = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }
    public void finishAll() {
        activities.forEach(activity -> activity.finish());
    }
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }
}
