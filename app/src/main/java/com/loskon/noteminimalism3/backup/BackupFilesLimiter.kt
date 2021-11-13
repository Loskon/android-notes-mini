package com.loskon.noteminimalism3.backup

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.loskon.noteminimalism3.sharedpref.PrefManager
import java.io.File

/**
 * Метод для ограничения файлов резервного копирования
 */

class BackupFilesLimiter {

    companion object {

        fun deleteExtraFiles(context: Context) {

            val homeFolder: File? = BackupPath.getFolderBackup(context)
            val maxNumberFiles: Int = PrefManager.getNumberBackups(context)
            var logFiles: Array<File>? = BackupFilter.getListDateBaseFile(homeFolder)

            if (logFiles != null && logFiles.size > maxNumberFiles) {
                // Удалить все старые файлы после того, как есть более N файлов
                do {
                    var oldestDate: Long = Long.MAX_VALUE
                    var oldestFile: File? = null

                    for (file in logFiles!!) {
                        if (file.lastModified() < oldestDate) {
                            oldestDate = file.lastModified()
                            oldestFile = file
                        }
                    }

                    if (oldestFile != null) {
                        SQLiteDatabase.deleteDatabase(File(oldestFile.path))
                    }

                    logFiles = BackupFilter.getListDateBaseFile(homeFolder)

                } while (logFiles!!.size > maxNumberFiles)
            }
        }
    }
}