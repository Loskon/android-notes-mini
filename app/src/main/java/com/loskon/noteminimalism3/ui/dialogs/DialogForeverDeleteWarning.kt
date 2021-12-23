package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение удаления выбранных элементов
 */

class DialogForeverDeleteWarning(context: Context) {

    private val activity: MainActivity = context as MainActivity
    private val dialog: BaseMaterialDialogs = BaseMaterialDialogs(context)

    init {
        dialog.setTextTitle(R.string.dg_delete_forever_title)
    }

    fun show() {
        installHandlers()
        dialog.show()
    }

    private fun installHandlers() {
        dialog.buttonOk.setOnSingleClickListener {
            activity.deleteItemsForever()
            dialog.dismiss()
        }
    }
}