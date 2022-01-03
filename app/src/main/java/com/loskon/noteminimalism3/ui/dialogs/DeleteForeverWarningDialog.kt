package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение удаления выбранных элементов
 */

class DeleteForeverWarningDialog(context: Context) {

    private val activity: MainActivity = context as MainActivity
    private val dialog: BaseDialog = BaseDialog(context)

    init {
        dialog.setTextTitle(R.string.dg_delete_forever_title)
    }

    fun show() {
        installHandlersForViews()
        dialog.show()
    }

    private fun installHandlersForViews() {
        dialog.buttonOk.setOnSingleClickListener {
            activity.deleteItemsForever()
            dialog.dismiss()
        }
    }
}