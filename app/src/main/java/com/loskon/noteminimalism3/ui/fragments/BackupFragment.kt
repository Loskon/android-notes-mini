package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DateBaseBackup
import com.loskon.noteminimalism3.backup.DateBaseCloudBackup
import com.loskon.noteminimalism3.other.InternetCheck
import com.loskon.noteminimalism3.request.RequestCode
import com.loskon.noteminimalism3.request.activity.ResultActivity
import com.loskon.noteminimalism3.request.activity.ResultActivityInterface
import com.loskon.noteminimalism3.request.storage.ResultAccessStorage
import com.loskon.noteminimalism3.request.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.sheets.SheetBackupNameDateBase
import com.loskon.noteminimalism3.ui.sheets.SheetCloudConfirm
import com.loskon.noteminimalism3.ui.sheets.SheetGoogleAccount
import com.loskon.noteminimalism3.ui.sheets.SheetListRestoreDateBase
import com.loskon.noteminimalism3.ui.snackbars.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager


/**
 * Форма для бэкапа/восстановления базы данных
 */

class BackupFragment : Fragment(),
    ResultAccessStorageInterface,
    ResultActivityInterface,
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
        ResultActivity.installing(this, this)
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ResultActivity.launcherSelectingDateBaseFile(activity)
                } else {
                    if (hasAccessStorageRequest) {
                        SheetListRestoreDateBase(activity).show()
                    }
                }
            }

            R.id.btn_backup_cloud -> {
                if (checkForInternet()) {
                    SheetCloudConfirm(activity, this).show(true)
                }
            }

            R.id.btn_restore_cloud -> {
                if (checkForInternet()) {
                    SheetCloudConfirm(activity, this).show(false)
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
                SheetListRestoreDateBase(activity).show()
            }
        } else {
            showSnackbar(SnackbarManager.MSG_NO_PERMISSION)
        }
    }

    override fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?) {
        if (isGranted) {
            if (requestCode == RequestCode.REQUEST_CODE_GET_BACKUP_FILE) {
                try {
                    restoreDateBase(data)
                } catch (exception: Exception) {
                    showSnackbar(SnackbarManager.MSG_RESTORE_FAILED)
                }
            }
        } else {
            showSnackbar(SnackbarManager.MSG_BACKUP_NOT_SELECTED)
        }
    }

    private fun restoreDateBase(data: Uri?) {
        if (data != null) {
            val isRestoreFailed: Boolean = DateBaseBackup.performRestore(activity, data)
            callback?.onRestoreNotes()

            if (isRestoreFailed) {
                showSnackbar(SnackbarManager.MSG_RESTORE_COMPLETED)
            } else {
                showSnackbar(SnackbarManager.MSG_INVALID_FORMAT_FILE)
            }
        }
    }

    fun showSnackbar(typeMessage: String) = activity.showSnackbar(typeMessage)

    fun visibilityMenuItemAccount(isVisible: Boolean) =
        activity.visibilityMenuItemAccount(isVisible)

    fun performBackupCloud() = cloudBackup.performBackupCloud()

    fun performRestoreCloud() = cloudBackup.performRestoreCloud()

    fun signOutFromGoogle() = cloudBackup.signOutFromGoogle()

    fun deleteUserAccount() = cloudBackup.deleteUserAccount()

    interface CallbackRestoreNoteAndroidR {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: CallbackRestoreNoteAndroidR? = null

        fun listenerCallback(callback: CallbackRestoreNoteAndroidR) {
            this.callback = callback
        }
    }
}