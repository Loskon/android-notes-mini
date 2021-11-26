package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.R.style.BottomSheetStatusBar
import com.loskon.noteminimalism3.other.TextNoteAssistant
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Действия с заметкой
 */

class SheetTextAssistantNote(
    private val context: Context,
    private val textAssistant: TextNoteAssistant
) {

    private val dialog: BottomSheetDialog = BottomSheetDialog(context, BottomSheetStatusBar)
    private val sheetView = View.inflate(context, R.layout.sheet_custom_note, null)

    private val tvDateModification: TextView = sheetView.findViewById(R.id.tv_sheet_note_title)
    private val btnPaste: MaterialButton = sheetView.findViewById(R.id.btn_sheet_paste)
    private val btnCopyAll: MaterialButton = sheetView.findViewById(R.id.btn_sheet_copy_all_text)
    private val btnSave: MaterialButton = sheetView.findViewById(R.id.btn_sheet_save_txt)
    private val btnShare: MaterialButton = sheetView.findViewById(R.id.btn_sheet_share)
    private val btnClose: MaterialButton = sheetView.findViewById(R.id.btn_sheet_close)

    init {
        installHandlers()
    }

    private fun installHandlers() {
        textAssistant.apply {
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

    fun show(dateModification: String?, noteId: Long?) {
        setTextDateModification(dateModification)
        hideDateModification(noteId)
        dialog.setContentView(sheetView)
        dialog.show()
    }

    private fun hideDateModification(noteId: Long?) {
        if (noteId == 0L) tvDateModification.setVisibleView(false)
    }

    private fun setTextDateModification(dateModification: String?) {
        tvDateModification.text = context.getString(R.string.bs_last_modified, dateModification)
    }
}