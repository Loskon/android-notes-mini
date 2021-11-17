package com.loskon.noteminimalism3.backup

import android.content.Context
import android.os.Build
import android.os.Environment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import java.io.File

/**
 *
 */

class BackupPathManager {

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

        @Suppress("DEPRECATION")
        fun getPathBackupFolder(context: Context): String {
            var fullPath: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                    .toString()
            } else {
                PrefManager.getSelectedDirectory(context)
            }

            val backupFolder = context.getString(R.string.app_name_backup)
            fullPath = fullPath + File.separator + backupFolder + File.separator
            return fullPath
        }

        // Получение файлов из папки
        fun getBackupFolder(context: Context): File {
            return File(getPathBackupFolder(context))
        }

        // Путь для Summary в виде текста'
        fun getPathSummary(context: Context): String {
            var summary = getPathBackupFolder(context)
            summary = summary.replace("//", "/")
            summary = summary.replace("storage/", "")
            return summary
        }

    }
}