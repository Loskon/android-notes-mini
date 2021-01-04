package com.loskon.noteminimalism3.db.backup;

import android.app.Activity;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.GetDate;
import com.loskon.noteminimalism3.helper.MyToast;
import com.loskon.noteminimalism3.helper.ReplaceText;

import java.util.Date;

public class BackupAuto {

    private final Activity activity;

    public BackupAuto(Activity activity) {
        this.activity = activity;
    }

    public void callAutoBackup() {

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

                showToast(isSuccess);
            }
    }

    private void showToast(boolean isSuccess) {
        String message;
        if (isSuccess) {
            message = activity.getString(R.string.toast_main_text_auto_backup_completed);
        } else {
            message = activity.getString(R.string.toast_main_text_auto_backup_no_completed);
        }
        MyToast.showToast(activity, message, true);
    }
}
