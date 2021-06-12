package com.loskon.noteminimalism3.ui.dialogs

import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

class DialogUnification(private val activity: MainActivity) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(activity)

    private val btnOk: Button = materialDialog.getButtonOk

    init {
        setupViews()
        installHandlers()
    }

    private fun setupViews() {
        materialDialog.setTextTitle(activity.getString(R.string.dg_unification_title))
        materialDialog.setContainerVisibility(false)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            activity.unification()
            materialDialog.dismiss()
        }
    }

    fun show() {
        materialDialog.show()
    }
}