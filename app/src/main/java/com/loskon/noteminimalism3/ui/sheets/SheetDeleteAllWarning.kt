package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Вывод дополнительного подтверждения для удаления
 */

class SheetDeleteAllWarning(
    context: Context,
    private val sheetListFiles: SheetListFiles
) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)

    private val btnOk: Button = sheetDialog.getButtonOk

    init {
        configViews()
        installHandlers()
    }

    private fun configViews() {
        sheetDialog.setTextTitle(R.string.confirm)
        sheetDialog.setContainerVisibility(false)
        sheetDialog.setTextBtnOk(R.string.yes)
        sheetDialog.setTextBtnCancel(R.string.no)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            sheetListFiles.deleteAll()
            sheetDialog.dismiss()
        }
    }

    fun show() {
        sheetDialog.show()
    }
}