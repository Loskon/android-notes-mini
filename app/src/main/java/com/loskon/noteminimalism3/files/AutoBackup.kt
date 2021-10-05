package com.loskon.noteminimalism3.files

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.backup.second.BackupDb
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.toast.ToastApp
import com.loskon.noteminimalism3.toast.ToastApp.Companion.MSG_TOAST_AUTO_BACKUP_COMPLETED
import com.loskon.noteminimalism3.toast.ToastApp.Companion.MSG_TOAST_AUTO_BACKUP_FAILED
import com.loskon.noteminimalism3.toast.ToastApp.Companion.MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE
import com.loskon.noteminimalism3.toast.ToastApp.Companion.MSG_TOAST_UNABLE_CREATE_FILE
import com.loskon.noteminimalism3.utils.DateUtils
import com.loskon.noteminimalism3.utils.createFolder
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/**
 *
 */

class AutoBackup(private val context: Context) {

    fun createBackupFile(date: Date, isShowToast: Boolean) =
        (context as AppCompatActivity).lifecycleScope.launch {
            val isAccess: Boolean = PermissionsStorageUpdate.hasAccessStorage(context)

            if (isAccess) {

                val folder = BackupPath.getFolder(context)
                val hasCreatedFolder = folder.createFolder()

                if (hasCreatedFolder) {
                    mainMethodBuildFile(date,isShowToast)
                } else {
                    showToast(MSG_TOAST_UNABLE_CREATE_FILE,isShowToast)
                }

            } else {
                showToast(MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE,isShowToast)
            }
        }

    private fun mainMethodBuildFile(date: Date, isShowToast: Boolean) {
        try {
            val backupName: String = getName(date)
            val filePath: String = BackupPath.getPath(context) + File.separator
            val outFileName = "$filePath$backupName.db"

            BackupDb(context).backupDatabase(true, outFileName)

            val hasNotification = GetSharedPref.hasNotificationAutoBackup(context)
            if (hasNotification) showToast(MSG_TOAST_AUTO_BACKUP_COMPLETED,isShowToast)

        } catch (exception: Exception) {
            exception.printStackTrace()
            showToast(MSG_TOAST_AUTO_BACKUP_FAILED,isShowToast)
        }
    }

    private fun getName(date: Date): String {
        var name: String = DateUtils.getStringDate(date)

        name = name.replace("[./]".toRegex(), "_")
        name = name.replace(":", "-")

        return "$name (A)"
    }

    private fun showToast(typeMessage: String, isShowToast: Boolean) {
      if (isShowToast)  ToastApp(context).show(typeMessage)
    }
}