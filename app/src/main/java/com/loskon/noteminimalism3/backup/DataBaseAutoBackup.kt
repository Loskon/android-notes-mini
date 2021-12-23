package com.loskon.noteminimalism3.backup

import android.content.Context
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.files.CheckCreatedFile
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorage
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.toast.ToastControl
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.StringUtil
import java.io.File
import java.util.*

/**
 * Автоматическое создание файла бэкапа
 */

class DataBaseAutoBackup {

    companion object {

        fun createBackupFile(context: Context, date: Date, isShowToast: Boolean) {
            val isAccess: Boolean = ResultAccessStorage.hasAccessStorage(context)

            if (isAccess) {

                val folder: File = BackupPath.getBackupFolder(context)
                val hasCreatedFolder: Boolean = CheckCreatedFile.hasCreated(folder)

                if (hasCreatedFolder) {
                    performAutoBackup(context, date, isShowToast)
                } else {
                    showToast(context, ToastControl.MSG_UNABLE_CREATE_FOLDER, isShowToast)
                }

            } else {
                showToast(context, ToastControl.MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE, isShowToast)
            }
        }

        private fun performAutoBackup(context: Context, date: Date, isShowToast: Boolean) {
            val backupPath: String = BackupPath.getPathBackupFolder(context)
            val backupName: String = StringUtil.replaceForbiddenCharacters(date)
            val outFileName = "$backupPath$backupName.db"

            try {
                DataBaseBackup.performBackup(context, outFileName)
                showToastNotification(context, isShowToast)
            } catch (exception: Exception) {
                showToast(context, ToastControl.MSG_TOAST_AUTO_BACKUP_FAILED, isShowToast)
            }
        }

        private fun replaceForbiddenCharacters(date: Date): String {
            var name: String = DateUtil.getStringDate(date)
            name = name.replace("[./:]".toRegex(), "_")
            return "$name (A)"
        }

        private fun showToast(context: Context, typeMessage: String, isShowToast: Boolean) {
            if (isShowToast) ToastControl.show(context, typeMessage)
        }

        private fun showToastNotification(context: Context, isShowToast: Boolean) {
            val hasNotification: Boolean = PrefManager.hasNotificationAutoBackup(context)

            if (hasNotification) {
                showToast(context, ToastControl.MSG_TOAST_AUTO_BACKUP_COMPLETED, isShowToast)
            }
        }
    }
}