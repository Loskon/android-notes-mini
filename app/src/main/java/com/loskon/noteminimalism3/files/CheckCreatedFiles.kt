package com.loskon.noteminimalism3.files

import android.content.Context
import com.loskon.noteminimalism3.backup.BackupPath
import java.io.File

/**
 * Создание папок
 */

class CheckCreatedFiles {
    companion object {

        fun createBackupFolder(context: Context): Boolean {
            val folder: File = BackupPath.getFolderBackup(context)

            var isFolderCreated = true

            if (!folder.exists()) {
                isFolderCreated = folder.mkdirs()
            }

            return isFolderCreated
        }

        fun checkCreatedFolder(folderTextFiles: File): Boolean {
            var isFolderCreated = true

            if (!folderTextFiles.exists()) {
                isFolderCreated = folderTextFiles.mkdir()
            }

            return isFolderCreated
        }
    }
}