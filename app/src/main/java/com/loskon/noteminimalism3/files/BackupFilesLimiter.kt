package com.loskon.noteminimalism3.files

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import java.io.File

/**
 * Метод для ограничения количества файлов бэкапов
 */

object BackupFilesLimiter {
    fun deleteExtraFiles(context: Context) {

        val homeFolder: File = BackupPath.getBackupFolder(context)
        val maxNumberFiles: Int = PrefHelper.getNumberBackups(context)
        var logFiles: Array<File>? = BackupFilter.getListDateBaseFiles(homeFolder)

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

                logFiles = BackupFilter.getListDateBaseFiles(homeFolder)

            } while (logFiles!!.size > maxNumberFiles)
        }
    }
}
