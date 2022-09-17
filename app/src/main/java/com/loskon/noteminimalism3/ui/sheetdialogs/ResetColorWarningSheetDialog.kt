package com.loskon.noteminimalism3.ui.sheetdialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.AppearanceSettingsFragment
import com.loskon.noteminimalism3.utils.getShortColor
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener

/**
 * Окно для подтверждения сброса цвета приложения
 */

class ResetColorWarningSheetDialog(private val fragment: AppearanceSettingsFragment) :
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
        btnOk.setDebounceClickListener { onOkBtnClick() }
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