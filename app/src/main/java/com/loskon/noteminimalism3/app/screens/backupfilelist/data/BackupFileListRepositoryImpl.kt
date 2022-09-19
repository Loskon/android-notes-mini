package com.loskon.noteminimalism3.app.screens.backupfilelist.data

import com.loskon.noteminimalism3.app.screens.backupfilelist.domain.BackupFileListRepository
import com.loskon.noteminimalism3.app.screens.note.data.LocaleFileSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class BackupFileListRepositoryImpl(
    private val localeFileSource: LocaleFileSource
) : BackupFileListRepository {

    override suspend fun getFiles(folderPath: String): Flow<List<File>?> {
        return flow {
            val backupFolder = File(folderPath)

            val files = if (backupFolder.exists()) {
                backupFolder.listFiles { _, name -> name.lowercase().endsWith(DATABASE_SUFFIX) }?.toList()
            } else {
                null
            }

            emit(files)
        }
    }

    override fun deleteFile(file: File) {
        localeFileSource.deleteDatabaseFile(file)
    }

    override fun performRestore(path: String, databasePath: String): Boolean {
        return localeFileSource.copyFile(File(path), File(databasePath))
    }

    companion object {
        private const val DATABASE_SUFFIX = ".db"
    }
}