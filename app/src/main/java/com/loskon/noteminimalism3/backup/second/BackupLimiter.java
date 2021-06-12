package com.loskon.noteminimalism3.backup.second;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;

import java.io.File;

/**
 *  Ограничение количества сохраненных в памяти файлов бэкапа
 */

public class BackupLimiter {

    public static void delExtraFiles(Context context) {
        File folder = BackupPath.getFolder(context);

        int numOfBackup = GetSharedPref.getNumOfBackup(context);
        File[] logFiles = BackupHelper.getListFile(folder);

        if (logFiles != null && logFiles.length > numOfBackup) {
            // Удалить все старые файлы после того, как есть более N файлов
            do {

                long oldestDate = Long.MAX_VALUE;
                File oldestFile = null;

                for (File file : logFiles) {
                    if (file.lastModified() < oldestDate) {
                        oldestDate = file.lastModified();
                        oldestFile = file;
                    }
                }

                if (oldestFile != null) {
                    SQLiteDatabase.deleteDatabase(new File(oldestFile.getPath()));
                }

                logFiles = BackupHelper.getListFile(folder);

            } while (logFiles.length > numOfBackup);
        }
    }
}
