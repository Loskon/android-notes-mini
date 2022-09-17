package com.loskon.noteminimalism3.app.screens.backupfilelist.domain

import kotlinx.coroutines.flow.Flow
import java.io.File

interface BackupFileListRepository {

    suspend fun getFiles(): Flow<List<File>?>

    suspend fun deleteFile(file: File)
}