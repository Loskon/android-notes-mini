package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.R.style.SheetDialogStatusBar
import com.loskon.noteminimalism3.other.NoteAssistant
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Действия с заметкой
 */

class SheetTextAssistantNote(
    private val context: Context,
    private val assistant: NoteAssistant
) {

    private val dialog: BottomSheetDialog = BottomSheetDialog(context, SheetDialogStatusBar)
    private val insertView = View.inflate(context, R.layout.sheet_note_assistant, null)

    private val tvDateModification: TextView = insertView.findViewById(R.id.tv_sheet_note_title)
    private val btnPaste: MaterialButton = insertView.findViewById(R.id.btn_sheet_paste)
    private val btnCopyAll: MaterialButton = insertView.findViewById(R.id.btn_sheet_copy_all_text)
    private val btnSave: MaterialButton = insertView.findViewById(R.id.btn_sheet_save_txt)
    private val btnShare: MaterialButton = insertView.findViewById(R.id.btn_sheet_share)
    private val btnClose: MaterialButton = insertView.findViewById(R.id.btn_sheet_close)

    init {
        dialog.setContentView(insertView)
    }

    fun show(dateModification: String?, noteId: Long) {
        setTextDateModification(dateModification)
        hideDateModification(noteId)
        installHandlers()
        dialog.show()
    }

    private fun hideDateModification(noteId: Long) {
        if (noteId == 0L) tvDateModification.setVisibleView(false)
    }

    private fun setTextDateModification(dateModification: String?) {
        tvDateModification.text = context.getString(R.string.bs_last_modified, dateModification)
    }

    private fun installHandlers() {
        assistant.apply {
            btnPaste.setOnSingleClickListener {
                dialog.dismiss()
                pasteText()
            }
            btnCopyAll.setOnSingleClickListener {
                dialog.dismiss()
                copyText()
            }
            btnSave.setOnSingleClickListener {
                dialog.dismiss()
                saveTextFile()
            }
            btnShare.setOnSingleClickListener {
                dialog.dismiss()
                shareText()
            }
            btnClose.setOnSingleClickListener { dialog.dismiss() }
        }
    }
}