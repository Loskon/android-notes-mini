package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setAllMenuItemsVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.widget.snackbar.CustomizedSnackbar
import com.loskon.noteminimalism3.databinding.FragmentBackupNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BackupNewFragment : Fragment(R.layout.fragment_backup_new) {

    private val binding by viewBinding(FragmentBackupNewBinding::bind)
    private val viewModel: BackupViewModel by viewModel()

    private val authContract = AuthContract(this) { viewModel.signInToGoogle(requireActivity(), it) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.hasAuthorizedUser()
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
            btnRestoreCloud.setDebounceClickListener { }
            bottomBarBackup.setDebounceMenuItemClickListener { onMenuItemClick(it) }
            bottomBarBackup.setDebounceNavigationClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun onBackupCloudBtnClick() {
        /*        if (viewModel.getAuthState.value.hasAuthorizedUser) {

                } else {

                }*/
        viewModel.getGoogleAuthIntentSender(requireActivity())
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_account) {
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
        viewModel.getAuthState.observe(viewLifecycleOwner) { authState ->
            binding.bottomBarBackup.setAllMenuItemsVisibility(authState.showAccountMenuItem)
        }
        viewModel.getAuthAction.observe(viewLifecycleOwner) { authAction ->
            when (authAction) {
                is AuthAction.LaunchAuthContract -> authContract.launch(authAction.intentSender)
                is AuthAction.VerificationResult -> TODO()
            }
        }
    }

    fun showSnackbar(message: String, success: Boolean) {
        CustomizedSnackbar.makeShow(requireView(), message, success, binding.bottomBarBackup)
    }
}