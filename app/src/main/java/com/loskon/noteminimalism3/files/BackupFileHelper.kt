package com.loskon.noteminimalism3.files

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.loskon.noteminimalism3.sharedpref.AppPreference
import java.io.File

/**
 * Помощник для работы с файлами бэкапа
 */

object BackupFileHelper {

    //--- Получить список файлов бэкапа ------------------------------------------------------------
    fun getList(context: Context): Array<File>? {
        val folder: File = BackupPath.getBackupFolder(context)

        return if (folder.exists()) {
            getListDateBaseFiles(folder)
        } else {
            null
        }
    }

    private fun getListDateBaseFiles(folder: File?): Array<File>? {
        return folder?.listFiles { _, name: String ->
            name.lowercase().endsWith(".db")
        }
    }

    //--- Удаление экстра файлов бэкапа ------------------------------------------------------------
    fun deleteExtraFiles(context: Context) {
        val homeFolder: File = BackupPath.getBackupFolder(context)
        val maxNumberFiles: Int = AppPreference.getNumberBackups(context)
        var logFiles: Array<File>? = getListDateBaseFiles(homeFolder)

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

                logFiles = getListDateBaseFiles(homeFolder)

            } while (logFiles!!.size > maxNumberFiles)
        }
    }

    fun folderCreated(folder: File): Boolean {
        var hasFileCreated = true
        if (folder.exists().not()) hasFileCreated = folder.mkdirs()
        return hasFileCreated
    }
}