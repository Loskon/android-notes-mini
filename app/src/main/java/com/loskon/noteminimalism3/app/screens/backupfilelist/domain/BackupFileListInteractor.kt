package com.loskon.noteminimalism3.app.screens.backupfilelist.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class BackupFileListInteractor(
    private val backupFileListRepository: BackupFileListRepository
) {

    suspend fun getBackupListFilesAsFlow(folderPath: String): Flow<List<File>?> {
        return backupFileListRepository.getFiles(folderPath).map { files ->
            files?.sortedByDescending { it.lastModified() }
        }
    }

    fun deleteFile(file: File) {
        backupFileListRepository.deleteFile(file)
    }

    fun performRestore(path: String, databasePath: String): Boolean {
        return backupFileListRepository.performRestore(path, databasePath)
    }
}