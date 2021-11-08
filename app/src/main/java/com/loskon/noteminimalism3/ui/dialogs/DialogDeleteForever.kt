package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Потверждение удаления выбранных элементов
 */

class DialogDeleteForever(context: Context) {

    private val activity: ListActivity = context as ListActivity
    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)

    private val btnOk: Button = dialog.buttonOk

    init {
        dialog.setTextTitle(R.string.dg_delete_forever_title)
        dialog.setContainerVisibility(false)
    }

    fun show() {
        installHandlers()
        dialog.show()
    }

    private fun installHandlers() {
        btnOk.setOnSingleClickListener {
            activity.deleteItemsForever()
            dialog.dismiss()
        }
    }
}