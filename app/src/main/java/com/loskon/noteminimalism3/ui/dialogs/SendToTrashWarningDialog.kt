package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение очистки корзины
 */

class SendToTrashWarningDialog(context: Context) {

    private val activity: MainActivity = context as MainActivity
    private val dialog: BaseDialog = BaseDialog(context)

    init {
        dialog.setTextTitle(R.string.dg_trash_title)
    }

    fun show(itemCount: Int) {
        installHandlersForViews(itemCount)
        dialog.show()
    }

    private fun installHandlersForViews(itemCount: Int) {
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