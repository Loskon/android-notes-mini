package com.loskon.noteminimalism3.ui.dialogs

import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

class DialogTrash(private val activity: MainActivity) {

    private val materialDialog: BaseMaterialDialog = BaseMaterialDialog(activity)

    private val btnOk: Button = materialDialog.getButtonOk

    init {
        materialDialog.setTextTitle(activity.getString(R.string.dg_trash_title))
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