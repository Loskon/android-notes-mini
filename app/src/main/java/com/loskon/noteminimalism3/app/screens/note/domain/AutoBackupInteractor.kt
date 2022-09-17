package com.loskon.noteminimalism3.app.screens.note.domain

import java.io.File

class AutoBackupInteractor(
    private val repository: AutoBackupRepository
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

    fun createTextFile(file: File, fileTitle: String, text: String): Boolean {
        return repository.createTextFile(file, fileTitle, text)
    }
}