package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.R.style.BottomSheetStatusBar
import com.loskon.noteminimalism3.auxiliary.note.TextNoteAssistant
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Действия с заметкой
 */

class SheetCustomNoteKt(
    private val context: Context,
    private val textAssistant: TextNoteAssistant
) :
    View.OnClickListener {

    private val sheetDialog: BottomSheetDialog = BottomSheetDialog(context, BottomSheetStatusBar)
    private val view = View.inflate(context, R.layout.sheet_custom_note, null)

    private val tvDateModification: TextView = view.findViewById(R.id.tv_sheet_note_title)
    private val tvPaste: MaterialButton = view.findViewById(R.id.btn_sheet_paste)
    private val tvCopyAll: MaterialButton = view.findViewById(R.id.btn_sheet_copy_all_text)
    private val tvSave: MaterialButton = view.findViewById(R.id.btn_sheet_save_txt)
    private val tvShare: MaterialButton = view.findViewById(R.id.btn_sheet_share)
    private val tvClose: MaterialButton = view.findViewById(R.id.btn_sheet_close)

    init {
        installHandlers()
    }

    private fun installHandlers() {
        tvPaste.setOnClickListener(this)
        tvCopyAll.setOnClickListener(this)
        tvSave.setOnClickListener(this)
        tvShare.setOnClickListener(this)
        tvClose.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        sheetDialog.dismiss()

        when (v?.id) {
            R.id.btn_sheet_paste -> {
                textAssistant.pasteText()
            }
            R.id.btn_sheet_copy_all_text -> {
                textAssistant.copyText()
            }
            R.id.btn_sheet_save_txt -> {
                textAssistant.saveTextFile()
            }
            R.id.btn_sheet_share -> {
                textAssistant.sendText()
            }
        }
    }

    fun show(dateModification: String?, noteId: Long?) {
        setTextDateModification(dateModification)
        hideDateModification(noteId)
        sheetDialog.setContentView(view)
        sheetDialog.show()
    }

    private fun hideDateModification(noteId: Long?) {
        if (noteId == 0L) tvDateModification.setVisibleView(false)
    }

    private fun setTextDateModification(dateModification: String?) {
        var text = context.getString(R.string.bs_last_modified)
        text = "$text: $dateModification"
        tvDateModification.text = text
    }
}