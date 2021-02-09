package com.loskon.noteminimalism3.backup;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;

import java.io.File;

public class BackupLimiter {

    public static void delExtraFiles(Activity activity, File folder) {

        int numOfBackup = GetSharedPref.getNumOfBackup(activity);
        File[] logFiles = BackupSort.getListFile(folder);

        if (logFiles != null && logFiles.length > numOfBackup) {
            // удалить самые старые файлы после того, как есть более N файлов
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

                logFiles = BackupSort.getListFile(folder);

            } while (logFiles.length > numOfBackup);
        }
    }
}
