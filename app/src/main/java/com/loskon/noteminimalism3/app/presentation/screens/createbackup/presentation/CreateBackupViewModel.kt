package com.loskon.noteminimalism3.app.presentation.screens.createbackup.presentation

import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.createbackup.domain.CreateBackupInteractor

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