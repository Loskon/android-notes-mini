package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение очистки корзины
 */

class DialogSendToTrashWarning(context: Context) {

    private val activity: ListActivity = context as ListActivity
    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)

    init {
        dialog.setTextTitle(R.string.dg_trash_title)
    }

    fun show(itemCount: Int) {
        installHandlers(itemCount)
        dialog.show()
    }

    private fun installHandlers(itemCount: Int) {
        dialog.buttonOk.setOnSingleClickListener {

            if (itemCount != 0) {
                activity.cleanTrash()
            } else {
                activity.showSnackbar(SnackbarControl.MSG_BUT_EMPTY_TRASH)
            }

            dialog.dismiss()
        }
    }
}