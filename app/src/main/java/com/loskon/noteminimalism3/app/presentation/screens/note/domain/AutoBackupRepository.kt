package com.loskon.noteminimalism3.app.presentation.screens.note.domain

import java.io.File

interface AutoBackupRepository {
    fun folderCreated(folderPath: String): Boolean
    fun performBackup(databasePath: String, backupFilePath: String)
    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int)
    fun createTextFile(file: File, fileTitle: String, text: String): Boolean
}