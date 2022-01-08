package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DataBaseBackup
import com.loskon.noteminimalism3.files.BackupFiles
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.listview.FilesAdapter
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.ui.toast.WarningToast
import com.loskon.noteminimalism3.utils.setMargins
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import java.io.File

/**
 * Список файлов резервного копирования
 */

class ListRestoreSheetDialog(private val context: Context) : FilesAdapter.FilesAdapterCallback {

    private val activity: SettingsActivity = context as SettingsActivity

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_list_files, null)

    private val listView: ListView = insertView.findViewById(R.id.list_view_files)
    private val tvEmpty: TextView = insertView.findViewById(R.id.tv_restore_empty)
    private val btnDeleteAll: MaterialButton = insertView.findViewById(R.id.btn_restore_delete)

    private var adapter: FilesAdapter = FilesAdapter()

    init {
        dialog.addInsertedView(insertView)
        dialog.setTextTitle(R.string.sheet_restore_db_title)
        dialog.setBtnOkVisibility(false)
        dialog.setTextBtnCancel(R.string.to_close)
        dialog.buttonCancel.setMargins(16, 0, 16, 16)
    }

    fun show() {
        installCallbacks()
        configureListView()
        updateFilesList()
        installHandlersForViews()
        dialog.show()
    }

    private fun installCallbacks() {
        FilesAdapter.registerCallbackFilesAdapter(this)
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
            return BackupFiles.getList(context)
        }

    private fun installHandlersForViews() {
        btnDeleteAll.setOnSingleClickListener {
            if (adapter.count == 0) {
                WarningToast.show(context, WarningToast.MSG_TOAST_RESTORE_LIST_EMPTY)
            } else {
                dialog.dismiss()
                DeleteBackupsFilesSheetDialog(context).show()
            }
        }
    }

    private fun checkEmptyFilesList() {
        if (files == null) {
            tvEmpty.text = context.getString(R.string.sheet_restore_db_folder_not_found)
        } else {
            tvEmpty.setVisibleView(adapter.count == 0)
        }
    }

    override fun onClickingFile(file: File) {
        try {
            DataBaseBackup.performRestore(context, file.path)
            activity.showSnackbar(WarningSnackbar.MSG_RESTORE_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(WarningSnackbar.MSG_RESTORE_FAILED)
        }

        callback?.onRestoreNotes()
        dialog.dismiss()
    }

    override fun onCheckEmpty() = checkEmptyFilesList()

    interface RestoreNoteCallback {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: RestoreNoteCallback? = null

        fun registerCallbackRestoreNote(callback: RestoreNoteCallback) {
            this.callback = callback
        }
    }
}