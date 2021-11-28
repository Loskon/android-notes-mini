package com.loskon.noteminimalism3.backup

import android.content.Context
import com.loskon.noteminimalism3.files.CheckCreatedFiles
import com.loskon.noteminimalism3.request.storage.ResultAccessStorage
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.toast.ToastManager
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.StringUtil
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

                val folder: File = BackupPathManager.getBackupFolder(context)
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
            val backupName: String = StringUtil.replaceForbiddenCharactersForAuto(date)
            //val filePath: String = BackupPath.getPath(context) + File.separator
            val backupPath: String = BackupPathManager.getPathBackupFolder(context)
            val outFileName = "$backupPath$backupName.db"

            try {
                DateBaseBackup.performBackup(context, outFileName)
                showToastNotification(context, isShowToast)
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

        private fun showToastNotification(context: Context, isShowToast: Boolean) {
            val hasNotification: Boolean = PrefManager.hasNotificationAutoBackup(context)

            if (hasNotification) {
                showToast(context, ToastManager.MSG_TOAST_AUTO_BACKUP_COMPLETED, isShowToast)
            }
        }
    }
}