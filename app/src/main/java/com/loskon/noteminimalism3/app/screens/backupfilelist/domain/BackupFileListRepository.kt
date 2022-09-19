package com.loskon.noteminimalism3.app.screens.backupfilelist.domain

import kotlinx.coroutines.flow.Flow
import java.io.File

interface BackupFileListRepository {
    suspend fun getFiles(folderPath: String): Flow<List<File>?>
    fun deleteFile(file: File)
    fun performRestore(path: String, databasePath: String): Boolean
}