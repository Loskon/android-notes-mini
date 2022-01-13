package com.loskon.noteminimalism3.ui.materialdialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждение удаления выбранных заметок
 */

class DeleteForeverWarningDialog(context: Context, private val activity: MainActivity) :
    BaseMaterialDialog(context, null) {

    init {
        configureDialogParameters()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.dg_delete_forever_title)
    }

    private fun installHandlersForViews() {
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        activity.deleteItemsForever()
        dismiss()
    }
}