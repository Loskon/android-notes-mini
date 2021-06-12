package com.loskon.noteminimalism3.ui.dialogs

import android.app.Activity
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

class DialogWarning(private val activity: Activity) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(activity)
    private val view = View.inflate(activity, R.layout.dialog_warning, null)

    private val btnOk: Button = materialDialog.getButtonOk

    init {
        setupViews()
        installHandlers()
    }

    private fun setupViews() {
        materialDialog.setTextTitle(activity.getString(R.string.dg_warning_title))
        materialDialog.setBtnCancelVisibility(false)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            MySharedPref.setBoolean(activity, MyPrefKey.KEY_DIALOG_WARNING_SHOW, false)
            materialDialog.dismiss()
        }
    }

    fun show() {
        materialDialog.show(view)
    }
}