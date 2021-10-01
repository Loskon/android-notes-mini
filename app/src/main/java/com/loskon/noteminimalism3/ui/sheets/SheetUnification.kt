package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.dialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Объединение заметок
 */

class SheetUnification(private val context: Context) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(context)

    private val btnOk: Button = materialDialog.buttonOk

    init {
        setupViews()
        installHandlers()
    }

    private fun setupViews() {
        materialDialog.setTextTitle(R.string.dg_unification_title)
        materialDialog.setContainerVisibility(false)
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            (context as MainActivity).unification()
            materialDialog.dismiss()
        }
    }

    fun show() {
        materialDialog.show()
    }
}