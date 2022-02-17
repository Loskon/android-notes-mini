package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для предупреждения о том, что выбранный шрифт
 * может не поддерживать некоторые символы
 */

class WarningAboutFontDialog(dialogContext: Context) :
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
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        PrefHelper.setStatusDialogShow(context)
        dismiss()
    }
}