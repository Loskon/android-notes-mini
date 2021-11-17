package com.loskon.noteminimalism3.backup

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel

/**
 * Создание файла бэкапа и восстановление
 */

class DateBaseBackup {

    companion object {

        fun performBackup(context: Context, outFileName: String) {
            processCopyingFileTransfer(pathDateBase(context), outFileName)
            BackupFilesLimiter.deleteExtraFiles(context)
        }

        fun performRestore(context: Context, inFileName: String) {
            processCopyingFileTransfer(inFileName, pathDateBase(context))
        }

        fun performRestore(context: Context, data: Uri) {
            val resolver: ContentResolver = context.contentResolver
            val descriptor: ParcelFileDescriptor? = resolver.openFileDescriptor(data, "r", null)
            descriptor?.let { processCopyingFile(context, descriptor) }
        }

        private fun pathDateBase(context: Context): String {
            return context.getDatabasePath(NoteTable.DATABASE_NAME).toString()
        }

/*        private fun processCopyingFileBuffer(inFileName: String, outFileName: String) {
            val file = File(inFileName)

            val inputStream: InputStream = FileInputStream(file)
            val outputStream: OutputStream = FileOutputStream(outFileName)

            val buffer = ByteArray(1024)
            var length: Int

            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }*/

        private fun processCopyingFileTransfer(inFileName: String, outFileName: String) {
            val inFile = File(inFileName)
            val outFile = File(outFileName)

            val inputStreamChannel: FileChannel = FileInputStream(inFile).channel
            val outputStreamChannel: FileChannel = FileOutputStream(outFile).channel

            outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size())

            outputStreamChannel.close()
            inputStreamChannel.close()
        }

        private fun processCopyingFile(context: Context, fileDescriptor: ParcelFileDescriptor) {
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val file = File(pathFolderDateBase(context), NoteDateBaseSchema.DATABASE_NAME)

            val outputStream = FileOutputStream(file)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }

        private fun pathFolderDateBase(context: Context): String {
            val path: String = pathDateBase(context)
            return path.replace(NoteDateBaseSchema.DATABASE_NAME, "")
        }
    }
}