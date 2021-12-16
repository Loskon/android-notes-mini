package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.backup.DateBaseBackup
import com.loskon.noteminimalism3.files.CheckCreatedFile
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard
import java.io.File
import java.util.*

/**
 * Создание резервной копии базы данных
 */

class SheetBackupNameDateBase(private val context: Context) {

    private val activity: SettingsActivity = context as SettingsActivity

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val insertView = View.inflate(context, R.layout.sheet_backup, null)

    private val inputLayout: TextInputLayout = insertView.findViewById(R.id.input_layout_backup)
    private val inputEditText: TextInputEditText = insertView.findViewById(R.id.input_edit_text_backup)

    init {
        dialog.setInsertView(insertView)
        dialog.setTextTitle(R.string.sheet_backup_title)
    }

    fun show() {
        establishColorViews()
        configInsertedViews()
        installHandlers()
        dialog.show()
    }

    private fun establishColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        inputLayout.boxStrokeColor = color
    }

    private fun configInsertedViews() {
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

        dialog.buttonOk.setOnSingleClickListener {
            clickingButtonOk()
        }
    }

    private fun errorManagement() {
        if (inputLayout.error != null) {
            inputLayout.isErrorEnabled = false
        }
    }

    private fun clickingButtonOk() {
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
        inputLayout.error = context.getString(R.string.sheet_backup_incorrect_name)
        inputLayout.isErrorEnabled = true
    }

    private fun createBackupFile(title: String) {
        val backupFolder: File = BackupPath.getBackupFolder(context)
        val hasCreatedFolder = CheckCreatedFile.hasCreated(backupFolder)

        if (hasCreatedFolder) {
            creatingBackup(title)
        } else {
            activity.showSnackbar(SnackbarControl.MSG_UNABLE_CREATE_FOLDER)
        }

        dialog.dismiss()
    }

    private fun creatingBackup(title: String) {
        val backupPath: String = BackupPath.getPathBackupFolder(context)
        val backupTitle: String = StringUtil.replaceForbiddenCharacters(title)
        val outFileName = "$backupPath$backupTitle.db"

        try {
            DateBaseBackup.performBackup(context, outFileName)
            activity.showSnackbar(SnackbarControl.MSG_BACKUP_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(SnackbarControl.MSG_BACKUP_FAILED)
        }
    }
}