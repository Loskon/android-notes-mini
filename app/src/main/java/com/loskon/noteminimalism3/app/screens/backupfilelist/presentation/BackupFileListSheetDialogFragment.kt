package com.loskon.noteminimalism3.app.screens.backupfilelist.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupNewFragment.Companion.CREATE_BACKUP_BUNDLE_STRING_ID_KEY
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupNewFragment.Companion.CREATE_BACKUP_BUNDLE_SUCCESS_KEY
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupNewFragment.Companion.CREATE_BACKUP_REQUEST_KEY
import com.loskon.noteminimalism3.base.extension.flow.observe
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setVisibleKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.base.widget.recyclerview.AddAnimationItemAnimator
import com.loskon.noteminimalism3.databinding.SheetRestoreListBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BackupFileListSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetRestoreListBinding::inflate)
    private val viewModel: BackupFileListViewModel by viewModel()

    private val filesAdapter = BackupFileListAdapter()

    override val isNestedScrollingEnabled = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getBackupFileList()
    }

    private fun getBackupFileList() {
        val folderPath = AppPreference.getBackupPath(requireContext())
        viewModel.getBackupFileList(folderPath)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContentView(binding.root)
        setupDialogViewsParameters()
        establishViewsColor()
        configureRecyclerView()
        setupViewsListeners()
        installObserver()
    }

    private fun setupDialogViewsParameters() {
        setDialogTitle(R.string.sheet_restore_db_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        val color = getAppColor()
        binding.btnCancelDelete.setTextColor(color)
        binding.btnConfirmDelete.setBackgroundColor(color)
    }

    private fun configureRecyclerView() {
        with(binding.rvRestoreDelete) {
            itemAnimator = AddAnimationItemAnimator()
            layoutManager = LinearLayoutManager(requireContext())
            adapter = filesAdapter
        }
    }

    private fun setupViewsListeners() {
        binding.btnRestoreDelete.setDebounceClickListener {
            displayConfirmDeleteBtns(true)
        }
        binding.btnCancelDelete.setDebounceClickListener {
            displayConfirmDeleteBtns(false)
        }
        binding.btnConfirmDelete.setDebounceClickListener {
            displayConfirmDeleteBtns(false)
            deleteAllFiles()
            getBackupFileList()
        }
        filesAdapter.setOnItemClickListener { file ->
            val databasePath = requireContext().getDatabasePath(NoteDatabaseSchema.DATABASE_NAME).toString()
            val restoreSuccess = viewModel.performRestore(file.path, databasePath)

            if (restoreSuccess) {
                showSnackbar(R.string.sb_bp_restore_succes, success = true)
            } else {
                showSnackbar(R.string.sb_bp_restore_failure, success = false)
            }

            dismiss()
        }
        filesAdapter.setOnItemDeleteClickListener { file ->
            viewModel.deleteFile(file)
            getBackupFileList()
        }
    }

    private fun displayConfirmDeleteBtns(display: Boolean) {
        with(binding) {
            btnRestoreDelete.setVisibleKtx(display.not())
            btnCancelDelete.setVisibleKtx(display)
            btnConfirmDelete.setVisibleKtx(display)
        }
    }

    private fun deleteAllFiles() {
        val files = viewModel.getBackupFileList.value

        if (files != null) {
            for (file in files) viewModel.deleteFile(file)
        }
    }

    private fun installObserver() {
        viewModel.getBackupFileList.observe(viewLifecycleOwner) { files ->
            binding.tvRestoreEmpty.isVisible = (files?.isEmpty() == true)
            files?.let { filesAdapter.updateFileList(files) }
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