package com.loskon.noteminimalism3.ui.snackbars;

import android.app.Activity;
import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;

/**
 * Кастомный Snackbar с различными сообщениями
 */

public class MySnackbarBackup {

    public static final String MSG_BACKUP_COMPLETED = "msg_backup_completed";
    public static final String MSG_RESTORE_COMPLETED = "msg_restore_completed";

    public static final String MSG_BACKUP_FAILED = "msg_backup_failed";
    public static final String MSG_RESTORE_FAILED = "msg_restore_failed";

    public static final String MSG_TEXT_ERROR = "msg_text_error";
    public static final String MSG_TEXT_NO_FOLDER = "msg_text_no_folder";

    public static final String MSG_TEXT_NO_PERMISSION = "msg_no_permission";
    public static final String MSG_TEXT_SIGN_IN_FAILED = "msg_sign_in_failed";
    public static final String MSG_TEXT_NO_INTERNET = "msg_no_internet";
    public static final String MSG_TEXT_OUT = "msg_out";
    public static final String MSG_DEL_DATA = "msg_del_data";
    public static final String MSG_INTERNET_PROBLEM = "msg_internet_problem";

    public static void  showSnackbar(Context context, boolean isSuccess, String typeMessage) {
        String message;

        ConstraintLayout constraintLayout = ((Activity) context).findViewById(R.id.cstLayBackup);
        BottomAppBar bottomAppBar = ((Activity) context).findViewById(R.id.btmAppBackup);

        switch (typeMessage) {
            case MSG_BACKUP_COMPLETED:
                message = context.getString(R.string.sb_bp_completed);
                break;
            case MSG_RESTORE_COMPLETED:
                message = context.getString(R.string.sb_bp_restore_completed);
                break;
            case MSG_BACKUP_FAILED:
                message = context.getString(R.string.sb_bp_failed);
                break;
            case MSG_RESTORE_FAILED:
                message = context.getString(R.string.sb_bp_restore_failed);
                break;
            case MSG_TEXT_ERROR:
                message = context.getString(R.string.sb_bp_text_error);
                break;
            case MSG_TEXT_NO_FOLDER:
                message = context.getString(R.string.sb_bp_text_no_folder);
                break;
            case MSG_TEXT_NO_PERMISSION:
                message = context.getString(R.string.no_permissions);
                break;
            case MSG_TEXT_SIGN_IN_FAILED:
                message = context.getString(R.string.sb_bp_sign_in_failed);
                break;
            case MSG_TEXT_NO_INTERNET:
                message = context.getString(R.string.sb_bp_no_internet);
                break;
            case MSG_TEXT_OUT:
                message = context.getString(R.string.sb_bp_logged_account);
                break;
            case MSG_DEL_DATA:
                message = context.getString(R.string.sb_bp_del_acc);
                break;
            case MSG_INTERNET_PROBLEM:
                message = context.getString(R.string.sb_bp_problem_internet);
                break;
            default:
                message = context.getString(R.string.unknown_error);
                break;
        }

        SnackbarBuilder.makeSnackbar(context, constraintLayout, message, bottomAppBar, isSuccess);
    }
}
