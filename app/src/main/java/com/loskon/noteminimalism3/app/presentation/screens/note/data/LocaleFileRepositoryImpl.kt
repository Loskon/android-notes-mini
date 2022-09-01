package com.loskon.noteminimalism3.app.presentation.screens.note.data

import android.database.sqlite.SQLiteDatabase
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.LocaleFileRepository
import java.io.File
import java.io.FileDescriptor

class LocaleFileRepositoryImpl(
    private val localeFileSource: LocaleFileSource
) : LocaleFileRepository {

    override fun performBackup(dbFilePath: String, backupFilePath: String) {
        val dbFile = File(dbFilePath)
        val backupFile = File(backupFilePath)

        localeFileSource.copyFile(dbFile, backupFile)
    }

    override fun performRestore(backupFilePath: String, dbFilePath: String) {
        val backupFile = File(backupFilePath)
        val dbFile = File(dbFilePath)

        localeFileSource.copyFile(backupFile, dbFile)
    }

    fun performRestoreAndroidR(
        descriptor: FileDescriptor,
        fileName: String,
        dbFilePath: String,
        cachePath: String,
    ): Boolean {
        val pathFile = cachePath + File.separator + fileName

        if (localeFileSource.copyDescriptorFile(descriptor, File(cachePath, fileName))) {
            return true
        } else {
            return false
        }

        // return checkingAndCopyingDataBaseFile(pathFile, dbFilePath)
    }

    private fun checkingAndCopyingDataBaseFile(pathFile: String, pathDateBase: String): Boolean {
        return if (localeFileSource.validSQLiteFile(pathFile)) {
            localeFileSource.copyFile(File(pathFile), File(pathDateBase))
            SQLiteDatabase.deleteDatabase(File(pathFile))
            true
        } else {
            SQLiteDatabase.deleteDatabase(File(pathFile))
            false
        }
    }
}