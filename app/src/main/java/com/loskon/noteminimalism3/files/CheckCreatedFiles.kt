package com.loskon.noteminimalism3.files

import android.content.Context
import com.loskon.noteminimalism3.backup.BackupPathManager
import java.io.File

/**
 * Создание папок
 */

class CheckCreatedFiles {
    companion object {

        fun createBackupFolder(context: Context): Boolean {
            val folder: File = BackupPathManager.getBackupFolder(context)

            var isFolderCreated = true

            if (!folder.exists()) {
                isFolderCreated = folder.mkdirs()
            }

            return isFolderCreated
        }

        fun checkCreatedFolder(folderTextFiles: File): Boolean {
            var isFolderCreated = true

            if (!folderTextFiles.exists()) {
                isFolderCreated = folderTextFiles.mkdirs()
            }

            return isFolderCreated
        }
    }
}