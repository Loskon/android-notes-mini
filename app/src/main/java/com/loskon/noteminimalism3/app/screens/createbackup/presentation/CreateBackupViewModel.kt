package com.loskon.noteminimalism3.app.screens.createbackup.presentation

import com.loskon.noteminimalism3.app.screens.createbackup.domain.CreateBackupInteractor
import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel

class CreateBackupViewModel(
    private val createBackupInteractor: CreateBackupInteractor
): BaseViewModel() {

    fun backupFolderCreated(backupPath: String): Boolean {
        return createBackupInteractor.folderCreated(backupPath)
    }

    fun performBackup(databasePath: String, backupFilePath: String): Boolean {
        return createBackupInteractor.performBackup(databasePath, backupFilePath)
    }

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        createBackupInteractor.deleteExtraFiles(backupFolderPath, maxFilesCount)
    }
}