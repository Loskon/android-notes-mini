package com.loskon.noteminimalism3.backup.main;

import android.app.Activity;

import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogBackup;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogRestore;

import java.io.File;

/**
 *
 */

public class BackupLocal {

    private final Activity activity;

    private String typeMessage;

    public BackupLocal(Activity activity) {
        this.activity = activity;
    }

    public void performBackup() {

        boolean isFolderCreated = BackupPath.createNoteFolder(activity);

        if (isFolderCreated) {
            (new MyDialogBackup(activity)).callDialogBackup();
        } else {
            typeMessage = MySnackbarBackup.MSG_TEXT_ERROR;
            showSnackbar();
        }

    }

    public void performRestore() {

        File folder = BackupPath.getFolder(activity);

        if (folder.exists()) {
            (new MyDialogRestore(activity)).callDialogRestore(folder);
        } else {
            typeMessage = MySnackbarBackup.MSG_TEXT_NO_FOLDER;
            showSnackbar();
        }

    }

    private void showSnackbar() {
        MySnackbarBackup.showSnackbar(activity, false, typeMessage);
    }
}
