package com.loskon.noteminimalism3.ui.dialogs.update

import android.content.Context
import android.view.View
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.ui.dialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.IntentUtil

/**
 * Работа с гиперссылками в заметке
 */

class DialogNoteReceivingData(private val context: Context) :
    View.OnClickListener {

    private val dialog: BaseMaterialDialog = BaseMaterialDialog(context)
    private val view = View.inflate(context, R.layout.dialog_receiving_data, null)

    private val btnOpen: Button = view.findViewById(R.id.btn_add_new_note)
    private val btnCopy: Button = view.findViewById(R.id.btn_update_old_note)

    private var receivingText: String = ""

    init {
        dialog.setTextTitleVisibility(false)
        dialog.setBtnOkVisibility(false)
        dialog.setBtnCancelVisibility(false)
    }

    fun show(receivingText: String) {
        this.receivingText = receivingText
        installHandlers()
        dialog.show(view)
    }

    private fun installHandlers() {
        btnOpen.setOnClickListener(this)
        btnCopy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_new_note -> {
                dialog.dismiss()
                val note = Note2()
                note.title = receivingText
                IntentUtil.openNote(context, note, CATEGORY_ALL_NOTES)
            }

            R.id.btn_update_old_note -> {
                dialog.dismiss()
                IntentUtil.openListNotes(context, receivingText)
            }
        }
    }
}