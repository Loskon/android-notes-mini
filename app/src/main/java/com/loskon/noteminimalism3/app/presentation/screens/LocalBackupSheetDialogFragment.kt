package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.datetime.formatString
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.BaseAppSheetDialogFragment
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.LOCAL_BACKUP_BUNDLE_STRING_ID_KEY
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.LOCAL_BACKUP_BUNDLE_SUCCESS_KEY
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.LOCAL_BACKUP_REQUEST_KEY
import com.loskon.noteminimalism3.backup.DataBaseBackup
import com.loskon.noteminimalism3.databinding.SheetCreateBackupBinding
import com.loskon.noteminimalism3.files.BackupFileHelper
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding
import java.io.File
import java.time.LocalDateTime

class LocalBackupSheetDialogFragment : BaseAppSheetDialogFragment() {

    private val binding by viewBinding(SheetCreateBackupBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addView(binding.root)

        setupDialogViewsParameters()
        establishViewsColor()
        setupViewsParameters()
        setupViewsListeners()
    }

    private fun setupDialogViewsParameters() {
        setTitleDialog(R.string.sheet_backup_title)
    }

    private fun establishViewsColor() {
        binding.inputLayoutBackup.boxStrokeColor = color
    }

    private fun setupViewsParameters() {
        with(binding.inputEditTextBackup) {
            showKeyboard()
            setText(LocalDateTime.now().formatString())
            setSelection(editableText.length)
        }
    }

    private fun setupViewsListeners() {
        binding.inputEditTextBackup.doOnTextChanged { _, _, _, _ -> run { disableErrorNotification() } }
        setBtnOkClickListener { onOkBtnClick() }
    }

    private fun disableErrorNotification() {
        if (binding.inputLayoutBackup.error != null) binding.inputLayoutBackup.isErrorEnabled = false
    }

    private fun onOkBtnClick() {
        val title = binding.inputEditTextBackup.text.toString().trim()

        if (title.isEmpty()) {
            triggerErrorMessage()
        } else {
            createBackupFile(title)
            dismiss()
        }
    }

    private fun triggerErrorMessage() {
        binding.inputEditTextBackup.editableText.clear()
        binding.inputLayoutBackup.error = getString(R.string.sheet_backup_incorrect_name)
        binding.inputLayoutBackup.isErrorEnabled = true
    }

    private fun createBackupFile(title: String) {
        val backupFolder: File = BackupPath.getBackupFolder(requireContext())
        val hasCreatedFolder: Boolean = BackupFileHelper.hasCreated(backupFolder)

        if (hasCreatedFolder) {
            creatingBackup(title)
        } else {
            showSnackbar(R.string.sb_bp_unable_created_folder, false)
        }
    }

    private fun creatingBackup(title: String) {
        val backupPath: String = BackupPath.getPathBackupFolder(requireContext())
        val backupTitle: String = StringUtil.replaceForbiddenCharacters(title)
        val outFileName = "$backupPath$backupTitle.db"

        try {
            DataBaseBackup.performBackup(requireContext(), outFileName)
            showSnackbar(R.string.sb_bp_succes, true)
        } catch (exception: Exception) {
            showSnackbar(R.string.sb_bp_failure, false)
        }
    }

    private fun showSnackbar(stringId: Int, success: Boolean) {
        val bundle = Bundle().apply {
            putInt(LOCAL_BACKUP_BUNDLE_STRING_ID_KEY, stringId)
            putBoolean(LOCAL_BACKUP_BUNDLE_SUCCESS_KEY, success)
        }
        setFragmentResult(LOCAL_BACKUP_REQUEST_KEY, bundle)
    }
}