package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Потверждение объединения выбранных заметок
 */

class DialogUnification(private val context: Context) {

    private val activity: ListActivity = context as ListActivity

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)

    private val btnOk: Button = dialog.buttonOk

    init {
        setupViews()
        installHandlers()
    }

    private fun setupViews() {
        dialog.setTextTitle(context.getString(R.string.dg_unification_title))
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            activity.performUnificationNotes()
            dialog.dismiss()
        }
    }

    fun show() {
        dialog.show()
    }
}