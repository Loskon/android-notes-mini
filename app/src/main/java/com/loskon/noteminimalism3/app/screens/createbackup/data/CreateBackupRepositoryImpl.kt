package com.loskon.noteminimalism3.app.screens.createbackup.data

import com.loskon.noteminimalism3.app.screens.createbackup.domain.CreateBackupRepository
import com.loskon.noteminimalism3.app.screens.note.data.LocaleFileSource
import java.io.File

class CreateBackupRepositoryImpl(
    private val localeFileSource: LocaleFileSource
): CreateBackupRepository {

    override fun folderCreated(folderPath: String): Boolean {
        val folder = File(folderPath)
        return localeFileSource.folderCreated(folder)
    }

    override fun performBackup(databasePath: String, backupFilePath: String): Boolean {
        val dbFile = File(databasePath)
        val backupFile = File(backupFilePath)
        return localeFileSource.copyFile(dbFile, backupFile)
    }

    override fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        val backupFolder = File(backupFolderPath)
        localeFileSource.deleteExtraFiles(backupFolder, maxFilesCount)
    }
}