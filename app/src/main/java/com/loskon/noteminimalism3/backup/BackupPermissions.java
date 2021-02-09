package com.loskon.noteminimalism3.backup;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class BackupPermissions {

    public static final int REQUEST_CODE_PERMISSIONS = 298;

    // Переменные прав доступа к хранилищу
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // проверьте разрешения.
    public static boolean verifyStoragePermissions(Activity activity,
                                                   Fragment fragment, boolean isRequestPermission) {
        // Разрешения на чтение и запись
        int writePermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
            // У нас нет разрешения, поэтому подскажите пользователю
            if (isRequestPermission) {
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
