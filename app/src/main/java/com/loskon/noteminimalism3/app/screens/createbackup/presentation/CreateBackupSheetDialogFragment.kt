package com.loskon.noteminimalism3.app.screens.createbackup.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupNewFragment.Companion.CREATE_BACKUP_BUNDLE_STRING_ID_KEY
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupNewFragment.Companion.CREATE_BACKUP_BUNDLE_SUCCESS_KEY
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupNewFragment.Companion.CREATE_BACKUP_REQUEST_KEY
import com.loskon.noteminimalism3.base.datetime.formattedString
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetCreateBackupBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class CreateBackupSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetCreateBackupBinding::inflate)
    private val viewModel: CreateBackupViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setupDialogViewsParameters()
        establishViewsColor()
        setupViewsParameters()
        setupViewsListeners()
    }

    private fun setupDialogViewsParameters() {
        setDialogTitle(R.string.sheet_backup_title)
    }

    private fun establishViewsColor() {
        binding.inputLayoutBackup.boxStrokeColor = getAppColor()
    }

    private fun setupViewsParameters() {
        with(binding.inputEditTextBackup) {
            showKeyboard()
            setText(LocalDateTime.now().formattedString())
            setSelection(editableText.length)
        }
    }

    private fun setupViewsListeners() {
        binding.inputEditTextBackup.doOnTextChanged { _, _, _, _ -> run { disableErrorNotification() } }
        setOkClickListener { handleBtnOkClick() }
    }

    private fun disableErrorNotification() {
        if (binding.inputLayoutBackup.error != null) binding.inputLayoutBackup.isErrorEnabled = false
    }

    private fun handleBtnOkClick() {
        val title = binding.inputEditTextBackup.text.toString().trim()

        if (title.isEmpty()) {
            triggerErrorMessage()
        } else {
            createBackupFile(title)
        }
    }

    private fun triggerErrorMessage() {
        binding.inputEditTextBackup.editableText.clear()
        binding.inputLayoutBackup.error = getString(R.string.sheet_backup_incorrect_name)
        binding.inputLayoutBackup.isErrorEnabled = true
    }

    private fun createBackupFile(title: String) {
        val backupPath = AppPreference.getBackupPath(requireContext())
        val folderCreated = viewModel.backupFolderCreated(backupPath)

        if (folderCreated) {
            creatingBackup(backupPath, title)
        } else {
            showSnackbar(R.string.sb_bp_failure, success = false)
        }
    }

    private fun creatingBackup(backupPath: String, title: String) {
        val backupTitle = StringUtil.replaceForbiddenCharacters(title)
        val backupFilePath = "$backupPath$backupTitle.db"

        val databasePath = requireContext().getDatabasePath(NoteDatabaseSchema.DATABASE_NAME).toString()
        val backupSuccess = viewModel.performBackup(databasePath, backupFilePath)

        if (backupSuccess) {
            val maxFilesCount = AppPreference.getBackupsCount(requireContext())
            viewModel.deleteExtraFiles(backupPath, maxFilesCount)
            showSnackbar(R.string.sb_bp_succes, success = true)
        } else {
            showSnackbar(R.string.sb_bp_failure, success = false)
        }
    }

    private fun showSnackbar(stringId: Int, success: Boolean) {
        val bundle = Bundle().apply {
            putInt(CREATE_BACKUP_BUNDLE_STRING_ID_KEY, stringId)
            putBoolean(CREATE_BACKUP_BUNDLE_SUCCESS_KEY, success)
        }

        setFragmentResult(CREATE_BACKUP_REQUEST_KEY, bundle)
    }
}