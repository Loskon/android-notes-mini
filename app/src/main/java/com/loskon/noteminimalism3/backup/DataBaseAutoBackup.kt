package com.loskon.noteminimalism3.backup

import android.content.Context
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.files.CheckCreatedFile
import com.loskon.noteminimalism3.requests.storage.ResultStorageAccess
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.toast.WarningToast
import com.loskon.noteminimalism3.utils.StringUtil
import java.io.File
import java.util.*

/**
 * Автоматическое создание файла бэкапа
 */

object DataBaseAutoBackup {

    fun checkingStorageAccess(
        context: Context,
        date: Date,
        isShowToast: Boolean,
        storageAccess: ResultStorageAccess
    ) {
        val hasAccessStorage: Boolean = storageAccess.hasAccessStorage()

        if (hasAccessStorage) {
            creatingBackupFolder(context, date, isShowToast)
        } else {
            showToast(context, WarningToast.MSG_TOAST_AUTO_BACKUP_NO_PERMISSIONS, isShowToast)
        }
    }

    private fun creatingBackupFolder(context: Context, date: Date, isShowToast: Boolean) {
        val folder: File = BackupPath.getBackupFolder(context)
        val hasCreatedFolder: Boolean = CheckCreatedFile.hasCreated(folder)

        if (hasCreatedFolder) {
            creatingBackupFile(context, date, isShowToast)
        } else {
            showToast(context, WarningToast.MSG_TOAST_UNABLE_CREATE_FOLDER, isShowToast)
        }
    }

    private fun creatingBackupFile(context: Context, date: Date, isShowToast: Boolean) {
        val backupPath: String = BackupPath.getPathBackupFolder(context)
        val backupName: String = StringUtil.replaceForbiddenCharacters(date)
        val outFileName = "$backupPath$backupName.db"

        try {
            DataBaseBackup.performBackup(context, outFileName)
            if (isShowToast) showSuccessNotification(context)
        } catch (exception: Exception) {
            showToast(context, WarningToast.MSG_TOAST_AUTO_BACKUP_FAILED, isShowToast)
        }
    }

    private fun showToast(context: Context, messageType: String, isShowToast: Boolean) {
        if (isShowToast) WarningToast.show(context, messageType)
    }

    private fun showSuccessNotification(context: Context) {
        val hasNotification: Boolean = PrefHelper.hasNotificationAutoBackup(context)

        if (hasNotification) {
            showToast(context, WarningToast.MSG_TOAST_AUTO_BACKUP_COMPLETED, true)
        }
    }
}
