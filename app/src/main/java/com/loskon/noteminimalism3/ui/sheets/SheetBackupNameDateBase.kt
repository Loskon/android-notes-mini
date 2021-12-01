package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.BackupPathManager
import com.loskon.noteminimalism3.backup.DateBaseBackup
import com.loskon.noteminimalism3.files.CheckCreatedFiles
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.DateManager
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard
import java.util.*

/**
 * Создание резервной копии базы данных
 */

class SheetBackupNameDateBase(private val context: Context) {

    private val activity: SettingsActivity = context as SettingsActivity

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)
    private val sheetView = View.inflate(context, R.layout.sheet_backup, null)

    private val inputLayout: TextInputLayout = sheetView.findViewById(R.id.input_layout_backup)
    private val inputEditText: TextInputEditText = sheetView.findViewById(R.id.input_edit_text_backup)

    init {
        dialog.setInsertView(sheetView)
        dialog.setTextTitle(R.string.sheet_backup_title)

        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        val color: Int = PrefManager.getAppColor(context)
        inputLayout.boxStrokeColor = color
    }

    private fun configViews() {
        inputEditText.showKeyboard(context)
        inputEditText.setText(DateManager.getStringDate(Date()))
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
        val hasCreatedFolder = CheckCreatedFiles.createBackupFolder(context)

        if (hasCreatedFolder) {
            creatingBackup(title)
        } else {
            activity.showSnackbar(SnackbarManager.MSG_TEXT_ERROR)
        }

        dialog.dismiss()
    }

    private fun creatingBackup(title: String) {
        val backupTitle: String = StringUtil.replaceForbiddenCharacters(title)
        val backupPath: String = BackupPathManager.getPathBackupFolder(context)
        val outFileName = "$backupPath$backupTitle.db"

        try {
            DateBaseBackup.performBackup(context, outFileName)
            activity.showSnackbar(SnackbarManager.MSG_BACKUP_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(SnackbarManager.MSG_BACKUP_FAILED)
        }
    }

    fun show() {
        dialog.show()
    }
}