package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.BackupFilter
import com.loskon.noteminimalism3.backup.BackupPathManager
import com.loskon.noteminimalism3.backup.DateBaseBackup
import com.loskon.noteminimalism3.toast.ToastManager
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.listview.FilesAdapter
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.setMargins
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import java.io.File


/**
 * Список файлов резервного копирования
 */

class SheetListRestoreDateBase(private val context: Context) {

    private val activity: SettingsActivity = context as SettingsActivity

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_list_files, null)

    private val listView: ListView = sheetView.findViewById(R.id.list_view_files)
    private val tvEmptyList: TextView = sheetView.findViewById(R.id.tv_restore_empty)
    private val btnDeleteAll: MaterialButton = sheetView.findViewById(R.id.btn_restore_delete)
    private val btnCancel: MaterialButton = dialog.buttonCancel

    private var adapter: FilesAdapter = FilesAdapter(this)

    init {
        configViews()
        configureListView()
        updateFilesList()
        installHandlers()
    }

    private fun configViews() {
        dialog.setInsertView(sheetView)
        dialog.setTextTitle(R.string.sheet_restore_db_title)
        dialog.setBtnOkVisibility(false)
        dialog.setTextBtnCancel(R.string.to_close)
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
            val folder = BackupPathManager.getBackupFolder(context)

            return if (folder.exists()) {
                BackupFilter.getListDateBaseFile(folder)
            } else {
                null
            }
        }

    private fun installHandlers() {
        btnDeleteAll.setOnSingleClickListener {
            if (adapter.count == 0) {
                ToastManager.show(context, ToastManager.MSG_TOAST_RESTORE_LIST_EMPTY)
            } else {
                dialog.dismiss()
                SheetDeleteBackupsFiles(context, this).show()
            }
        }
    }

    fun deleteAll() {
        if (files != null) {
            adapter.removeAll(files)
            activity.showSnackbar(SnackbarManager.MSG_BACKUP_FILES_DELETED)
        }
    }

    fun checkEmptyFilesList() {
        if (files == null) {
            tvEmptyList.text = context.getString(R.string.sheet_restore_db_folder_not_found)
        } else {
            tvEmptyList.setVisibleView(adapter.count == 0)
        }
    }

    fun restoreDateBase(inFileName: String) {
        try {
            DateBaseBackup.performRestore(context, inFileName)
            activity.showSnackbar(SnackbarManager.MSG_RESTORE_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(SnackbarManager.MSG_RESTORE_FAILED)
        }

        callback?.onRestoreNotes()
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }

    interface CallbackRestoreNote {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: CallbackRestoreNote? = null

        fun listenerCallback(callback: CallbackRestoreNote) {
            this.callback = callback
        }
    }
}