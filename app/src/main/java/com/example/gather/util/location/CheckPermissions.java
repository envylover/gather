package com.example.gather.util.location;

import android.Manifest;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.gather.MainApplication;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class CheckPermissions implements EasyPermissions.PermissionCallbacks {
    private boolean hasChecked = false;
    private String[] permissions;
    private final int REQUEST_CODE = 996;
    public String ErrorMsg;
    public CheckPermissions(String[] permissions) {
        if(permissions == null)
            this.permissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            };
        else this.permissions = permissions;
        init();
    }

    private void init() {
        if(EasyPermissions.hasPermissions(MainApplication.context, permissions)) {
            hasChecked = true;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied((Activity) MainApplication.context, perms))
            ErrorMsg = "有权限被永久拒绝";
        else
            ErrorMsg = "权限被拒绝";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void requestPermissions(Activity activity) {

        EasyPermissions.requestPermissions(activity,"需要权限获取精确位置",REQUEST_CODE,permissions);

    }

    public void requestPermissions(Fragment fragment) {

        EasyPermissions.requestPermissions(fragment,"需要权限获取精确位置",REQUEST_CODE,permissions);

    }

    public boolean isHasChecked () {
        return hasChecked;
    }
}