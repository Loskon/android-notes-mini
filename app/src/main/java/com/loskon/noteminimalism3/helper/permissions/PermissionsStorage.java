package com.loskon.noteminimalism3.helper.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_PERMISSIONS;

/**
 * Проверка разрешений для взаимодействия с памятью телефона
 */

public class PermissionsStorage {



    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean verifyStoragePermissions(Activity activity,
                                                   Fragment fragment, boolean isAskForPermission) {
        int writePermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
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
