package com.loskon.noteminimalism3.ui.fragments.update

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.backup.update.DateBaseCloudBackup
import com.loskon.noteminimalism3.other.InternetCheckUpdate
import com.loskon.noteminimalism3.permissions.PermissionsInterface
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.ui.activities.update.SettingsActivityUpdate
import com.loskon.noteminimalism3.ui.sheets.SheetBackupDateBase
import com.loskon.noteminimalism3.ui.sheets.SheetRestoreDateBase
import com.loskon.noteminimalism3.ui.sheets.update.SheetCloudConfirmUpdate
import com.loskon.noteminimalism3.ui.sheets.update.SheetPrefDateAccountUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp

/**
 * Форма для бэкапа/восстановления базы данных
 */

class BackupFragment : Fragment(),
    PermissionsInterface,
    View.OnClickListener {

    private lateinit var activity: SettingsActivityUpdate
    private lateinit var cloudBackup: DateBaseCloudBackup

    private lateinit var btnBackupSD: Button
    private lateinit var btnRestoreSD: Button
    private lateinit var btnBackupCloud: Button
    private lateinit var btnResetCloud: Button

    private var btnId: Int? = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivityUpdate
        PermissionsStorageUpdate.installingVerification(this, this)
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
        //initObjects()
        establishColorViews()
        clickListener()
        installHandlers()

/*
        setupParameters()
        configureOtherViews()
        installNoteHandlers()*/
    }

    private fun configurationBottomBar() {
        activity.apply {
            bottomBar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun establishColorViews() {
        val color = AppPref.getAppColor(activity)
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
                    SheetBackupDateBase(activity).show()
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
            return PermissionsStorageUpdate.hasAccessStorageRequest(activity)
        }

    private fun checkForInternet(): Boolean {
        return if (hasInternetConnection) {
            true
        } else {
            showSnackbar(SnackbarApp.MSG_TEXT_NO_INTERNET)
            false
        }
    }

    private val hasInternetConnection: Boolean
        get() {
            return InternetCheckUpdate.isConnected(activity)
        }

    private fun installHandlers() {
        activity.bottomBar.setOnMenuItemClickListener { item: MenuItem ->
            if (item.itemId == R.id.action_account) {
                if (checkForInternet()) {
                    SheetPrefDateAccountUpdate(activity, this).show()
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
                SheetBackupDateBase(activity).show()
            } else if (btnId == R.id.btn_restore_sd) {
                SheetRestoreDateBase(activity).show()
            }
        } else {
            showSnackbar(SnackbarApp.MSG_NO_PERMISSION)
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