package com.loskon.noteminimalism3.app.screens.backup.data

import com.loskon.noteminimalism3.app.screens.backup.domain.LocalFileRepository
import com.loskon.noteminimalism3.app.screens.note.data.LocaleFileSource
import java.io.File
import java.io.FileDescriptor

class LocalFileRepositoryImpl(
    private val localeFileSource: LocaleFileSource
) : LocalFileRepository {

    override fun copyFileInCacheDir(fileDescriptor: FileDescriptor, fileName: String, cacheDir: File): Boolean {
        val outFile = File(cacheDir, fileName)
        return localeFileSource.copyDescriptorFile(fileDescriptor, outFile)
    }

    override fun validSQLiteFile(backupFile: String): Boolean {
        return localeFileSource.validSQLiteFile(backupFile)
    }

    override fun restore(backupFile: String, databasePath: String): Boolean {
        return localeFileSource.copyFile(File(backupFile), File(databasePath))
    }

    override fun deleteFile(file: String) {
        localeFileSource.deleteDatabaseFile(File(file))
    }
}