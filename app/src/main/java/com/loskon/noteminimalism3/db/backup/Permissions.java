package com.loskon.noteminimalism3.db.backup;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import static com.loskon.noteminimalism3.ui.Helper.MainHelper.REQUEST_CODE_PERMISSIONS;

public class Permissions {

    public static boolean permissionGranted = false;

    // Переменные прав доступа к хранилищу
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // проверьте разрешения.
    public static void verifyStoragePermissions(Activity activity) {
        // Проверьте, есть ли у нас разрешение на чтение или запись
        int writePermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
            // У нас нет разрешения, поэтому подскажите пользователю
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE_PERMISSIONS
            );
        } else {
            permissionGranted = true;
        }
    }
}
