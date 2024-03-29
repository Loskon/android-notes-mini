package com.loskon.noteminimalism3.ui.sheetdialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.SettingsAppFragment
import com.loskon.noteminimalism3.utils.getShortColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждения сброса цвета приложения
 */

class ResetColorWarningSheetDialog(private val fragment: SettingsAppFragment) :
    BaseSheetDialog(fragment.requireContext()) {

    init {
        configureDialogParameters()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_reset_color_title)
        setContainerVisibility(false)
        setTextBtnOk(R.string.yes)
        setTextBtnCancel(R.string.no)
    }

    private fun setupViewsListeners() {
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        resetColor()
        dismiss()
    }

    private fun resetColor() {
        val appColor: Int = context.getShortColor(R.color.material_blue)
        fragment.callingCallbacks(appColor)
    }
}