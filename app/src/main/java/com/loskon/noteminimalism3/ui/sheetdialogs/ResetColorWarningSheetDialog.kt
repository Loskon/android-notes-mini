package com.loskon.noteminimalism3.ui.sheetdialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenResetColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждения сброса цвета приложения
 */

class ResetColorWarningSheetDialog(private val prefScreenResetColor: PrefScreenResetColor) :
    BaseSheetDialog(prefScreenResetColor.context, null) {

    init {
        configureDialogParameters()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_reset_color_title)
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
        prefScreenResetColor.callingCallbacks()
    }
}