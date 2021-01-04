package com.loskon.noteminimalism3.db.backup;

import android.app.Activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MySnackbar;

public class BackupSnackbar {

    public static final String MSG_BACKUP_COMPLETED = "backup_completed";
    public static final String MSG_RESTORE_COMPLETED = "restore_completed";

    public static final String MSG_BACKUP_NO_COMPLETED = "backup_no_completed";
    public static final String MSG_RESTORE_NO_COMPLETED = "restore_no_completed";

    public static final String MSG_TEXT_ERROR = "text_error";
    public static final String MSG_TEXT_NO_FOLDER = "text_no_folder";

    public static final String MSG_TEXT_NO_PERMISSION = "no_permissions";

    public static void showSnackbar(Activity activity, boolean isSuccess, String typeMessage) {

        String string = null;

        ConstraintLayout constraintLayout =
                activity.findViewById(R.id.cstLayBackup);
        BottomAppBar bar = activity.findViewById(R.id.btmAppBarBackup);

        if (isSuccess) {
            if (typeMessage.equals(MSG_BACKUP_COMPLETED)) {
                string = activity.getString(R.string.snackbar_backup_text_completed);
            } else if (typeMessage.equals(MSG_RESTORE_COMPLETED)) {
                string = activity.getString(R.string.snackbar_backup_text_restore_completed);
            }
        } else {
            switch (typeMessage) {
                case MSG_BACKUP_NO_COMPLETED:
                    string = activity.getString(R.string.snackbar_backup_text_no_completed);
                    break;
                case MSG_RESTORE_NO_COMPLETED:
                    string = activity.getString(R.string.snackbar_backup_text_restore_no_completed);
                    break;
                case MSG_TEXT_ERROR:
                    string = activity.getString(R.string.snackbar_backup_text_error);
                    break;
                case MSG_TEXT_NO_FOLDER:
                    string = activity.getString(R.string.snackbar_backup_text_no_folder);
                    break;
                case MSG_TEXT_NO_PERMISSION:
                    string = activity.getString(R.string.no_permissions);
                    break;
            }
        }

        MySnackbar.makeSnackbar(activity, constraintLayout, string , bar, isSuccess);
    }
}
