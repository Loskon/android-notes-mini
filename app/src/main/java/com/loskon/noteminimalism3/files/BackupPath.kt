package com.loskon.noteminimalism3.files

import android.content.Context
import android.os.Build
import android.os.Environment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import java.io.File

/**
 * Получение пути папки для хранения данных
 */

class BackupPath {
    companion object {

        // Дает абсолютный путь от дерева uri
        fun findFullPath(uriPath: String): String {
            var path = uriPath

            val actualPAth: String
            var index = 0

            path = path.substring(5)

            val result = StringBuilder("/storage")

            run {
                var i = 0

                while (i < path.length) {

                    if (path[i] != ':') {
                        result.append(path[i])
                    } else {
                        index = ++i
                        result.append('/')
                        break
                    }

                    i++
                }
            }

            for (i in index until path.length) {
                result.append(path[i])
            }

            actualPAth = if (result.substring(9, 16).equals("primary", ignoreCase = true)) {
                result.substring(0, 8) + "/emulated/0/" + result.substring(17)
            } else {
                result.toString()
            }

            return actualPAth
        }

        fun getPathBackupFolder(context: Context): String {
            var fullPath: String = getPathSelectedDirectory(context)
            val titleBackupFolder: String = context.getString(R.string.folder_backups_name)
            fullPath = fullPath + File.separator + titleBackupFolder + File.separator
            return fullPath
        }

        @Suppress("DEPRECATION")
        fun getPathSelectedDirectory(context: Context): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val documents: String = Environment.DIRECTORY_DOCUMENTS
                Environment.getExternalStoragePublicDirectory(documents).toString()
            } else {
                PrefHelper.getSelectedDirectory(context)
            }
        }

        fun getBackupFolder(context: Context): File {
            return File(getPathBackupFolder(context))
        }

        fun getSummary(context: Context): String {
            var summary = getPathBackupFolder(context)
            summary = summary.replace("//", "/")
            summary = summary.replace("storage/", "")
            return summary
        }
    }
}