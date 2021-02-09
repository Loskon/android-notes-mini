package com.loskon.noteminimalism3.backup;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.GetDate;
import com.loskon.noteminimalism3.helper.MyToast;
import com.loskon.noteminimalism3.helper.ReplaceText;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;

import java.util.Date;

public class BackupAuto {

    private final Activity activity;

    public BackupAuto(Activity activity) {
        this.activity = activity;
    }

    public void callAutoBackup(boolean isAutoBackupMessage) {
        if (BackupPermissions.verifyStoragePermissions(activity, null, false)) {
            boolean isSuccess;
            boolean isFolderCreated = BackupPath.createNoteFolder(activity);

            if (isFolderCreated) {

                try {
                    String titleText = GetDate.getNowDate(new Date());
                    titleText = ReplaceText.replaceForSaveTittle(titleText) + " (A)";

                    (new BackupSetName(activity)).callBackup(titleText);

                    isSuccess = true;

                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                }

                if (GetSharedPref.isNotAutoBackup(activity) && isAutoBackupMessage) {
                    showToast(isSuccess);
                }

            }
        } else {
            showToast(false);
        }
    }

    private void showToast(boolean isSuccess) {
        String message;
        if (isSuccess) {
            message = activity.getString(R.string.toast_main_text_auto_backup_completed);
        } else {
            message = activity.getString(R.string.toast_main_text_auto_backup_no_completed);
        }
        MyToast.showToast(activity, message, isSuccess);
    }
}
