package com.loskon.noteminimalism3.ui.sheetdialogs

import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.datetime.formattedString
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.backup.DataBaseBackup
import com.loskon.noteminimalism3.files.BackupFileHelper
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.showKeyboard
import java.io.File
import java.time.LocalDateTime

/**
 * Окно для создание бэкапа с определенным названием
 */

class CreateBackupSheetDialog(private val activity: SettingsActivity) :
    BaseSheetDialog(activity, R.layout.sheet_create_backup) {

    private val inputLayout: TextInputLayout = view.findViewById(R.id.input_layout_backup)
    private val inputEditText: TextInputEditText = view.findViewById(R.id.input_edit_text_backup)

    init {
        configureDialogParameters()
        establishViewsColor()
        configInsertedViews()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_backup_title)
    }

    private fun establishViewsColor() {
        inputLayout.boxStrokeColor = color
    }

    private fun configInsertedViews() {
        inputEditText.showKeyboard()
        inputEditText.setText(LocalDateTime.now().formattedString())
        inputEditText.setSelection(inputEditText.editableText.length)
    }

    private fun setupViewsListeners() {
        inputEditText.doOnTextChanged { _, _, _, _ -> run { disableErrorNotification() } }
        btnOk.setDebounceClickListener { onOkBtnClick() }
    }

    private fun disableErrorNotification() {
        if (inputLayout.error != null) inputLayout.isErrorEnabled = false
    }

    private fun onOkBtnClick() {
        val title: String = inputEditText.text.toString().trim()

        if (title.isEmpty()) {
            triggerErrorMessage()
        } else {
            createBackupFile(title)
            dismiss()
        }
    }

    private fun triggerErrorMessage() {
        inputEditText.editableText.clear()
        inputLayout.error = context.getString(R.string.sheet_backup_incorrect_name)
        inputLayout.isErrorEnabled = true
    }

    private fun createBackupFile(title: String) {
        val backupFolder: File = BackupPath.getBackupFolder(context)
        val hasCreatedFolder: Boolean = BackupFileHelper.folderCreated(backupFolder)

        if (hasCreatedFolder) {
            creatingBackup(title)
        } else {
            activity.showSnackbar(WarningSnackbar.MSG_UNABLE_CREATE_FOLDER)
        }
    }

    private fun creatingBackup(title: String) {
        val backupPath: String = BackupPath.getBackupFolderPath(context)
        val backupTitle: String = StringUtil.replaceForbiddenCharacters(title)
        val outFileName = "$backupPath$backupTitle.db"

        try {
            DataBaseBackup.performBackup(context, outFileName)
            activity.showSnackbar(WarningSnackbar.MSG_BACKUP_COMPLETED)
        } catch (exception: Exception) {
            activity.showSnackbar(WarningSnackbar.MSG_BACKUP_FAILED)
        }
    }
}