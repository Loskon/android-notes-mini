package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Потверждение очистки корзины
 */

class DialogTrash(context: Context) {

    private val activity: ListActivity = context as ListActivity
    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)

    private val btnOk: Button = dialog.buttonOk

    init {
        dialog.setTextTitle(R.string.dg_trash_title)
        dialog.setContainerVisibility(false)
    }

    fun show(itemCount: Int) {
        installHandlers(itemCount)
        dialog.show()
    }

    private fun installHandlers(itemCount: Int) {
        btnOk.setOnSingleClickListener {

            if (itemCount != 0) {
                activity.cleanTrash()
            } else {
                activity.showSnackbar(SnackbarManager.MSG_BUT_EMPTY_TRASH, false)
            }

            dialog.dismiss()
        }
    }
}