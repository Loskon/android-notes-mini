package com.loskon.noteminimalism3.backup.main;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.helper.permissions.PermissionsStorage;
import com.loskon.noteminimalism3.backup.second.BackupSetName;
import com.loskon.noteminimalism3.helper.GetDate;
import com.loskon.noteminimalism3.helper.MyToast;
import com.loskon.noteminimalism3.helper.ReplaceText;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;

import java.util.Date;

/**
 * Создание резеравной копии базы данных после добавления новой заметки
 */

public class BackupAuto {

    private final Activity activity;
    boolean isRequestPermissions;

    public BackupAuto(Activity activity) {
        this.activity = activity;
    }

    public void callAutoBackup(boolean isAutoBackupMessage, Date date) {

        isRequestPermissions = PermissionsStorage
                .verifyStoragePermissions(activity, null, false);

        if (isRequestPermissions) {

            boolean isFolderCreated = BackupPath.createNoteFolder(activity);
            boolean isNotification = GetSharedPref.isNotificationAutoBackup(activity);

            if (isFolderCreated) {

                try {
                    String titleText = GetDate.getNowDate(date);
                    titleText = ReplaceText.replaceForSaveTittle(titleText) + " (A)";

                    (new BackupSetName(activity)).callBackup(titleText);

                    if (isNotification && isAutoBackupMessage) toastAutoBackup(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    if (isAutoBackupMessage) toastAutoBackup(false);
                }

            }
        } else {
            toastAutoBackup(false);
        }
    }

    private void toastAutoBackup(boolean isSuccess) {
        String message;

        if (isRequestPermissions) {
            if (isSuccess) {
                message = activity.getString(R.string.toast_main_text_auto_backup_completed);
            } else {
                message = activity.getString(R.string.toast_main_text_auto_backup_no_completed);
            }
        } else {
            message = activity.getString(R.string.no_permissions);
        }

        MyToast.showToast(activity, message, isSuccess);
    }
}
