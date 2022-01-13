package com.loskon.noteminimalism3.files

import android.content.Context
import java.io.File

/**
 * Получить файлы бэкапов из выбранной папки
 */

object BackupFiles {
    fun getList(context: Context): Array<File>? {
        val folder: File = BackupPath.getBackupFolder(context)

        return if (folder.exists()) {
            BackupFilter.getListDateBaseFiles(folder)
        } else {
            null
        }
    }
}
