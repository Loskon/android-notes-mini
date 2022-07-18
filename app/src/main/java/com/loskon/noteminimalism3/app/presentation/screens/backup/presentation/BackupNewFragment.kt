package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.setFragmentResultListener
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setAllMenuItemsVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.widget.snackbar.BaseCustomSnackbar
import com.loskon.noteminimalism3.app.base.widget.snackbar.CustomizedSnackbar
import com.loskon.noteminimalism3.databinding.FragmentBackupNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BackupNewFragment : Fragment(R.layout.fragment_backup_new) {

    private val binding by viewBinding(FragmentBackupNewBinding::bind)
    private val viewModel: BackupViewModel by viewModel()

    private val authContract = AuthContract(this) { viewModel.signInToGoogle(requireActivity(), it, isBackup) }
    private var snackbar: BaseCustomSnackbar? = null
    private var isBackup: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.hasAuthorizedUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CLOUD_BACKUP_REQUEST_KEY) { bundle ->
            val isBackupResult = bundle.getBoolean(CLOUD_BACKUP_BUNDLE_KEY)
            bundle.remove(CLOUD_BACKUP_BUNDLE_KEY)

            if (isBackupResult) {

            } else {

            }
        }
    }

    fun checkingBeforeBackup() {
        if (viewModel.getBackupState.value.hasAuthorizedUser) {
            viewModel.backupDatebaseFile()
        } else {
            isBackup = true
            viewModel.getIntentSenderForAuthContract(requireActivity())
        }
    }

    fun checkingBeforeRetore() {
        if (viewModel.getBackupState.value.hasAuthorizedUser) {
            viewModel.restoreDatabaseFile()
        } else {
            isBackup = false
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
            btnBackupLocal.setDebounceClickListener { }
            btnRestoreLocal.setDebounceClickListener { }
            btnBackupCloud.setDebounceClickListener { onBackupCloudBtnClick() }
            btnRestoreCloud.setDebounceClickListener { onRestoreCloudBtnClick() }
            bottomBarBackup.setDebounceMenuItemClickListener { onMenuItemClick(it) }
            bottomBarBackup.setDebounceNavigationClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun onBackupCloudBtnClick() {
        val action = BackupNewFragmentDirections.actionOpenCloudConfirmSheetDialogFragment(true)
        findNavController().navigate(action)
    }

    private fun onRestoreCloudBtnClick() {
        val action = BackupNewFragmentDirections.actionOpenCloudConfirmSheetDialogFragment(false)
        findNavController().navigate(action)
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_account) {
            snackbar?.dismiss()
            viewModel.signOut()
            //showSnackbar("HI", true)
            // if (hasInternetConnection()) {
            //   GoogleAccountSheetDialog(this).show()
            // }
            return true
        }

        return false
    }

    private fun installObservers() {
        viewModel.getBackupState.observe(viewLifecycleOwner) { authState ->
            binding.bottomBarBackup.setAllMenuItemsVisibility(authState.hasAuthorizedUser)
        }
        viewModel.getBackupAction.observe(viewLifecycleOwner) { authAction ->
            when (authAction) {
                is AuthAction.LaunchAuthContract -> authContract.launch(authAction.intentSender)
                is AuthAction.ShowSnackbar -> selectMessageToShowSnackbar(authAction.message)
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
        }
    }

    private fun showSnackbar(stringId: Int, success: Boolean) {
        snackbar = CustomizedSnackbar.make(requireView(), getString(stringId), success, binding.bottomBarBackup)
        snackbar?.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Timber.d("dismiss")
                snackbar?.dismiss()
            }
            true
        }
        return view
    }

    companion object {
        const val CLOUD_BACKUP_REQUEST_KEY = "CLOUD_BACKUP_REQUEST_KEY"
        const val CLOUD_BACKUP_BUNDLE_KEY = "CLOUD_BACKUP_BUNDLE_KEY"
    }
}