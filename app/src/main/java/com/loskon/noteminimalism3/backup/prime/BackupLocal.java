package com.loskon.noteminimalism3.backup.prime;

import android.app.Activity;

import com.loskon.noteminimalism3.backup.second.AppFolder;
import com.loskon.noteminimalism3.ui.dialogs.SheetListFiles;
import com.loskon.noteminimalism3.ui.sheets.SheetBackupCreate;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

/**
 * Проверка и создание папки для бэкапа
 */

public class BackupLocal {

    private final Activity activity;

    private String typeMessage;

    public BackupLocal(Activity activity) {
        this.activity = activity;
    }

    public void performBackup() {

        boolean isFolderCreated = AppFolder.createBackupFolder(activity);

        if (isFolderCreated) {
            (new SheetBackupCreate(activity)).show();
        } else {
            typeMessage = MySnackbarBackup.MSG_TEXT_ERROR;
            showSnackbar();
        }

    }

    public void performRestore() {
        (new SheetListFiles(activity)).show();
    }

    private void showSnackbar() {
        MySnackbarBackup.showSnackbar(activity, false, typeMessage);
    }
}
