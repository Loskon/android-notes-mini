package com.loskon.noteminimalism3.backup.update

import android.content.Context
import com.loskon.noteminimalism3.files.BackupFilesLimiter
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * Создание файла бэкапа и его восстановление
 */

class DateBaseBackup() {

    companion object {

        fun performBackup(context: Context, outFileName: String) {
            processingBackupOrRestoration(dateBasePath(context), outFileName)
            BackupFilesLimiter.deleteExtraFiles(context)
        }

        fun performRestore(context: Context, inFileName: String) {
            processingBackupOrRestoration(inFileName, dateBasePath(context))
        }

        private fun dateBasePath(context: Context): String {
            return context.getDatabasePath(NoteTable.DATABASE_NAME).toString()
        }

        private fun processingBackupOrRestoration(inFileName: String, outFileName: String) {
            // создать файл
            val dbFile = File(inFileName)
            val fis = FileInputStream(dbFile)

            // путь к внешней резервной копии
            val output: OutputStream = FileOutputStream(outFileName)

            // передача байтов из входного файла в выходной файл
            val buffer = ByteArray(1024)
            var length: Int
            while (fis.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }

            // закрыть потоки
            output.flush()
            output.close()
            fis.close()
        }
    }
}