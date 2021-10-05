package com.loskon.noteminimalism3.ui.dialogs.update

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.update.MainActivityUpdate
import com.loskon.noteminimalism3.ui.dialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Очистка корзины
 */

class DialogTrashUpdate(context: Context) {

    private val activity: MainActivityUpdate = context as MainActivityUpdate
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
                activity.showSnackbar(SnackbarApp.MSG_BUT_EMPTY_TRASH, false)
            }

            dialog.dismiss()
        }
    }
}