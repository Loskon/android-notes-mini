package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Очистка корзины
 */

class DialogTrash(context: Context) {

    private val activity: MainActivity = context as MainActivity

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(context)

    private val btnOk: Button = materialDialog.buttonOk

    init {
        materialDialog.setTextTitle(R.string.dg_trash_title)
        materialDialog.setContainerVisibility(false)
    }

    fun show(countNotes: Int) {
        installHandlers(countNotes)
        materialDialog.show()
    }

    private fun installHandlers(countNotes: Int) {
        btnOk.setOnSingleClickListener {

            if (countNotes != 0) {
                activity.deleteAll()
            } else {
                activity.showTrashSnackabar()
            }

            materialDialog.dismiss()
        }
    }
}