package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Вывод подтверждения для удаления всех файлов бэкапа
 */

class SheetDeleteBackupsFiles(
    context: Context,
    private val sheetListRestoreDateBase: SheetListRestoreDateBase
) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)

    private val btnOk: Button = dialog.buttonOk

    init {
        configViews()
        installHandlers()
    }

    private fun configViews() {
        dialog.setTextTitle(R.string.sheet_delete_warnings)
        dialog.setContainerVisibility(false)
        dialog.setTextBtnOk(R.string.yes)
        dialog.setTextBtnCancel(R.string.no)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            sheetListRestoreDateBase.deleteAll()
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}