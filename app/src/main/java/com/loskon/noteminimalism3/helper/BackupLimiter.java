package com.loskon.noteminimalism3.helper;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class BackupLimiter {

    public static void purgeLogFiles(File folder) {
        File[] logFiles = RestoreHelper.getListFile(folder);
        long oldestDate = Long.MAX_VALUE;
        File oldestFile = null;
        if (logFiles != null && logFiles.length > 10) {
            // удалить самые старые файлы после того, как есть более 10 файлов
            for (File file : logFiles) {
                if (file.lastModified() < oldestDate) {
                    oldestDate = file.lastModified();
                    oldestFile = file;
                }
            }

            if (oldestFile != null) {
                SQLiteDatabase.deleteDatabase(new File(oldestFile.getPath()));
                //oldestFile.deleteOnExit();
            }
        }
    }
}
