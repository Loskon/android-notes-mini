package com.loskon.noteminimalism3.backup.second;

import android.content.Context;

import java.io.File;

/**
 * Создание имени бэкапа
 */

public class BackupSetName {

    private final Context context;

    public BackupSetName(Context context) {
        this.context = context;
    }

    public void callBackup(boolean isAutoBackup, String backupName) {
        String filePath = BackupPath.getPath(context) + File.separator;

        String outFileName = filePath + backupName + ".db";

        (new BackupDb(context)).backupDatabase(isAutoBackup, outFileName);


    }
}
