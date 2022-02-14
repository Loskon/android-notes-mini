package com.loskon.noteminimalism3.ui.sheetdialogs

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

class NoteAssistantSheetDialog(
    sheetContext: Context,
    private val assistant: NoteAssistant
) : BottomSheetDialog(sheetContext, SheetDialogStatusBar) {

    private val insertView = View.inflate(context, R.layout.sheet_assistant_note, null)

    private val tvDateModification: TextView = insertView.findViewById(R.id.tv_sheet_note_title)
    private val btnPaste: MaterialButton = insertView.findViewById(R.id.btn_sheet_paste)
    private val btnCopyAll: MaterialButton = insertView.findViewById(R.id.btn_sheet_copy_all_text)
    private val btnSave: MaterialButton = insertView.findViewById(R.id.btn_sheet_save_txt)
    private val btnShare: MaterialButton = insertView.findViewById(R.id.btn_sheet_share)
    private val btnClose: MaterialButton = insertView.findViewById(R.id.btn_sheet_close)

    init {
        setContentView(insertView)
    }

    fun show(dateModification: String?, noteId: Long) {
        configInsertedViews(dateModification, noteId)
        setupViewsListeners()
        super.show()
    }

    private fun configInsertedViews(dateModification: String?, noteId: Long) {
        setTextDateModification(dateModification)
        hideDateModification(noteId)
    }

    private fun hideDateModification(noteId: Long) {
        if (noteId == 0L) tvDateModification.setVisibleView(false)
    }

    private fun setTextDateModification(dateModification: String?) {
        tvDateModification.text = context.getString(R.string.bs_last_modified, dateModification)
    }

    private fun setupViewsListeners() {
        assistant.apply {
            btnPaste.setOnSingleClickListener {
                dismiss()
                pasteText()
            }
            btnCopyAll.setOnSingleClickListener {
                dismiss()
                copyText()
            }
            btnSave.setOnSingleClickListener {
                dismiss()
                saveTextFile()
            }
            btnShare.setOnSingleClickListener {
                dismiss()
                shareText()
            }
            btnClose.setOnSingleClickListener { dismiss() }
        }
    }
}