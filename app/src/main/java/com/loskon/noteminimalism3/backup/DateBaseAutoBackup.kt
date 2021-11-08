package com.loskon.noteminimalism3.backup

import android.content.Context
import com.loskon.noteminimalism3.files.CheckCreatedFiles
import com.loskon.noteminimalism3.request.permissions.ResultAccessStorage
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.toast.ToastManager
import com.loskon.noteminimalism3.utils.DateUtil
import java.io.File
import java.util.*

/**
 * Создание файла бэкапа c определенным названием
 */

class DateBaseAutoBackup {

    companion object {

        fun createBackupFile(context: Context, date: Date, isShowToast: Boolean) {
            val isAccess: Boolean = ResultAccessStorage.hasAccessStorage(context)

            if (isAccess) {

                val folder: File = BackupPath.getFolderBackup(context)
                val hasCreatedFolder: Boolean = CheckCreatedFiles.checkCreatedFolder(folder)

                if (hasCreatedFolder) {
                    performAutoBackup(context, date, isShowToast)
                } else {
                    showToast(context, ToastManager.MSG_TOAST_UNABLE_CREATE_FILE, isShowToast)
                }

            } else {
                showToast(context, ToastManager.MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE, isShowToast)
            }
        }

        private fun performAutoBackup(context: Context, date: Date, isShowToast: Boolean) {
            try {
                val backupName: String = replaceForbiddenCharacters(date)
                val filePath: String = BackupPath.getPath(context) + File.separator
                val outFileName = "$filePath$backupName.db"

                DateBaseBackup.performBackup(context, outFileName)

                val hasNotification: Boolean = PrefManager.hasNotificationAutoBackup(context)

                if (hasNotification) {
                    showToast(context, ToastManager.MSG_TOAST_AUTO_BACKUP_COMPLETED, isShowToast)
                }

            } catch (exception: Exception) {
                showToast(context, ToastManager.MSG_TOAST_AUTO_BACKUP_FAILED, isShowToast)
            }
        }

        private fun replaceForbiddenCharacters(date: Date): String {
            var name: String = DateUtil.getStringDate(date)
            name = name.replace("[./:]".toRegex(), "_")
            return "$name (A)"
        }

        private fun showToast(context: Context, typeMessage: String, isShowToast: Boolean) {
            if (isShowToast) ToastManager.show(context, typeMessage)
        }
    }
}