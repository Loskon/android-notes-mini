package com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setVisibleKtx
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.BaseAppSheetDialogFragment
import com.loskon.noteminimalism3.app.base.widget.recyclerview.AddAnimationItemAnimator
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.LOCAL_BACKUP_BUNDLE_STRING_ID_KEY
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.LOCAL_BACKUP_BUNDLE_SUCCESS_KEY
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.LOCAL_BACKUP_REQUEST_KEY
import com.loskon.noteminimalism3.backup.DataBaseBackup
import com.loskon.noteminimalism3.databinding.SheetRestoreListBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BackupFileListSheetDialogFragment : BaseAppSheetDialogFragment() {

    private val binding by viewBinding(SheetRestoreListBinding::inflate)
    private val viewModel: BackupFileListViewModel by viewModel()

    private val filesAdapter = BackupFileListAdapter()

    override val isNestedScrollingEnabled = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.getBackupFileList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addView(binding.root)
        setupDialogViewsParameters()
        establishViewsColor()
        configureRecyclerView()
        setupViewsListeners()
        installObserver()
    }

    private fun setupDialogViewsParameters() {
        setTitleDialog(R.string.sheet_restore_db_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
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
            viewModel.getBackupFileList()
        }
        filesAdapter.setOnItemClickListener { file ->
            try {
                DataBaseBackup.performRestore(requireContext(), file.path)
                showSnackbar(R.string.sb_bp_restore_succes, true)
            } catch (exception: Exception) {
                showSnackbar(R.string.sb_bp_restore_failure, false)
            }
            dismiss()
        }
        filesAdapter.setOnItemDeleteClickListener { file ->
            viewModel.deleteFile(file)
            viewModel.getBackupFileList()
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
            putInt(LOCAL_BACKUP_BUNDLE_STRING_ID_KEY, stringId)
            putBoolean(LOCAL_BACKUP_BUNDLE_SUCCESS_KEY, success)
        }
        setFragmentResult(LOCAL_BACKUP_REQUEST_KEY, bundle)
    }
}