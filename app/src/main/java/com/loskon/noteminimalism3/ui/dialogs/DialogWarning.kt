package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Вывод предупреждения
 */

class DialogWarning(private val context: Context) {

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val insertView = View.inflate(context, R.layout.dialog_warning, null)

    init {
        dialog.setTextTitle(context.getString(R.string.dg_warning_title))
        dialog.setBtnCancelVisibility(false)
    }

    fun show() {
        installHandlers()
        dialog.show(insertView)
    }

    private fun installHandlers() {
        dialog.buttonOk.setOnSingleClickListener {
            PrefManager.setStatusDialogShow(context)
            dialog.dismiss()
        }
    }
}