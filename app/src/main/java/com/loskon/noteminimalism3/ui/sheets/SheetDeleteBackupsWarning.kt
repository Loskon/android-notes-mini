package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Вывод дополнительного подтверждения для удаления
 */

class SheetDeleteBackupsWarning(
    context: Context,
    private val sheetRestoreDateBase: SheetRestoreDateBase
) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)

    private val btnOk: Button = sheetDialog.buttonOk

    init {
        configViews()
        installHandlers()
    }

    private fun configViews() {
        sheetDialog.setTextTitle(R.string.dg_delete_warnings)
        sheetDialog.setContainerVisibility(false)
        sheetDialog.setTextBtnOk(R.string.yes)
        sheetDialog.setTextBtnCancel(R.string.no)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            sheetRestoreDateBase.deleteAll()
            sheetDialog.dismiss()
        }
    }

    fun show() {
        sheetDialog.show()
    }
}