package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Вывод предупреждения
 */

class DialogWarning(private val context: Context) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val view = View.inflate(context, R.layout.dialog_warning, null)

    private val btnOk: Button = materialDialog.buttonOk

    init {
        setupViews()
        installHandlers()
    }

    private fun setupViews() {
        materialDialog.setTextTitle(context.getString(R.string.dg_warning_title))
        materialDialog.setBtnCancelVisibility(false)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            MySharedPref.setBoolean(context, MyPrefKey.KEY_DIALOG_WARNING_SHOW, false)
            materialDialog.dismiss()
        }
    }

    fun show() {
        materialDialog.show(view)
    }
}