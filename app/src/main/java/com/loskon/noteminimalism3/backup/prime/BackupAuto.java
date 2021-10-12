package com.loskon.noteminimalism3.backup.prime;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.MyToast;
import com.loskon.noteminimalism3.auxiliary.other.ReplaceText;
import com.loskon.noteminimalism3.permissions.PermissionsInActivity;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.backup.second.AppFolder;
import com.loskon.noteminimalism3.backup.second.BackupSetName;

import java.util.Date;

/**
 * Создание резеравной копии базы данных после добавления новой заметки
 */

public class BackupAuto {

    private final Activity activity;
    boolean isAccess;

    public BackupAuto(Activity activity) {
        this.activity = activity;
    }

    public void buildBackup(boolean isShowToast, Date date) {

        isAccess = new PermissionsInActivity().isAccess(activity);

        if (isAccess) {

            boolean isFolderCreated = AppFolder.createBackupFolder(activity);
            boolean isNotification = GetSharedPref.hasNotificationAutoBackup(activity);

            if (isFolderCreated) {

                try {
                    String backupName = MyDate.getNowDate(date);
                    backupName = ReplaceText.replace(backupName) + " (A)";
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

        if (isAccess) {
            if (isSuccess) {
                message = activity.getString(R.string.toast_main_auto_backup_completed);
            } else {
                message = activity.getString(R.string.toast_main_auto_backup_failed);
            }
        } else {
            message = activity.getString(R.string.toast_main_auto_backup_not_possible);
        }

        MyToast.showToast(activity, message, isSuccess);
    }
}
