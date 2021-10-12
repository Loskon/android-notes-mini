package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.second.BackupFilter
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.backup.update.DateBaseBackup
import com.loskon.noteminimalism3.ui.activities.update.SettingsActivityUpdate
import com.loskon.noteminimalism3.ui.listview.FilesAdapter
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.setMargins
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import com.loskon.noteminimalism3.utils.showToast
import java.io.File

/**
 * Список файлов резервного копирования
 */

class SheetRestoreDateBase(private val context: Context) {

    private val activity: SettingsActivityUpdate = context as SettingsActivityUpdate

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_list_files, null)

    private val listView: ListView = view.findViewById(R.id.list_view_files)
    private val tvEmptyList: TextView = view.findViewById(R.id.tv_restore_empty)
    private val btnDeleteAll: MaterialButton = view.findViewById(R.id.btn_restore_delete)
    private val btnCancel: MaterialButton = sheetDialog.buttonCancel

    private var adapter: FilesAdapter = FilesAdapter(this)

    init {
        configViews()
        configureListView()
        updateFilesList()
        installHandlers()
    }

    private fun configViews() {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(R.string.sheet_list_files_title)
        sheetDialog.setBtnOkVisibility(false)
        sheetDialog.setTextBtnCancel(R.string.to_close)
        btnCancel.setMargins(16, 0, 16, 16)
    }

    private fun configureListView() {
        listView.adapter = adapter
    }

    private fun updateFilesList() {
        adapter.setFilesList(files)
        checkEmptyFilesList()
    }

    private val files: Array<File>?
        get() {
            val folder = BackupPath.getFolderBackup(context)

            return if (folder.exists()) {
                BackupFilter.getListFile(folder)
            } else {
                null
            }
        }

    private fun installHandlers() {
        btnDeleteAll.setOnSingleClickListener {
            if (adapter.count == 0) {
                context.showToast(R.string.dg_restore_empty)
            } else {
                sheetDialog.dismiss()
                SheetDeleteBackupsWarning(context, this).show()
            }
        }
    }

    fun deleteAll() {
        if (files != null) {
            adapter.removeAll(files)
            activity.showSnackbar(SnackbarApp.MSG_BACKUP_FILES_DELETED)
        }
    }

    fun checkEmptyFilesList() {
        if (files == null) {
            tvEmptyList.text = context.getString(R.string.sb_bp_text_no_folder)
        } else {
            tvEmptyList.setVisibleView(adapter.count == 0)
        }
    }

    fun restoreDateBase(inFileName: String) {
        try {
            DateBaseBackup.performRestore(context, inFileName)
            activity.showSnackbar(SnackbarApp.MSG_RESTORE_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(SnackbarApp.MSG_RESTORE_FAILED)
        }

        callback?.onRestoreNotes()
        sheetDialog.dismiss()
    }

    fun show() {
        sheetDialog.show()
    }

    interface CallbackRestoreNote {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: CallbackRestoreNote? = null

        @JvmStatic
        fun listenerCallback(callback: CallbackRestoreNote) {
            this.callback = callback
        }
    }
}