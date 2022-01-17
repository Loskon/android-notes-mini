package com.loskon.noteminimalism3.ui.sheetdialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.SettingsAppFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждения сброса цвета приложения
 */

class ResetFontSizeWarningSheetDialog(private val fragment: SettingsAppFragment) :
    BaseSheetDialog(fragment.requireContext()) {

    init {
        configureDialogParameters()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_reset_font_size_title)
        setContainerVisibility(false)
        setTextBtnOk(R.string.yes)
        setTextBtnCancel(R.string.no)
    }

    private fun installHandlersForViews() {
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        fragment.resetFontSize()
        dismiss()
    }
}