package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.listview.FilesAdapter
import com.loskon.noteminimalism3.utils.setMargins
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import com.loskon.noteminimalism3.utils.showToast

/**
 * Список файлов резервного копирования
 */

class SheetListFiles(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_list_files, null)

    private val listViewFiles: ListView = view.findViewById(R.id.list_view_files)
    private val txtEmptyRestore: TextView = view.findViewById(R.id.tv_restore_empty)
    private val btnDeleteAll: MaterialButton = view.findViewById(R.id.btn_restore_delete)
    private val btnCancel: MaterialButton = sheetDialog.getButtonCancel

    private var filesAdapter: FilesAdapter = FilesAdapter(context, this)

    init {
        configViews()
        setupViews()
        installHandlers()
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(R.string.sheet_list_files_title)
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setTextBtnCancel(R.string.to_close)
        btnCancel.setMargins(16, 0, 16, 16)
    }

    private fun setupViews() {
        listViewFiles.adapter = filesAdapter
        checkEmptyListFiles()
    }

    private fun installHandlers() {
        btnDeleteAll.setOnSingleClickListener {
            if (filesAdapter.count == 0) {
                context.showToast(R.string.dg_restore_empty)
            } else {
                SheetDeleteAllWarning(context, this).show()
            }
        }
    }

    fun deleteAll() {
        filesAdapter.deleteAll()
    }

    fun checkEmptyListFiles() {
        if (filesAdapter.count == 0) {
            textEmptyVisible(true)
        } else {
            textEmptyVisible(false)
        }
    }

    private fun textEmptyVisible(isVisible: Boolean) {
        txtEmptyRestore.setVisibleView(isVisible)
    }

    fun dismissSheet() {
        callbackRestoreNote?.onRestoreNotes()
        sheetDialog.dismiss()
    }

    fun show() {
        sheetDialog.show()
    }


    // Callback
    interface CallbackRestoreNote {
        fun onRestoreNotes()
    }

    companion object {
        private var callbackRestoreNote: CallbackRestoreNote? = null

        @JvmStatic
        fun listenerCallback(callbackRestoreNote: CallbackRestoreNote) {
            Companion.callbackRestoreNote = callbackRestoreNote
        }
    }
}