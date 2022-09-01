package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.contracts.AuthContract
import com.loskon.noteminimalism3.app.base.contracts.StorageContract
import com.loskon.noteminimalism3.app.base.extension.dialogfragment.dismissShowing
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.setFragmentResultListener
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setAllMenuItemsVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.presentation.dialogfragment.WaitingDialogFragment
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.ConfirmSheetDialogFragment
import com.loskon.noteminimalism3.app.base.widget.snackbar.AppSnackbar
import com.loskon.noteminimalism3.app.presentation.screens.AccountSheetDialogFragment
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.AuthIntent
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupAction
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupMessageType
import com.loskon.noteminimalism3.databinding.FragmentBackupNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BackupNewFragment : Fragment(R.layout.fragment_backup_new) {

    private val binding by viewBinding(FragmentBackupNewBinding::bind)
    private val viewModel: BackupViewModel by viewModel()

    private var waitingDialog = WaitingDialogFragment.newInstance()
    private var isBackup: Boolean = false

    private val authContract = AuthContract(this)
    private val storageContract = StorageContract(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(LOCAL_BACKUP_REQUEST_KEY) { bundle ->
            val stringId = bundle.getInt(LOCAL_BACKUP_BUNDLE_STRING_ID_KEY)
            val success = bundle.getBoolean(LOCAL_BACKUP_BUNDLE_SUCCESS_KEY)
            showSnackbar(stringId, success)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.hasAuthorizedUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        installObservers()
        setupContractsListeners()
        setupViewsListeners()
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

    private fun installObservers() {
        viewModel.getBackupState.observe(viewLifecycleOwner) { backupState ->
            binding.bottomBarBackup.setAllMenuItemsVisibility(backupState.hasAuthorizedUser)
        }
        viewModel.getBackupAction.observe(viewLifecycleOwner) { backupAction ->
            when (backupAction) {
                is BackupAction.LaunchAuthContract -> authContract.launch(backupAction.intentSender)
                is BackupAction.ShowSnackbar -> closeWaitingDialogAndShowMessage(backupAction.messageType)
                is BackupAction.ShowConfirmSheetDialog -> showConfirmSheetDialog(backupAction.isBackup)
                is BackupAction.ShowAccountDialog -> showAccountDialog()
                else -> {}
            }
        }
    }

    private fun setupContractsListeners() {
        authContract.setHandleResultDataListener { intent ->
            showWaitingDialog()
            viewModel.authenticationWithSelectWay(intent)
        }
        storageContract.setHandleGrantedListener { granted ->
            if (granted) {
                if (isBackup) {
                    showLocalBackupSheetDialog()
                } else {
                    showRestoreListSheetDialog()
                }
            } else {
                showSnackbar(R.string.no_permissions, success = false)
            }
        }
    }

    private fun showWaitingDialog() {
        waitingDialog.show(childFragmentManager, WaitingDialogFragment.TAG)
    }

    private fun setupViewsListeners() {
        with(binding) {
            btnBackupLocal.setDebounceClickListener {
                if (storageContract.storageAccess(requireContext())) {
                    showLocalBackupSheetDialog()
                } else {
                    isBackup = true
                    storageContract.launch()
                }
            }
            btnRestoreLocal.setDebounceClickListener {
                if (storageContract.storageAccess(requireContext())) {
                    showRestoreListSheetDialog()
                } else {
                    isBackup = false
                    storageContract.launch()
                }
            }
            btnBackupCloud.setDebounceClickListener {
                viewModel.checkInternetBeforeShowConfirmSheetDialog(isBackup = true)
            }
            btnRestoreCloud.setDebounceClickListener {
                viewModel.checkInternetBeforeShowConfirmSheetDialog(isBackup = false)
            }
            bottomBarBackup.setDebounceMenuItemClickListener { item ->
                if (item.itemId == R.id.item_account) viewModel.checkInternetBeforeShowAccountDialog()
            }
            bottomBarBackup.setDebounceNavigationClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun closeWaitingDialogAndShowMessage(messageType: BackupMessageType) {
        waitingDialog.dismissShowing()
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
        AppSnackbar().make(binding.root, getString(stringId), success, binding.bottomBarBackup).show()
    }

    private fun showConfirmSheetDialog(isBackup: Boolean) {
        ConfirmSheetDialogFragment.newInstance(
            title = getString(R.string.sheet_confirm_action),
            btnOkText = getString(R.string.continue_action),
            btnCancelText = getString(R.string.no)
        ).apply {
            setOkClickListener {
                if (isBackup) {
                    checkingUserBeforeBackup()
                } else {
                    checkingUserBeforeRestore()
                }
            }
        }.show(childFragmentManager, ConfirmSheetDialogFragment.TAG)
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

    private fun showAccountDialog() {
        AccountSheetDialogFragment.newInstance().apply {
            setOnSignOutClickListener {
                viewModel.signOut()
            }
            setOnDeleteClickListener {
                showConfirmDeleteDialog()
            }
        }.show(childFragmentManager, AccountSheetDialogFragment.TAG)
    }

    private fun showConfirmDeleteDialog() {
        ConfirmSheetDialogFragment.newInstance(
            title = getString(R.string.sheet_deleting_data),
            btnOkText = getString(R.string.continue_action),
            btnCancelText = getString(android.R.string.cancel),
            message = getString(R.string.sheet_deleting_data_message)
        ).apply {
            setOkClickListener {
                viewModel.setAuthIntent(AuthIntent.DELETE_ACCOUNT)
                viewModel.deleteDatabaseFile()
                viewModel.getIntentSenderForAuthContract(requireActivity())
            }
        }.show(childFragmentManager, ConfirmSheetDialogFragment.TAG)
    }

    private fun showLocalBackupSheetDialog() {
        val action = BackupNewFragmentDirections.actionOpenLocalBackupSheetDialogFragment()
        findNavController().navigate(action)
    }

    private fun showRestoreListSheetDialog() {
        val action = BackupNewFragmentDirections.actionOpenRestoreListSheetDialogFragment()
        findNavController().navigate(action)
    }

    companion object {
        const val LOCAL_BACKUP_REQUEST_KEY = "LOCAL_BACKUP_REQUEST_KEY"
        const val LOCAL_BACKUP_BUNDLE_STRING_ID_KEY = "LOCAL_BACKUP_BUNDLE_STRING_ID_KEY"
        const val LOCAL_BACKUP_BUNDLE_SUCCESS_KEY = "LOCAL_BACKUP_BUNDLE_SUCCESS_KEY"
    }
}