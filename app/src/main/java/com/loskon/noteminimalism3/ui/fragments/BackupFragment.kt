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
import com.google.android.material.bottomappbar.BottomAppBar
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
import com.loskon.noteminimalism3.ui.sheetdialogs.CreateBackupSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.FileListSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.GoogleAccountSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Экран для бэкапа/восстановления БД
 */

class BackupFragment : Fragment(),
    ResultAccessStorageInterface,
    ResultActivityInterface,
    ResultGoogleInterface {

    private lateinit var activity: SettingsActivity
    private lateinit var resultActivity: ResultActivity
    private lateinit var storageAccess: ResultStorageAccess
    private lateinit var resultGoogle: ResultGoogle
    private lateinit var cloudBackup: DataBaseCloudBackup

    private lateinit var btnBackupSD: Button
    private lateinit var btnRestoreSD: Button
    private lateinit var btnBackupCloud: Button
    private lateinit var btnRestoreCloud: Button
    private lateinit var bottomAppBar: BottomAppBar

    private var isBackupSd: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
        bottomAppBar = activity.bottomAppBar
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
        resultGoogle = ResultGoogle(this, this)
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

    private fun installHandlersForViews() {
        btnBackupSD.setOnSingleClickListener { onBackupSdBtnClick() }
        btnRestoreSD.setOnSingleClickListener { onRestoreSdBtnClick() }
        btnBackupCloud.setOnSingleClickListener { onBackupCloudBtnClick() }
        btnRestoreCloud.setOnSingleClickListener { onRestoreCloudBtnClick() }
        bottomAppBar.setOnMenuItemClickListener { onMenuItemClick(it) }
    }

    private fun onBackupSdBtnClick() {
        WarningSnackbar.dismiss()
        isBackupSd = true
        if (hasAccessStorageRequest) showCreateBackupSheetDialog()
    }

    private val hasAccessStorageRequest: Boolean
        get() {
            return storageAccess.hasAccessStorageRequest()
        }

    private fun showCreateBackupSheetDialog() {
        CreateBackupSheetDialog(activity).show()
    }

    private fun onRestoreSdBtnClick() {
        WarningSnackbar.dismiss()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            resultActivity.launcherSelectingDateBaseFile()
        } else {
            isBackupSd = false
            if (hasAccessStorageRequest) showListRestoreSheetDialog()
        }
    }

    private fun showListRestoreSheetDialog() {
        FileListSheetDialog(activity).show()
    }

    private fun onBackupCloudBtnClick() {
        WarningSnackbar.dismiss()
        if (hasInternetConnection()) showCloudConfirmSheetDialog(true)
    }

    private fun onRestoreCloudBtnClick() {
        WarningSnackbar.dismiss()
        if (hasInternetConnection()) showCloudConfirmSheetDialog(false)
    }

    private fun hasInternetConnection(): Boolean {
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

    private fun showCloudConfirmSheetDialog(isBackup: Boolean) {
        CloudConfirmSheetDialog(this).show(isBackup)
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_account) {
            if (hasInternetConnection()) {
                GoogleAccountSheetDialog(this).show()
            }
            return true
        }

        return false
    }

    override fun onDetach() {
        bottomAppBar.setOnMenuItemClickListener(null)
        changeVisibilityMenuItem(false)
        super.onDetach()
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            if (isBackupSd) {
                showCreateBackupSheetDialog()
            } else {
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
        if (isGranted) {
            if (cloudBackup.isBackup()) {
                cloudBackup.uploadCopyDateBaseToCloud()
            } else {
                cloudBackup.checkingPresenceFile()
            }
        } else {
            showSnackbar(WarningSnackbar.MSG_TEXT_SIGN_IN_FAILED)
        }

        cloudBackup.changeVisibilityMenuItemAccount()
    }

    fun showSnackbar(messageType: String) = activity.showSnackbar(messageType)

    fun changeVisibilityMenuItem(isVisible: Boolean) = activity.changeVisibilityMenuItem(isVisible)

    //--- Внешние методы ---------------------------------------------------------------------------
    fun performBackupCloud() = cloudBackup.performBackupCloud()

    fun performRestoreCloud() = cloudBackup.performRestoreCloud()

    fun signOutFromGoogle() = cloudBackup.signOutFromGoogle()

    fun deleteUserAccount() = cloudBackup.deleteUserAccount()

    //--- interface --------------------------------------------------------------------------------
    interface RestoreNoteAndroidRCallback {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: RestoreNoteAndroidRCallback? = null

        fun registerRestoreNoteAndroidRCallback(callback: RestoreNoteAndroidRCallback) {
            this.callback = callback
        }
    }
}