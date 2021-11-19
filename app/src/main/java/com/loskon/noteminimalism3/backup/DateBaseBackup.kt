package com.loskon.noteminimalism3.backup

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.io.*
import java.nio.channels.FileChannel

/**
 * Создание файла бэкапа и восстановление
 */

class DateBaseBackup {

    companion object {

        fun performBackup(context: Context, outFileName: String) {
            processCopyingDateBaseFile(pathDateBase(context), outFileName)
            BackupFilesLimiter.deleteExtraFiles(context)
        }

        fun performRestore(context: Context, inFileName: String) {
            processCopyingDateBaseFile(inFileName, pathDateBase(context))
        }

        private fun pathDateBase(context: Context): String {
            return context.getDatabasePath(NoteTable.DATABASE_NAME).toString()
        }

        private fun processCopyingDateBaseFile(inFileName: String, outFileName: String) {
            val inFile = File(inFileName)
            val outFile = File(outFileName)

            val inputStreamChannel: FileChannel = FileInputStream(inFile).channel
            val outputStreamChannel: FileChannel = FileOutputStream(outFile).channel

            outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size())

            outputStreamChannel.close()
            inputStreamChannel.close()
        }

        // For android R
        fun performRestore(context: Context, fileUri: Uri): Boolean {
            val resolver: ContentResolver = context.contentResolver
            val descriptor: ParcelFileDescriptor = resolver.openFileDescriptor(fileUri, "r", null)!!
            val fileName: String = context.contentResolver.getFileName(fileUri)
            val pathFile: String = context.cacheDir.path + File.separator + fileName
            processCopyingFileInCacheDir(context, fileName, descriptor)
            return checkingFile(pathFile, pathDateBase(context))
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

        private fun processCopyingFileInCacheDir(
            context: Context,
            fileName: String,
            fileDescriptor: ParcelFileDescriptor
        ) {
            val inputStream: InputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val file = File(context.cacheDir, fileName)
            val outputStream: OutputStream = FileOutputStream(file)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }

        private fun checkingFile(pathFile: String, pathDateBase: String): Boolean {
            return if (isValidSQLiteFile(pathFile)) {
                processCopyingDateBaseFile(pathFile, pathDateBase)
                File(pathFile).delete()
                true
            } else {
                File(pathFile).delete()
                false
            }
        }

        private fun isValidSQLiteFile(path: String): Boolean {
            return try {
                val fr = FileReader(File(path))
                val buffer = CharArray(16)
                fr.read(buffer, 0, 16)
                val str = String(buffer)
                fr.close()
                str == "SQLite format 3\u0000"
            } catch (exception: Exception) {
                false
            }
        }
    }
}