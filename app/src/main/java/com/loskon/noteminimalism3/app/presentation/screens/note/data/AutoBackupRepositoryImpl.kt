package com.loskon.noteminimalism3.app.presentation.screens.note.data

import com.loskon.noteminimalism3.app.presentation.screens.note.domain.AutoBackupRepository
import java.io.File

class AutoBackupRepositoryImpl(
    private val localeFileSource: LocaleFileSource
) : AutoBackupRepository {

    override fun folderCreated(folderPath: String): Boolean {
        val folder = File(folderPath)

        return localeFileSource.folderCreated(folder)
    }

    override fun performBackup(databasePath: String, backupFilePath: String): Boolean {
        val dbFile = File(databasePath)
        val backupFile = File(backupFilePath)

        return localeFileSource.copyFile(dbFile, backupFile)
    }

    override fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        val backupFolder = File(backupFolderPath)

        localeFileSource.deleteExtraFiles(backupFolder, maxFilesCount)
    }

    override fun createTextFile(file: File, fileTitle: String, text: String): Boolean {
        val textFile = File(file, fileTitle)

        return localeFileSource.createTextFile(textFile, text)
    }

    /*    override fun performRestore(backupFilePath: String, dbFilePath: String) {
            val backupFile = File(backupFilePath)
            val dbFile = File(dbFilePath)

            localeFileSource.copyFile(backupFile, dbFile)
        }

        fun validSQLiteFile(descriptor: FileDescriptor, fileName: String, cachePath: String): Boolean {
            val pathFile = cachePath + File.separator + fileName

            return if (localeFileSource.copyDescriptorFile(descriptor, File(cachePath, fileName))) {
                localeFileSource.validSQLiteFile(pathFile)
            } else {
                false
            }
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
        }*/
}