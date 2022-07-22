package com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.data

import android.content.Context
import com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.domain.BackupFileListRepository
import com.loskon.noteminimalism3.files.BackupPath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class BackupFileListRepositoryImpl(
    private val context: Context
) : BackupFileListRepository {

    override suspend fun getFiles(): Flow<List<File>?> {
        return flow {
            val backupFolder = BackupPath.getBackupFolder(context)

            val files = if (backupFolder.exists()) {
                backupFolder.listFiles { _, name -> name.lowercase().endsWith(DATABASE_SUFFIX) }?.toList()
            } else {
                null
            }

            emit(files)
        }
    }

    override suspend fun deleteFile(file: File) {
        file.delete()
    }

    suspend fun deleteAll() {

    }

    companion object {
        private const val DATABASE_SUFFIX = ".db"
    }
}