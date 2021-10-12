package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Вывод предупреждения
 */

class DialogWarning(private val context: Context) {

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val view = View.inflate(context, R.layout.dialog_warning, null)

    private val btnOk: Button = dialog.buttonOk

    init {
        setupViews()
        installHandlers()
    }

    private fun setupViews() {
        dialog.setTextTitle(context.getString(R.string.dg_warning_title))
        dialog.setBtnCancelVisibility(false)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            AppPref.setStatusDialogShow(context)
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show(view)
    }
}