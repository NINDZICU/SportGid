package com.kpfu.khlopunov.sportgid.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import static com.kpfu.khlopunov.sportgid.service.ServiceConstants.GALLERY_REQUEST;

/**
 * Created by hlopu on 27.03.2018.
 */

public class ActiveSystemService implements ActiveSystemServiceInt {
    private Context context;
    private PermissionService permissionService;

    public ActiveSystemService(Context context) {
        this.context = context;
        permissionService = new PermissionService(context);
    }

    public void makeCall(String number) {
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (permissionService.checkPermissionPhone()) {
            context.startActivity(dialIntent);
        }
    }

    public void runPhotoPicker() {
        if (permissionService.checkPermissionsStorage()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            ((Activity) context).startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        }
    }
}
