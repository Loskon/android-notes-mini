package com.loskon.noteminimalism3.ui.dialogs

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.second.FilesAdapter
import com.loskon.noteminimalism3.ui.sheets.BaseSheetDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 *
 */

class SheetListFiles(private val context: Context) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_list_files, null)

    private val listViewFiles: ListView = view.findViewById(R.id.list_view_files)
    private val txtEmptyRestore: TextView = view.findViewById(R.id.tv_restore_empty)
    private val btnRemoveAll: MaterialButton = view.findViewById(R.id.btn_restore_delete)

    private var filesAdapter: FilesAdapter = FilesAdapter(context, this)

    init {
        configViews()
        setupViews()
        installHandlers()
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(context.getString(R.string.dg_restore_title))
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setTextBtnCancel(context.getString(R.string.bs_to_close))
    }

    private fun setupViews() {
        listViewFiles.adapter = filesAdapter
        checkEmptyListFiles()
    }

    private fun installHandlers() {
        btnRemoveAll.setOnSingleClickListener {
            filesAdapter.deleteAll()
        }
    }

    fun checkEmptyListFiles() {
        if (filesAdapter.count == 0) {
            visible(true)
        } else {
            visible(false)
        }
    }

    private fun visible(isVisible: Boolean) {
        txtEmptyRestore.setVisibleView(isVisible)
    }

    fun asd() {
        callbackRestoreNote?.onCallBack()
        sheetDialog.dismiss()
    }

    fun show() {
        sheetDialog.show()
    }


    // Callback
    interface CallbackRestoreNote {
        fun onCallBack()
    }

    companion object {
        private var callbackRestoreNote: CallbackRestoreNote? = null

        @JvmStatic
        fun regCallbackRestoreNote(callbackRestoreNote: CallbackRestoreNote) {
            SheetListFiles.callbackRestoreNote = callbackRestoreNote
        }
    }
}