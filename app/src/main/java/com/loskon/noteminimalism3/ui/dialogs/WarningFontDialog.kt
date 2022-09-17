package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener

/**
 * Окно для предупреждения о том, что выбранный шрифт
 * может не поддерживать некоторые символы
 */

class WarningFontDialog(dialogContext: Context) :
    BaseMaterialDialog(dialogContext, R.layout.dialog_warning_about_font) {

    init {
        configureDialogParameters()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.dg_warning_title)
        setBtnCancelVisibility(false)
    }

    private fun setupViewsListeners() {
        btnOk.setDebounceClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        AppPreference.setStatusDialogShow(context)
        dismiss()
    }
}