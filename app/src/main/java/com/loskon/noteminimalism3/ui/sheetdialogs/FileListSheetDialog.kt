package com.loskon.noteminimalism3.ui.sheetdialogs

import android.widget.ListView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DataBaseBackup
import com.loskon.noteminimalism3.files.BackupFiles
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.listview.FileBaseAdapter
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.ui.toast.WarningToast
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import java.io.File

/**
 * Окно со список файлов бэкапа
 */

class FileListSheetDialog(private val activity: SettingsActivity) :
    BaseSheetDialog(activity, R.layout.sheet_file_list),
    FileBaseAdapter.FilesAdapterCallback {

    private val listView: ListView = view.findViewById(R.id.list_view_files)
    private val tvEmpty: TextView = view.findViewById(R.id.tv_restore_empty)
    private val btnDeleteAll: MaterialButton = view.findViewById(R.id.btn_restore_delete)

    private var adapter: FileBaseAdapter = FileBaseAdapter()

    init {
        configureDialogParameters()
        configureListAdapter()
        configureListView()
        updateFilesList()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_restore_db_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun configureListAdapter() {
        adapter.registerCallbackFilesAdapter(this)
    }

    private fun configureListView() {
        listView.adapter = adapter
    }

    private fun updateFilesList() {
        adapter.setFilesList(files)
        checkEmptyFilesList()
    }

    private fun checkEmptyFilesList() {
        if (files == null) {
            tvEmpty.text = context.getString(R.string.sheet_restore_db_folder_not_found)
        } else {
            tvEmpty.setVisibleView(adapter.count == 0)
        }
    }

    private val files: Array<File>?
        get() {
            return BackupFiles.getList(context)
        }

    private fun installHandlersForViews() {
        btnDeleteAll.setOnSingleClickListener { onDeleteAllBtnClick() }
    }

    private fun onDeleteAllBtnClick() {
        if (adapter.count == 0) {
            WarningToast.show(context, WarningToast.MSG_TOAST_RESTORE_LIST_EMPTY)
        } else {
            dismiss()
            DeleteFilesSheetDialog(activity).show()
        }
    }

    //--- FilesAdapter -----------------------------------------------------------------------------
    override fun onFileClick(file: File) {
        try {
            DataBaseBackup.performRestore(context, file.path)
            activity.showSnackbar(WarningSnackbar.MSG_RESTORE_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(WarningSnackbar.MSG_RESTORE_FAILED)
        }

        callback?.onRestoreNotes()
        dismiss()
    }

    override fun onCheckEmpty() = checkEmptyFilesList()

    //--- interface --------------------------------------------------------------------------------
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