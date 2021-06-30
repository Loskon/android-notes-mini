package com.loskon.noteminimalism3.auxiliary.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.os.Build.VERSION.SDK_INT;
import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_PERMISSIONS;

/**
 * Проверка и запрос разрешений для взаимодействия с памятью телефона
 */

public class PermissionsStorage {

    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean verify(Activity activity,
                                 Fragment fragment, boolean isAskForPermission) {

        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", activity.getApplicationContext().getPackageName())));
                    activity.startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    activity.startActivityForResult(intent, 2296);
                }
                return false;
            }
        } else {
            int writePermission = ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readPermission = ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.READ_EXTERNAL_STORAGE);

            int granted = PackageManager.PERMISSION_GRANTED;

            if (writePermission != granted && readPermission != granted) {
                if (isAskForPermission) {
                    if (fragment == null) {
                        ActivityCompat.requestPermissions(
                                activity,
                                PERMISSIONS_STORAGE,
                                REQUEST_CODE_PERMISSIONS
                        );
                    } else {
                        fragment.requestPermissions(
                                PERMISSIONS_STORAGE,
                                REQUEST_CODE_PERMISSIONS
                        );
                    }
                }
                return false;
            } else {
                return true;
            }
        }


    }
}
