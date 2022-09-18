package com.loskon.noteminimalism3.app.screens.backup.domain

import java.io.File
import java.io.FileDescriptor

interface LocalFileRepository {
    fun copyFileInCacheDir(fileDescriptor: FileDescriptor, fileName: String, cacheDir: File): Boolean
    fun validSQLiteFile(backupFile: String): Boolean
    fun restore(backupFile: String, databasePath: String): Boolean
    fun deleteFile(file: String)
}