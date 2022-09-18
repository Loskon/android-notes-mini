package com.loskon.noteminimalism3.app.screens.backup.domain

import java.io.File
import java.io.FileDescriptor

class LocalFileInteractor(
    private val localFileRepository: LocalFileRepository
) {

    fun copyFileInCacheDir(fileDescriptor: FileDescriptor, fileName: String, cacheDir: File): Boolean {
        return localFileRepository.copyFileInCacheDir(fileDescriptor, fileName, cacheDir)
    }

    fun validSQLiteFile(backupFile: String): Boolean {
        return localFileRepository.validSQLiteFile(backupFile)
    }

    fun restore(backupFile: String, databasePath: String): Boolean {
        return localFileRepository.restore(backupFile, databasePath)
    }

    fun deleteFile(file: String) {
        localFileRepository.deleteFile(file)
    }
}