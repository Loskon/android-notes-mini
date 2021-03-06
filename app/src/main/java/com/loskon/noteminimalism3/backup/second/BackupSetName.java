package com.loskon.noteminimalism3.backup.second;

import android.app.Activity;

import java.io.File;

/**
 * Создание имени бэкапа
 */

public class BackupSetName {

    private final Activity activity;

    public BackupSetName(Activity activity) {
        this.activity = activity;
    }

    public void callBackup(boolean isAutoBackup, String backupName) {
        File folder = BackupPath.getFolder(activity);
        String filePath = BackupPath.getPath(activity) + File.separator;

        String outFileName = filePath + backupName  + ".db";
        (new BackupDb(activity)).backupDatabase(isAutoBackup, outFileName);
        BackupLimiter.delExtraFiles(activity, folder);
    }
}
