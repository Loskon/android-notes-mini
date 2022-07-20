package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.contracts.AuthContract
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setAllMenuItemsVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.presentation.dialogfragment.WaitingDialogFragment
import com.loskon.noteminimalism3.app.base.presentation.sheetdialog.BaseCustomSheetDialog
import com.loskon.noteminimalism3.app.base.widget.snackbar.CustomizedSnackbar
import com.loskon.noteminimalism3.app.presentation.screens.AccountSheetDialogFragment
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupAction
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupMessageType
import com.loskon.noteminimalism3.databinding.FragmentBackupNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BackupNewFragment : Fragment(R.layout.fragment_backup_new) {

    private val binding by viewBinding(FragmentBackupNewBinding::bind)
    private val viewModel: BackupViewModel by viewModel()

    private val waitingDialog = WaitingDialogFragment.newInstance()

    private val authContract = AuthContract(this) { intent ->
        showWaitingDialog()
        viewModel.authenticationWithSelectWay(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.hasAuthorizedUser()
    }

    private fun checkingUserBeforeBackup() {
        if (viewModel.getBackupState.value.hasAuthorizedUser) {
            showWaitingDialog()
            viewModel.backupDatebaseFile()
        } else {
            viewModel.setAuthIntent(AuthIntent.BACKUP)
            viewModel.getIntentSenderForAuthContract(requireActivity())
        }
    }

    private fun checkingUserBeforeRestore() {
        if (viewModel.getBackupState.value.hasAuthorizedUser) {
            showWaitingDialog()
            viewModel.restoreDatabaseFile()
        } else {
            viewModel.setAuthIntent(AuthIntent.RESTORE)
            viewModel.getIntentSenderForAuthContract(requireActivity())
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
            btnBackupLocal.setDebounceClickListener { showAccountDialog() }
            btnRestoreLocal.setDebounceClickListener { }
            btnBackupCloud.setDebounceClickListener { onBackupCloudBtnClick() }
            btnRestoreCloud.setDebounceClickListener { onRestoreCloudBtnClick() }
            bottomBarBackup.setDebounceMenuItemClickListener { onMenuItemClick(it) }
            bottomBarBackup.setDebounceNavigationClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun onBackupCloudBtnClick() {
        viewModel.checkInternetBeforeShowConfirmSheetDialog(isBackup = true)
    }

    private fun onRestoreCloudBtnClick() {
        viewModel.checkInternetBeforeShowConfirmSheetDialog(isBackup = false)
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
                is BackupAction.ShowSnackbar -> closeDialogAndShowMessage(backupAction.messageType)
                is BackupAction.ShowConfirmSheetDialog -> showConfirmSheetDialog(backupAction.isBackup)
                is BackupAction.ShowAccountDialog -> showAccountDialog()
            }
        }
    }

    private fun closeDialogAndShowMessage(messageType: BackupMessageType) {
        waitingDialog.dismiss()
        selectMessageToShowSnackbar(messageType)
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
            BackupMessageType.DELETED_ACCOUNT -> showSnackbar(R.string.sb_bp_delete_data_from_cloud, true)
        }
    }

    private fun showSnackbar(stringId: Int, success: Boolean) {
        CustomizedSnackbar.make(requireView(), getString(stringId), success, binding.bottomBarBackup)?.show()
    }

    private fun showConfirmSheetDialog(isBackup: Boolean) {
        BaseCustomSheetDialog(requireContext()).create {
            setTitleDialog(R.string.sheet_confirm_action)
            setTextBtnOk(R.string.continue_action)
            setTextBtnCancel(R.string.no)
        }.setOnClickBtnOk {
            if (isBackup) {
                checkingUserBeforeBackup()
            } else {
                checkingUserBeforeRestore()
            }
        }.show()
    }

    private fun showAccountDialog() {
        AccountSheetDialogFragment.newInstance().show(childFragmentManager, AccountSheetDialogFragment.TAG)
    }

    private fun deleteAccount() {
        viewModel.setAuthIntent(AuthIntent.DELETE_ACCOUNT)
        viewModel.deleteDatabaseFile()
        viewModel.getIntentSenderForAuthContract(requireActivity())
    }

    private fun showWaitingDialog() {
        waitingDialog.show(childFragmentManager, WaitingDialogFragment.TAG)
    }

    companion object {
        const val CLOUD_BACKUP_REQUEST_KEY = "CLOUD_BACKUP_REQUEST_KEY"
        const val SIGN_OUT_REQUEST_KEY = "SIGN_OUT_REQUEST_KEY"
        const val DELETE_ACCOUNT_REQUEST_KEY = "DELETE_ACCOUNT_REQUEST_KEY"
        const val CLOUD_BACKUP_BUNDLE_KEY = "CLOUD_BACKUP_BUNDLE_KEY"
    }
}