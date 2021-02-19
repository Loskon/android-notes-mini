package com.loskon.noteminimalism3.ui.snackbars;

import android.app.Activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;

public class MySnackbarBackup {

    public static final String MSG_BACKUP_COMPLETED = "backup_completed";
    public static final String MSG_RESTORE_COMPLETED = "restore_completed";

    public static final String MSG_BACKUP_NO_COMPLETED = "backup_no_completed";
    public static final String MSG_RESTORE_NO_COMPLETED = "restore_no_completed";

    public static final String MSG_TEXT_ERROR = "text_error";
    public static final String MSG_TEXT_NO_FOLDER = "text_no_folder";

    public static final String MSG_TEXT_NO_PERMISSION = "no_permissions";
    public static final String MSG_TEXT_SIGN_IN_FAIL = "fail";
    public static final String MSG_TEXT_NO_INTERNET = "no_internet";
    public static final String MSG_TEXT_OUT = "out";

    public static void showSnackbar(Activity activity, boolean isSuccess, String typeMessage) {

        String message = null;

        ConstraintLayout constraintLayout =
                activity.findViewById(R.id.cstLayBackup);
        BottomAppBar bottomAppBar = activity.findViewById(R.id.btmAppBackup);

        switch (typeMessage) {
            case MSG_BACKUP_COMPLETED:
                message = activity.getString(R.string.snackbar_backup_text_completed);
                break;
            case MSG_RESTORE_COMPLETED:
                message = activity.getString(R.string.snackbar_backup_text_restore_completed);
                break;
            case MSG_BACKUP_NO_COMPLETED:
                message = activity.getString(R.string.snackbar_backup_text_no_completed);
                break;
            case MSG_RESTORE_NO_COMPLETED:
                message = activity.getString(R.string.snackbar_backup_text_restore_no_completed);
                break;
            case MSG_TEXT_ERROR:
                message = activity.getString(R.string.snackbar_backup_text_error);
                break;
            case MSG_TEXT_NO_FOLDER:
                message = activity.getString(R.string.snackbar_backup_text_no_folder);
                break;
            case MSG_TEXT_NO_PERMISSION:
                message = activity.getString(R.string.no_permissions);
                break;
            case MSG_TEXT_SIGN_IN_FAIL:
                message = activity.getString(R.string.sign_in_failed);
                break;
            case MSG_TEXT_NO_INTERNET:
                message = activity.getString(R.string.no_internet);
                break;
            case MSG_TEXT_OUT:
                message = activity.getString(R.string.out);
                break;
            default:
                message = "Error";
                break;
        }

        SnackbarBuilder.makeSnackbar(activity,
                constraintLayout, message , bottomAppBar, isSuccess);
    }
}
