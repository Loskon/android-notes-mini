package com.loskon.noteminimalism3.ui.sheets

import android.app.Activity
import android.view.View
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.other.MyDate
import com.loskon.noteminimalism3.auxiliary.other.ReplaceText
import com.loskon.noteminimalism3.backup.second.AppFolder
import com.loskon.noteminimalism3.backup.second.BackupSetName
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard
import java.util.*

/**
 * Создание резервно копии базы данных
 */

class SheetBackupCreate(private val activity: Activity) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(activity)
    private val view = View.inflate(activity, R.layout.sheet_backup, null)

    private val inputLayout: TextInputLayout = view.findViewById(R.id.input_layout_backup)
    private val inputEditText: TextInputEditText = view.findViewById(R.id.input_edit_text_backup)
    private val btnOk: Button = sheetDialog.getButtonOk

    init {
        sheetDialog.setInsertView(view)
        sheetDialog.setTextTitle(R.string.sheet_backup_title)

        setupColorViews()
        configViews()
        installHandlers()
    }

    private fun setupColorViews() {
        val color: Int = MyColor.getMyColor(activity)
        inputLayout.boxStrokeColor = color
    }

    private fun configViews() {
        inputEditText.showKeyboard(activity)
        inputEditText.setText(MyDate.getNowDate(Date()))
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
        inputLayout.error = activity.getString(R.string.dg_bp_error)
        inputLayout.isErrorEnabled = true
    }

    private fun createBackupFile(text: String) {
        val isFolderCreated = AppFolder.createBackupFolder(activity)

        if (isFolderCreated) {
            val backupName: String = ReplaceText.replaceForSaveTittle(text)
            BackupSetName(activity).callBackup(false, backupName)
        } else {
            MySnackbarBackup.showSnackbar(activity, false, MySnackbarBackup.MSG_TEXT_ERROR)
        }

        sheetDialog.dismiss()
    }

    fun show() {
        sheetDialog.show()
    }
}