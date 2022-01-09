package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение объединения выбранных заметок
 */

class UnificationDialog(context: Context) {

    private val activity: MainActivity = context as MainActivity
    private val dialog: BaseDialog = BaseDialog(context)

    init {
        dialog.setTextTitle(context.getString(R.string.dg_unification_title))
    }

    fun show() {
        installHandlersForViews()
        dialog.show()
    }

    private fun installHandlersForViews() {
        dialog.btnOk.setOnSingleClickListener {
            activity.performUnificationNotes()
            dialog.dismiss()
        }
    }
}