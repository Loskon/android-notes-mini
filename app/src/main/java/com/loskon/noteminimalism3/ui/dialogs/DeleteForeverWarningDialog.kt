package com.loskon.noteminimalism3.ui.dialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждение удаления выбранных заметок
 */

class DeleteForeverWarningDialog(private val activity: MainActivity) :
    BaseMaterialDialog(activity) {

    init {
        configureDialogParameters()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.dg_delete_forever_title)
    }

    private fun setupViewsListeners() {
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        activity.deleteItemsForever()
        dismiss()
    }
}