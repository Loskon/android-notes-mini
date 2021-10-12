package com.loskon.noteminimalism3.backup.prime;

import android.app.Activity;

import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.ui.sheets.SheetBackupDateBase;
import com.loskon.noteminimalism3.ui.sheets.SheetRestoreDateBase;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

import java.io.File;

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
        (new SheetBackupDateBase(activity)).show();
    }

    public void performRestore() {
        File folder = BackupPath.getFolderBackup(activity);

        if (folder.exists()) {
            (new SheetRestoreDateBase(activity)).show();
        } else {
            typeMessage = MySnackbarBackup.MSG_TEXT_NO_FOLDER;
            showSnackbar();
        }
    }

    private void showSnackbar() {
        MySnackbarBackup.showSnackbar(activity, false, typeMessage);
    }
}
