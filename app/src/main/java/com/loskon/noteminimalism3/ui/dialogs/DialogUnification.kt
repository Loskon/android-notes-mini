package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение объединения выбранных заметок
 */

class DialogUnification(context: Context) {

    private val activity: ListActivity = context as ListActivity
    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)

    init {
        dialog.setTextTitle(context.getString(R.string.dg_unification_title))
    }

    fun show() {
        installHandlers()
        dialog.show()
    }

    private fun installHandlers() {
        dialog.buttonOk.setOnSingleClickListener {
            activity.performUnificationNotes()
            dialog.dismiss()
        }
    }
}