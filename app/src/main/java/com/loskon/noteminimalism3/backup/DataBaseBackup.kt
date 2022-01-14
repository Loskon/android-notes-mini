package com.loskon.noteminimalism3.backup

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import com.loskon.noteminimalism3.files.BackupFilesLimiter
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.io.*
import java.nio.channels.FileChannel

/**
 * Создание локальных файлов бэкапа и восстановление базы данных с помощью копирования файлов
 */

object DataBaseBackup {

    fun performBackup(context: Context, outFileName: String) {
        copyFile(pathDateBase(context), outFileName)
        BackupFilesLimiter.deleteExtraFiles(context)
    }

    fun performRestore(context: Context, inFileName: String) {
        copyFile(inFileName, pathDateBase(context))
    }

    private fun pathDateBase(context: Context): String {
        return context.getDatabasePath(NoteTable.DATABASE_NAME).toString()
    }

    private fun copyFile(inFileName: String, outFileName: String) {
        val inFile = File(inFileName)
        val outFile = File(outFileName)

        val inputStreamChannel: FileChannel = FileInputStream(inFile).channel
        val outputStreamChannel: FileChannel = FileOutputStream(outFile).channel

        outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size())

        outputStreamChannel.close()
        inputStreamChannel.close()
    }

    //--- Android R ----------------------------------------------------------------------------
    fun performRestore(context: Context, fileUri: Uri): Boolean {
        val resolver: ContentResolver = context.contentResolver
        val descriptor: ParcelFileDescriptor = resolver.openFileDescriptor(fileUri, "r", null)!!
        val fileName: String = context.contentResolver.getFileName(fileUri)
        val pathFile: String = context.cacheDir.path + File.separator + fileName

        copyFileInCacheDir(context, fileName, descriptor)
        return checkingAndCopyingDataBaseFile(pathFile, pathDateBase(context))
    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""

        val cursor: Cursor? = this.query(fileUri, null, null, null, null)

        if (cursor != null) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            name = cursor.getString(nameIndex)
            cursor.close()
        }

        return name
    }

    private fun copyFileInCacheDir(
        context: Context,
        fileName: String,
        fileDescriptor: ParcelFileDescriptor
    ) {
        val inputStream: InputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val outputStream: OutputStream = FileOutputStream(File(context.cacheDir, fileName))

        val buffer = ByteArray(1024)
        var length: Int

        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
    }

    private fun checkingAndCopyingDataBaseFile(
        pathFile: String,
        pathDateBase: String
    ): Boolean {
        return if (isValidSQLiteFile(pathFile)) {
            copyFile(pathFile, pathDateBase)
            File(pathFile).delete()
            true
        } else {
            File(pathFile).delete()
            false
        }
    }

    private fun isValidSQLiteFile(path: String): Boolean {
        return try {
            val fileReader = FileReader(File(path))
            val buffer = CharArray(16)
            fileReader.read(buffer, 0, 16)
            val str = String(buffer)
            fileReader.close()
            str == "SQLite format 3\u0000"
        } catch (exception: Exception) {
            false
        }
    }
}
