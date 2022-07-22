package com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File

class BackupFileListInteractor(
    private val backupFileListRepository: BackupFileListRepository
) {

    suspend fun getBackupListFilesAsFlow(): Flow<List<File>?> {
        return backupFileListRepository.getFiles().map { files ->
            files?.sortedByDescending { it.lastModified() }
        }
    }

    suspend fun deleteFile(file: File) {
        backupFileListRepository.deleteFile(file)
    }
}