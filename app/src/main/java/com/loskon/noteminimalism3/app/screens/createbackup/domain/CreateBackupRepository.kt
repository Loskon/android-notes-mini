package com.loskon.noteminimalism3.app.screens.createbackup.domain

interface CreateBackupRepository {
    fun folderCreated(folderPath: String): Boolean
    fun performBackup(databasePath: String, backupFilePath: String): Boolean
    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int)
}