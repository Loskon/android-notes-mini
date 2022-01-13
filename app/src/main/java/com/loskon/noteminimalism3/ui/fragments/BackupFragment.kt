package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DataBaseBackup
import com.loskon.noteminimalism3.backup.DataBaseCloudBackup
import com.loskon.noteminimalism3.other.InternetCheck
import com.loskon.noteminimalism3.requests.activity.REQUEST_CODE_BACKUP_FILE
import com.loskon.noteminimalism3.requests.activity.ResultActivity
import com.loskon.noteminimalism3.requests.activity.ResultActivityInterface
import com.loskon.noteminimalism3.requests.google.ResultGoogle
import com.loskon.noteminimalism3.requests.google.ResultGoogleInterface
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.requests.storage.ResultStorageAccess
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.sheetdialogs.CloudConfirmSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.FileListSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.GoogleAccountSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.NameBackupSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningBaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar

/**
 * Экран для бэкапа/восстановления БД
 */

class BackupFragment : Fragment(),
    ResultAccessStorageInterface,
    ResultActivityInterface,
    ResultGoogleInterface,
    View.OnClickListener {

    private lateinit var activity: SettingsActivity
    private lateinit var resultActivity: ResultActivity
    private lateinit var storageAccess: ResultStorageAccess
    private lateinit var resultGoogle: ResultGoogle
    private lateinit var cloudBackup: DataBaseCloudBackup

    private lateinit var btnBackupSD: Button
    private lateinit var btnRestoreSD: Button
    private lateinit var btnBackupCloud: Button
    private lateinit var btnRestoreCloud: Button

    private var btnId: Int? = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
        configureRequestPermissions()
    }

    private fun configureRequestPermissions() {
        // storage
        storageAccess = ResultStorageAccess(activity, this, this)
        storageAccess.installingContracts()
        // activity
        resultActivity = ResultActivity(activity, this, this)
        resultActivity.installingContracts()
        // google
        resultGoogle = ResultGoogle(activity, this, this)
        resultGoogle.installingContracts()
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
        btnRestoreCloud = view.findViewById(R.id.btn_restore_cloud)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        establishViewsColor()
        initializingObjects()
        installHandlersForViews()
    }

    private fun initializingObjects() {
        cloudBackup = DataBaseCloudBackup(activity, this, resultGoogle)
    }

    private fun establishViewsColor() {
        val color: Int = PrefHelper.getAppColor(activity)
        btnBackupSD.setBackgroundColor(color)
        btnRestoreSD.setBackgroundColor(color)
        btnBackupCloud.setBackgroundColor(color)
        btnRestoreCloud.setBackgroundColor(color)
    }

    override fun onClick(view: View?) {
        WarningBaseSnackbar.dismiss()
        btnId = view?.id

        when (btnId) {
            R.id.btn_backup_sd -> {
                if (hasAccessStorageRequest) {
                    showNameBackupSheetDialog()
                }
            }

            R.id.btn_restore_sd -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    resultActivity.launcherSelectingDateBaseFile()
                } else {
                    if (hasAccessStorageRequest) {
                        showListRestoreSheetDialog()
                    }
                }
            }

            R.id.btn_backup_cloud -> {
                if (checkForInternet()) {
                    showCloudConfirmSheetDialog(true)
                }
            }

            R.id.btn_restore_cloud -> {
                if (checkForInternet()) {
                    showCloudConfirmSheetDialog(false)
                }

            }
        }
    }

    private val hasAccessStorageRequest: Boolean
        get() {
            return storageAccess.hasAccessStorageRequest()
        }

    private fun checkForInternet(): Boolean {
        return if (hasInternetConnection) {
            true
        } else {
            showSnackbar(WarningSnackbar.MSG_TEXT_NO_INTERNET)
            false
        }
    }

    private val hasInternetConnection: Boolean
        get() {
            return InternetCheck.isConnected(activity)
        }

    private fun showNameBackupSheetDialog() {
        NameBackupSheetDialog(activity).show()
    }

    private fun showListRestoreSheetDialog() {
        FileListSheetDialog(activity).show()
    }

    private fun showCloudConfirmSheetDialog(isBackup: Boolean) {
        CloudConfirmSheetDialog(this).show(isBackup)
    }

    private fun installHandlersForViews() {
        btnBackupSD.setOnClickListener(this)
        btnRestoreSD.setOnClickListener(this)
        btnBackupCloud.setOnClickListener(this)
        btnRestoreCloud.setOnClickListener(this)
        activity.bottomAppBar.setOnMenuItemClickListener { onMenuItemClick(it) }
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_account) {
            if (checkForInternet()) {
                GoogleAccountSheetDialog(this).show()
            }
            return true
        }

        return false
    }

    override fun onDetach() {
        super.onDetach()
        WarningBaseSnackbar.dismiss()
        changeVisibilityMenuItem(false)
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            if (btnId == R.id.btn_backup_sd) {
                showNameBackupSheetDialog()
            } else if (btnId == R.id.btn_restore_sd) {
                showListRestoreSheetDialog()
            }
        } else {
            showSnackbar(WarningSnackbar.MSG_NO_PERMISSION)
        }
    }

    override fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?) {
        if (isGranted) {
            if (requestCode == REQUEST_CODE_BACKUP_FILE) {
                try {
                    restoreDateBase(data)
                } catch (exception: Exception) {
                    showSnackbar(WarningSnackbar.MSG_RESTORE_FAILED)
                }
            }
        } else {
            showSnackbar(WarningSnackbar.MSG_BACKUP_NOT_SELECTED)
        }
    }

    private fun restoreDateBase(data: Uri?) {
        if (data != null) {
            val isRestoreFailed: Boolean = DataBaseBackup.performRestore(activity, data)

            if (isRestoreFailed) {
                callback?.onRestoreNotes()
                showSnackbar(WarningSnackbar.MSG_RESTORE_COMPLETED)
            } else {
                showSnackbar(WarningSnackbar.MSG_INVALID_FORMAT_FILE)
            }
        }
    }

    override fun onRequestGoogleResult(isGranted: Boolean) {
        cloudBackup.apply {
            if (isGranted) {
                if (isBackup()) {
                    uploadCopyDateBaseToCloud()
                } else {
                    checkingPresenceFile()
                }
            } else {
                showSnackbar(WarningSnackbar.MSG_TEXT_SIGN_IN_FAILED)
            }

            changeVisibilityMenuItemAccount()
        }
    }

    fun showSnackbar(messageType: String) = activity.showSnackbar(messageType)

    fun changeVisibilityMenuItem(isVisible: Boolean) = activity.changeVisibilityMenuItem(isVisible)

    fun performBackupCloud() = cloudBackup.performBackupCloud()

    fun performRestoreCloud() = cloudBackup.performRestoreCloud()

    fun signOutFromGoogle() = cloudBackup.signOutFromGoogle()

    fun deleteUserAccount() = cloudBackup.deleteUserAccount()

    interface RestoreNoteAndroidRCallback {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: RestoreNoteAndroidRCallback? = null

        fun registerCallbackRestoreNoteAndroidR(callback: RestoreNoteAndroidRCallback) {
            this.callback = callback
        }
    }
}