package com.loskon.noteminimalism3.app.presentation.screens.note.data

import android.database.sqlite.SQLiteDatabase
import timber.log.Timber
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter

class LocaleFileSource {

    fun copyFile(inFile: File, outFile: File): Boolean {
        return try {
            val inputStreamChannel = FileInputStream(inFile).channel
            val outputStreamChannel = FileOutputStream(outFile).channel

            outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size())
            outputStreamChannel.close()
            inputStreamChannel.close()
            true
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    fun copyDescriptorFile(descriptor: FileDescriptor, outFile: File): Boolean {
        return try {
            val inputStream = FileInputStream(descriptor)
            val outputStream = FileOutputStream(outFile)

            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)

            outputStream.flush()
            outputStream.close()
            inputStream.close()
            true
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    fun deleteExtraFiles(backupFolder: File, maxFilesCount: Int): Boolean {
        return try {
            val files = getBackupFiles(backupFolder)

            if (files != null && files.size > maxFilesCount) {
                val deletedFiles = files.sortedBy { it.lastModified() }.take(files.size - maxFilesCount)
                for (file in deletedFiles) SQLiteDatabase.deleteDatabase(File(file.path))
            }

            true
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    private fun getBackupFiles(folder: File): Array<File>? {
        return folder.listFiles { _, name: String -> name.lowercase().endsWith(BACKUP_FILE_SUFFIX) }
    }

    fun deleteDatabaseFile(dbFile: File) {
        SQLiteDatabase.deleteDatabase(dbFile)
    }

    fun folderCreated(folder: File): Boolean {
        return if (folder.exists().not()) folder.mkdirs() else false
    }

    fun validSQLiteFile(path: String): Boolean {
        return try {
            val fileReader = FileReader(File(path))
            val buffer = CharArray(16)
            fileReader.read(buffer, 0, 16)
            val string = String(buffer)
            fileReader.close()
            string == "SQLite format 3\u0000"
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    fun createTextFile(file: File, text: String): Boolean {
        return try {
            val writer = FileWriter(file)

            writer.append(text)
            writer.flush()
            writer.close()

            true
        } catch (exception: Exception) {
            Timber.e(exception)
            false
        }
    }

    companion object {
        private const val BACKUP_FILE_SUFFIX = ".db"
    }
}