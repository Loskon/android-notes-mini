package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.contracts.AuthContract
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.setFragmentClickListener
import com.loskon.noteminimalism3.app.base.extension.fragment.setFragmentResultListener
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setAllMenuItemsVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.widget.snackbar.CustomizedSnackbar
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupAction
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupMessageType
import com.loskon.noteminimalism3.databinding.FragmentBackupNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BackupNewFragment : Fragment(R.layout.fragment_backup_new) {

    private val binding by viewBinding(FragmentBackupNewBinding::bind)
    private val viewModel: BackupViewModel by viewModel()

    private val authContract = AuthContract(this) { viewModel.signInToGoogle(it) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.hasAuthorizedUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CLOUD_BACKUP_REQUEST_KEY) { bundle ->
            val isBackupResult = bundle.getBoolean(CLOUD_BACKUP_BUNDLE_KEY)

            if (isBackupResult) {
                checkingUserBeforeBackup()
            } else {
                checkingUserBeforeRestore()
            }
        }
        setFragmentClickListener(SIGN_OUT_REQUEST_KEY) {
            viewModel.signOut()
        }
        setFragmentClickListener(DELETE_ACCOUNT_REQUEST_KEY) {
            viewModel.getIntentSenderForAuthContract(requireActivity(), AuthIntent.REAUTHENTICATE)
        }
    }

    private fun checkingUserBeforeBackup() {
        if (viewModel.getBackupState.value.hasAuthorizedUser) {
            viewModel.backupDatebaseFile()
        } else {
            viewModel.getIntentSenderForAuthContract(requireActivity(), AuthIntent.BACKUP)
        }
    }

    private fun checkingUserBeforeRestore() {
        if (viewModel.getBackupState.value.hasAuthorizedUser) {
            viewModel.restoreDatabaseFile()
        } else {
            viewModel.getIntentSenderForAuthContract(requireActivity(), AuthIntent.RESTORE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        setupViewsListeners()
        installObservers()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())

        with(binding) {
            btnBackupLocal.setBackgroundColor(color)
            btnRestoreLocal.setBackgroundColor(color)
            btnBackupCloud.setBackgroundColor(color)
            btnRestoreCloud.setBackgroundColor(color)
            bottomBarBackup.setAllItemsColor(color)
        }
    }

    private fun setupViewsListeners() {
        with(binding) {
            btnBackupLocal.setDebounceClickListener { }
            btnRestoreLocal.setDebounceClickListener { }
            btnBackupCloud.setDebounceClickListener { onBackupCloudBtnClick() }
            btnRestoreCloud.setDebounceClickListener { onRestoreCloudBtnClick() }
            bottomBarBackup.setDebounceMenuItemClickListener { onMenuItemClick(it) }
            bottomBarBackup.setDebounceNavigationClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun onBackupCloudBtnClick() {
        viewModel.checkInternetBeforeShowConfirmDialog(isBackup = true)
    }

    private fun onRestoreCloudBtnClick() {
        viewModel.checkInternetBeforeShowConfirmDialog(isBackup = false)
    }

    private fun onMenuItemClick(item: MenuItem) {
        if (item.itemId == R.id.item_account) {
            viewModel.checkInternetBeforeShowAccountDialog()
        }
    }

    private fun installObservers() {
        viewModel.getBackupState.observe(viewLifecycleOwner) { backupState ->
            binding.bottomBarBackup.setAllMenuItemsVisibility(backupState.hasAuthorizedUser)
        }
        viewModel.getBackupAction.observe(viewLifecycleOwner) { backupAction ->
            when (backupAction) {
                is BackupAction.LaunchAuthContract -> authContract.launch(backupAction.intentSender)
                is BackupAction.ShowSnackbar -> selectMessageToShowSnackbar(backupAction.message)
                is BackupAction.ShowConfirmDialog -> showConfirmDialog(backupAction.isBackup)
                is BackupAction.ShowAccountDialog -> showAccountDialog()
            }
        }
    }

    private fun selectMessageToShowSnackbar(messageType: BackupMessageType) {
        when (messageType) {
            BackupMessageType.NO_INTERNET -> showSnackbar(R.string.sb_bp_no_internet, false)
            BackupMessageType.FILE_NO_EXISTS -> showSnackbar(R.string.sb_bp_backup_not_found, false)
            BackupMessageType.BACKUP_SUCCESS -> showSnackbar(R.string.sb_bp_succes, true)
            BackupMessageType.RESTORE_SUCCESS -> showSnackbar(R.string.sb_bp_restore_succes, true)
            BackupMessageType.BACKUP_FAILURE -> showSnackbar(R.string.sb_bp_failure, false)
            BackupMessageType.RESTORE_FAILURE -> showSnackbar(R.string.sb_bp_restore_failure, false)
            BackupMessageType.SIGN_OUT -> showSnackbar(R.string.sb_bp_logged_account, true)
            BackupMessageType.DELETE_ACCOUNT -> showSnackbar(R.string.sb_bp_delete_data_from_cloud, true)
        }
    }

    private fun showSnackbar(stringId: Int, success: Boolean) {
        CustomizedSnackbar.make(requireView(), getString(stringId), success, binding.bottomBarBackup)?.show()
    }

    private fun showConfirmDialog(isBackup: Boolean) {
        val action = BackupNewFragmentDirections.actionOpenCloudConfirmSheetDialogFragment(isBackup)
        findNavController().navigate(action)
    }

    private fun showAccountDialog() {
        val action = BackupNewFragmentDirections.actionOpenAccountSheetDialogFragment()
        findNavController().navigate(action)
    }

    companion object {
        const val CLOUD_BACKUP_REQUEST_KEY = "CLOUD_BACKUP_REQUEST_KEY"
        const val SIGN_OUT_REQUEST_KEY = "SIGN_OUT_REQUEST_KEY"
        const val DELETE_ACCOUNT_REQUEST_KEY = "DELETE_ACCOUNT_REQUEST_KEY"
        const val CLOUD_BACKUP_BUNDLE_KEY = "CLOUD_BACKUP_BUNDLE_KEY"
    }
}