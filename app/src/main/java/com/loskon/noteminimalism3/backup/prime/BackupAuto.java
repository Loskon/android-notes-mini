package com.loskon.noteminimalism3.backup.prime;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.MyToast;
import com.loskon.noteminimalism3.auxiliary.other.ReplaceText;
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsStorage;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.backup.second.AppFolder;
import com.loskon.noteminimalism3.backup.second.BackupSetName;

import java.util.Date;

/**
 * Создание резеравной копии базы данных после добавления новой заметки
 */

public class BackupAuto {

    private final Activity activity;
    boolean isPermissions;

    public BackupAuto(Activity activity) {
        this.activity = activity;
    }

    public void buildBackup(boolean isShowToast, Date date) {

        isPermissions = PermissionsStorage
                .verify(activity, null, false);

        if (isPermissions) {

            boolean isFolderCreated = AppFolder.createBackupFolder(activity);
            boolean isNotification = GetSharedPref.isNotificationAutoBackup(activity);

            if (isFolderCreated) {

                try {
                    String backupName = MyDate.getNowDate(date);
                    backupName = ReplaceText.replaceForSaveTittle(backupName) + " (A)";
                    (new BackupSetName(activity)).callBackup(true, backupName);
                    if (isNotification && isShowToast) showToast(true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    if (isShowToast) showToast(false);
                }
            }
        } else {
            showToast(false);
        }
    }

    private void showToast(boolean isSuccess) {
        String message;

        if (isPermissions) {
            if (isSuccess) {
                message = activity.getString(R.string.toast_main_auto_backup_completed);
            } else {
                message = activity.getString(R.string.toast_main_auto_backup_failed);
            }
        } else {
            message = activity.getString(R.string.no_permissions);
        }

        MyToast.showToast(activity, message, isSuccess);
    }
}
