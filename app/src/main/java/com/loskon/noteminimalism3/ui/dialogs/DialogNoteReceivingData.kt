package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.ReceivingDataActivity

/**
 * Работа с гиперссылками в заметке
 */

class DialogNoteReceivingData(context: Context) : View.OnClickListener {

    private val activity: ReceivingDataActivity = context as ReceivingDataActivity

    private val dialog: BaseMaterialDialogs = BaseMaterialDialogs(context)
    private val insertView = View.inflate(context, R.layout.dialog_receiving_data, null)

    private val btnOpen: Button = insertView.findViewById(R.id.btn_add_in_new_note)
    private val btnCopy: Button = insertView.findViewById(R.id.btn_update_old_note)

    init {
        dialog.setTextTitleVisibility(false)
        dialog.setBtnOkVisibility(false)
        dialog.setBtnCancelVisibility(false)
    }

    fun show() {
        installHandlers()
        dialog.show(insertView)
    }

    private fun installHandlers() {
        btnOpen.setOnClickListener(this)
        btnCopy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_in_new_note -> {
                dialog.dismiss()
                activity.addNewNote()
            }

            R.id.btn_update_old_note -> {
                dialog.dismiss()
            }
        }
    }
}