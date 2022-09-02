package com.loskon.noteminimalism3.files

import android.content.Context
import android.os.Build
import android.os.Environment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.AppPreference
import java.io.File

/**
 * Получение пути папки для хранения данных
 */

object BackupPath {

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

    fun getBackupFolder(context: Context): File {
        return File(getBackupFolderPath(context))
    }

    fun getBackupFolderPath(context: Context): String {
        val fullPath = getPathSelectedDirectory(context)
        val backupFolderName = context.getString(R.string.backup_folder_title)

        return fullPath + File.separator + backupFolderName + File.separator
    }

    @Suppress("DEPRECATION")
    fun getPathSelectedDirectory(context: Context): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString()
        } else {
            AppPreference.getSelectedDirectory(context)
        }
    }

    fun getSummary(context: Context): String {
        var summary = getBackupFolderPath(context)
        summary = summary.replace("//", "/")
        summary = summary.replace("storage/", "")
        return summary
    }
}
