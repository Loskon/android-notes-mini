package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.backup.update.DateBaseBackup
import com.loskon.noteminimalism3.backup.update.FolderUtil
import com.loskon.noteminimalism3.ui.activities.update.SettingsActivityUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard
import java.io.File
import java.util.*

/**
 * Создание резервной копии базы данных
 */

class SheetBackupDateBase(private val context: Context) {

    private val activity: SettingsActivityUpdate = context as SettingsActivityUpdate

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)
    private val view = View.inflate(context, R.layout.sheet_backup, null)

    private val inputLayout: TextInputLayout = view.findViewById(R.id.input_layout_backup)
    private val inputEditText: TextInputEditText = view.findViewById(R.id.input_edit_text_backup)
    private val btnOk: Button = sheetDialog.buttonOk

    init {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(R.string.sheet_backup_title)

        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        val color: Int = AppPref.getAppColor(context)
        inputLayout.boxStrokeColor = color
    }

    private fun configViews() {
        inputEditText.showKeyboard(context)
        inputEditText.setText(DateUtil.getStringDate(Date()))
        inputEditText.setSelection(inputEditText.editableText.length)
    }

    private fun installHandlers() {
        inputEditText.doOnTextChanged { _, _, _, _ ->
            run {
                errorManagement()
            }
        }

        btnOk.setOnSingleClickListener {
            onClickOk()
        }
    }

    private fun errorManagement() {
        if (inputLayout.error != null) {
            inputLayout.isErrorEnabled = false
        }
    }

    private fun onClickOk() {
        val text: String = inputEditText.text.toString()
        val textTrim: String = text.trim()

        if (textTrim.isEmpty()) {
            outErrorMessage()
        } else {
            createBackupFile(text)
        }
    }

    private fun outErrorMessage() {
        inputEditText.editableText.clear()
        inputLayout.error = context.getString(R.string.dg_bp_error)
        inputLayout.isErrorEnabled = true
    }

    private fun createBackupFile(title: String) {
        val hasCreatedFolder = FolderUtil.createBackupFolder(context)

        if (hasCreatedFolder) {
            creatingBackup(title)
        } else {
            activity.showSnackbar(SnackbarApp.MSG_TEXT_ERROR)
        }

        sheetDialog.dismiss()
    }

    private fun creatingBackup(title: String) {
        val backupTitle: String = StringUtil.replaceForbiddenCharacters(title)
        val backupPath: String = BackupPath.getPath(context) + File.separator
        val outFileName = "$backupPath$backupTitle.db"

        try {
            DateBaseBackup.performBackup(context, outFileName)
            activity.showSnackbar(SnackbarApp.MSG_BACKUP_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(SnackbarApp.MSG_BACKUP_FAILED)
        }
    }

    fun show() {
        sheetDialog.show()
    }
}