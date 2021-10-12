package com.loskon.noteminimalism3.files

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.backup.second.BackupFilter
import com.loskon.noteminimalism3.backup.second.BackupPath
import java.io.File

/**
 * Метод для ограничения файлов резервного копирования
 */

class BackupFilesLimiter {

    companion object {
        @JvmStatic
        fun deleteExtraFiles(context: Context) {

            val homeFolder = BackupPath.getFolderBackup(context)
            val maxNumberFiles = AppPref.getNumberBackups(context)
            var logFiles = BackupFilter.getListFile(homeFolder)

            if (logFiles != null && logFiles.size > maxNumberFiles) {
                // Удалить все старые файлы после того, как есть более N файлов
                do {
                    var oldestDate = Long.MAX_VALUE
                    var oldestFile: File? = null

                    for (file in logFiles) {
                        if (file.lastModified() < oldestDate) {
                            oldestDate = file.lastModified()
                            oldestFile = file
                        }
                    }

                    if (oldestFile != null) {
                        SQLiteDatabase.deleteDatabase(File(oldestFile.path))
                    }

                    logFiles = BackupFilter.getListFile(homeFolder)

                } while (logFiles.size > maxNumberFiles)
            }
        }
    }
}