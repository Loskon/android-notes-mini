package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DateBaseCloudBackup
import com.loskon.noteminimalism3.other.InternetCheck
import com.loskon.noteminimalism3.request.permissions.ResultAccessStorage
import com.loskon.noteminimalism3.request.permissions.ResultAccessStorageInterface
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.sheets.SheetBackupNameDateBase
import com.loskon.noteminimalism3.ui.sheets.SheetCloudConfirmUpdate
import com.loskon.noteminimalism3.ui.sheets.SheetGoogleAccount
import com.loskon.noteminimalism3.ui.sheets.SheetRestoreDateBase
import com.loskon.noteminimalism3.ui.snackbars.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager

/**
 * Форма для бэкапа/восстановления базы данных
 */

class BackupFragment : Fragment(),
    ResultAccessStorageInterface,
    View.OnClickListener {

    private lateinit var activity: SettingsActivity
    private lateinit var cloudBackup: DateBaseCloudBackup

    private lateinit var btnBackupSD: Button
    private lateinit var btnRestoreSD: Button
    private lateinit var btnBackupCloud: Button
    private lateinit var btnResetCloud: Button

    private var btnId: Int? = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
        ResultAccessStorage.installingVerification(this, this)
        cloudBackup = DateBaseCloudBackup(activity, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_backup, container, false)
        btnBackupSD = view.findViewById(R.id.btn_backup_sd)
        btnRestoreSD = view.findViewById(R.id.btn_restore_sd)
        btnBackupCloud = view.findViewById(R.id.btn_backup_cloud)
        btnResetCloud = view.findViewById(R.id.btn_restore_cloud)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurationBottomBar()
        establishColorViews()
        clickListener()
        installHandlers()
    }

    private fun configurationBottomBar() {
        activity.apply {
            bottomBar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun establishColorViews() {
        val color = PrefManager.getAppColor(activity)
        btnBackupSD.setBackgroundColor(color)
        btnRestoreSD.setBackgroundColor(color)
        btnBackupCloud.setBackgroundColor(color)
        btnResetCloud.setBackgroundColor(color)
    }

    private fun clickListener() {
        btnBackupSD.setOnClickListener(this)
        btnRestoreSD.setOnClickListener(this)
        btnBackupCloud.setOnClickListener(this)
        btnResetCloud.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        BaseSnackbar.dismiss()
        btnId = view?.id

        when (btnId) {
            R.id.btn_backup_sd -> {
                if (hasAccessStorageRequest) {
                    SheetBackupNameDateBase(activity).show()
                }
            }

            R.id.btn_restore_sd -> {
                if (hasAccessStorageRequest) {
                    SheetRestoreDateBase(activity).show()
                }
            }

            R.id.btn_backup_cloud -> {
                if (checkForInternet()) {
                    SheetCloudConfirmUpdate(activity, this).show(true)
                }
            }

            R.id.btn_restore_cloud -> {
                if (checkForInternet()) {
                    SheetCloudConfirmUpdate(activity, this).show(false)
                }

            }
        }
    }

    private val hasAccessStorageRequest: Boolean
        get() {
            return ResultAccessStorage.hasAccessStorageRequest(activity)
        }

    private fun checkForInternet(): Boolean {
        return if (hasInternetConnection) {
            true
        } else {
            showSnackbar(SnackbarManager.MSG_TEXT_NO_INTERNET)
            false
        }
    }

    private val hasInternetConnection: Boolean
        get() {
            return InternetCheck.isConnected(activity)
        }

    private fun installHandlers() {
        activity.bottomBar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_account) {
                if (checkForInternet()) {
                    SheetGoogleAccount(activity, this).show()
                }
                return@setOnMenuItemClickListener true
            }
            false
        }
    }

    override fun onDetach() {
        super.onDetach()
        BaseSnackbar.dismiss()
        visibilityMenuItemAccount(false)
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            if (btnId == R.id.btn_backup_sd) {
                SheetBackupNameDateBase(activity).show()
            } else if (btnId == R.id.btn_restore_sd) {
                SheetRestoreDateBase(activity).show()
            }
        } else {
            showSnackbar(SnackbarManager.MSG_NO_PERMISSION)
        }
    }

    fun showSnackbar(typeMessage: String) = activity.showSnackbar(typeMessage)

    fun visibilityMenuItemAccount(isVisible: Boolean) =
        activity.visibilityMenuItemAccount(isVisible)

    fun performBackupCloud() = cloudBackup.performBackupCloud()

    fun performRestoreCloud() = cloudBackup.performRestoreCloud()

    fun signOutFromGoogle() = cloudBackup.signOutFromGoogle()

    fun deleteUserAccount() = cloudBackup.deleteUserAccount()
}