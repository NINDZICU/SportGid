package com.kpfu.khlopunov.sportgid.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by hlopu on 26.03.2018.
 */

public class PermissionService {
    public static final int REQUEST_PERMISSION = 777;
    private Context context;

    public PermissionService(Context context) {
        this.context = context;
    }

    public boolean checkPermissionsStorage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
//            dialog.dismiss();
            return false;
        }
        else{
            return true;
        }
    }

    public boolean checkPermissionPhone(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.CALL_PHONE},
                    REQUEST_PERMISSION);
//            dialog.dismiss();
            return false;
        }
        else{
            return true;
        }
    }


}
