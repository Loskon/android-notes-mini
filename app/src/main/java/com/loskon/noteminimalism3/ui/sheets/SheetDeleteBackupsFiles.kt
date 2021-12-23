package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupFiles
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import java.io.File

/**
 * Вывод подтверждения для удаления всех файлов бэкапа
 */

class SheetDeleteBackupsFiles(private val context: Context) {

    private val dialog: BaseSheetDialogs = BaseSheetDialogs(context)

    init {
        dialog.setTextTitle(R.string.sheet_delete_warnings)
        dialog.setContainerVisibility(false)
        dialog.setTextBtnOk(R.string.yes)
        dialog.setTextBtnCancel(R.string.no)
    }

    fun show() {
        installHandlers()
        dialog.show()
    }

    private fun installHandlers() {
        dialog.buttonOk.setOnSingleClickListener {
            deleteAllBackupFiles(files)
            dialog.dismiss()
        }
    }

    private fun deleteAllBackupFiles(files: Array<File>?) {
        val backupFiles: Array<File>? = files

        if (backupFiles != null) {
            for (file in backupFiles) {
                file.delete()
            }
        }

    }

    private val files: Array<File>?
        get() {
            return BackupFiles.getList(context)
        }
}