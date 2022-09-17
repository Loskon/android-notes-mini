package com.loskon.noteminimalism3.app.screens.createbackup.domain

class CreateBackupInteractor(
    private val repository: CreateBackupRepository
) {

    fun folderCreated(folderPath: String): Boolean {
        return repository.folderCreated(folderPath)
    }

    fun performBackup(databasePath: String, backupFilePath: String): Boolean {
        return repository.performBackup(databasePath, backupFilePath)
    }

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        repository.deleteExtraFiles(backupFolderPath, maxFilesCount)
    }
}