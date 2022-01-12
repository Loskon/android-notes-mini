package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupFiles
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.io.File

/**
 * Окно для подтверждения удаления всех файлов бэкапа
 */

class DeleteBackupsFilesSheetDialog(
    sheetContext: Context,
    private val activity: SettingsActivity
) :
    BaseSheetDialog(sheetContext, null) {

    init {
        configureDialogParameters()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_delete_warnings)
        setContainerVisibility(false)
        setTextBtnOk(R.string.yes)
        setTextBtnCancel(R.string.no)
    }

    private fun installHandlersForViews() {
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        deleteAllBackupFiles()
        dismiss()
    }

    private fun deleteAllBackupFiles() {
        try {
            val backupFiles: Array<File>? = files

            if (backupFiles != null) {
                for (file in backupFiles) {
                    file.delete()
                }
            }

            activity.showSnackbar(WarningSnackbar.MSG_BACKUP_FILES_DELETED)
        } catch (exception: Exception) {
            activity.showSnackbar(WarningSnackbar.MSG_UNKNOWN_ERROR)
        }
    }

    private val files: Array<File>?
        get() {
            return BackupFiles.getList(context)
        }
}