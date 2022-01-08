package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.managers.ColorManager
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.requests.RequestCode
import com.loskon.noteminimalism3.requests.activity.ResultActivity
import com.loskon.noteminimalism3.requests.activity.ResultActivityInterface
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorage
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.sheets.*
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Форма общих настроек
 */

class SettingsFragment :
    AppBaseSettingsFragment(),
    Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener,
    NumberBackupsSheetDialog.NumberBackupsCallback,
    RetentionTimeSheetDialog.RetentionTimeCallback,
    ResultAccessStorageInterface,
    ResultActivityInterface {

    private lateinit var activity: SettingsActivity

    private var selectedPreference: String = ""

    // PreferenceScreen Keys
    // Appearance settings
    private var customizationKey: String = ""
    private var typeFontKey: String = ""
    private var sortingKey: String = ""
    private var darkModeKey: String = ""

    // Data
    private var backupKey: String = ""
    private var folderKey: String = ""
    private var autoBackupKey: String = ""
    private var autoBackupNotifyKey: String = ""
    private var numberBackupsKey: String = ""

    // Notes
    private var hyperlinksKey: String = ""
    private var fontSizeKey: String = ""

    // Other
    private var retentionKey: String = ""
    private var communicationKey: String = ""
    private var aboutAppKey: String = ""

    // Preferences and SwitchPreferences
    // Appearance settings
    private var customization: Preference? = null
    private var typeFont: Preference? = null
    private var sorting: Preference? = null
    private var darkMode: SwitchPreference? = null

    // Data
    private var backup: Preference? = null
    private var folder: Preference? = null
    private var autoBackup: SwitchPreference? = null
    private var autoBackupNotify: SwitchPreference? = null
    private var numberBackups: Preference? = null

    // Notes
    private var hyperlinks: Preference? = null
    private var fontSize: Preference? = null

    // Other
    private var retention: Preference? = null
    private var communication: Preference? = null
    private var aboutApp: Preference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
        ResultAccessStorage.installing(this, this)
        ResultActivity.installing(activity, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installCallbacks()
    }

    private fun installCallbacks() {
        NumberBackupsSheetDialog.registerCallbackNumberBackups(this)
        RetentionTimeSheetDialog.registerCallbackRetentionTime(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        getPreferenceScreenKeys()
        findPreferences()
        installPreferencesListener()
    }

    private fun getPreferenceScreenKeys() {
        // Appearance settings
        customizationKey = getString(R.string.custom_app_title)
        typeFontKey = getString(R.string.type_font_title)
        sortingKey = getString(R.string.sort_title)
        darkModeKey = getString(R.string.dark_mode_key)
        // Data
        backupKey = getString(R.string.backup_title)
        folderKey = getString(R.string.folder_title)
        autoBackupKey = getString(R.string.auto_backup_key)
        autoBackupNotifyKey = getString(R.string.notification_key)
        numberBackupsKey = getString(R.string.number_of_backup_key)
        // Notes
        hyperlinksKey = getString(R.string.hyperlinks_title)
        fontSizeKey = getString(R.string.font_size_notes_title)
        // Other
        retentionKey = getString(R.string.retention_trash_key)
        communicationKey = getString(R.string.communication_title)
        aboutAppKey = getString(R.string.about_app_title)
    }

    private fun findPreferences() {
        // Appearance settings
        customization = findPreference(customizationKey)
        typeFont = findPreference(typeFontKey)
        sorting = findPreference(sortingKey)
        darkMode = findPreference(darkModeKey)
        // Data
        backup = findPreference(backupKey)
        folder = findPreference(folderKey)
        autoBackup = findPreference(autoBackupKey)
        autoBackupNotify = findPreference(autoBackupNotifyKey)
        numberBackups = findPreference(numberBackupsKey)
        // Notes
        hyperlinks = findPreference(hyperlinksKey)
        fontSize = findPreference(fontSizeKey)
        // Other
        retention = findPreference(retentionKey)
        communication = findPreference(communicationKey)
        aboutApp = findPreference(aboutAppKey)
    }

    private fun installPreferencesListener() {
        // Appearance settings
        customization?.onPreferenceClickListener = this
        typeFont?.onPreferenceClickListener = this
        sorting?.onPreferenceClickListener = this
        darkMode?.onPreferenceChangeListener = this // Change
        // Data
        backup?.onPreferenceClickListener = this
        folder?.onPreferenceClickListener = this
        autoBackup?.onPreferenceChangeListener = this // Change
        numberBackups?.onPreferenceClickListener = this
        // Notes
        hyperlinks?.onPreferenceClickListener = this
        fontSize?.onPreferenceClickListener = this
        // Other
        retention?.onPreferenceClickListener = this
        communication?.onPreferenceClickListener = this
        aboutApp?.onPreferenceClickListener = this
    }

    override fun onResume() {
        super.onResume()
        installSummaryPreferences()
        otherConfigurations()
    }

    private fun installSummaryPreferences() {
        // Data
        folder?.summary = BackupPath.getSummary(activity)

        val number: Int = PrefHelper.getNumberBackups(activity)
        numberBackups?.summary = number.toString()

        // Other
        val range: Int = PrefHelper.getRetentionRange(activity)
        retention?.summary = activity.getString(R.string.number_of_days_summary, range)
    }

    private fun otherConfigurations() {
        if (!ResultAccessStorage.hasAccessStorage(activity)) {
            autoBackup?.isChecked = false
            autoBackupNotify?.isChecked = false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            folder?.isEnabled = false
        }
    }

    private val hasAccessStorageRequest: Boolean
        get() {
            return ResultAccessStorage.hasAccessStorageRequest(activity)
        }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        val key: String? = preference?.key
        selectedPreference = key.toString()

        activity.apply {
            when (key) {
                customizationKey -> {
                    lifecycleScope.launch {
                        delay(200L)
                        replaceFragment(SettingsAppFragment())
                    }
                    return true
                }

                typeFontKey -> {
                    lifecycleScope.launch {
                        delay(200L)
                        replaceFragment(FontsFragment())
                    }
                    return true
                }

                sortingKey -> {
                    SortWaySheetDialog(this).show()
                    return true
                }

                backupKey -> {
                    lifecycleScope.launch {
                        delay(200L)
                        replaceFragment(BackupFragment())
                    }
                    return true
                }

                folderKey -> {
                    if (hasAccessStorageRequest) ResultActivity.launcherSelectingFolder(this)
                    return true
                }

                numberBackupsKey -> {
                    NumberBackupsSheetDialog(this).show()
                    return true
                }

                hyperlinksKey -> {
                    LinksSheetDialog(this).show()
                    return true
                }

                retentionKey -> {
                    RetentionTimeSheetDialog(this).show()
                    return true
                }

                fontSizeKey -> {
                    NoteFontSizeSheetDialog(this).show()
                    return true
                }

                communicationKey -> {
                    IntentManager.launchEmailClient(this)
                    return true
                }

                aboutAppKey -> {
                    AboutAppSheetDialog(activity).show()
                    return true
                }

                else -> return false
            }
        }
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val key: String? = preference?.key
        selectedPreference = key.toString()

        if (key == darkModeKey) {
            ColorManager.setDarkTheme(newValue as Boolean)
            return true
        } else if (key == autoBackupKey) {
            hasAccessStorageRequest
            return true
        }

        return false
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            if (selectedPreference == folderKey) {
                ResultActivity.launcherSelectingFolder(activity)
            }
        } else {
            if (selectedPreference == autoBackupKey) {
                autoBackup?.isChecked = false
            }

            activity.showSnackbar(WarningSnackbar.MSG_NO_PERMISSION)
        }
    }

    override fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?) {
        if (isGranted) {
            if (requestCode == RequestCode.REQUEST_CODE_FOLDER_FOR_BACKUP) {
                if (data != null && data.path != null) {
                    if (data.path!!.contains("primary")) {
                        val backupPath: String = BackupPath.findFullPath(data.path!!)
                        PrefHelper.setBackupPath(activity, backupPath)
                    } else {
                        activity.showSnackbar(WarningSnackbar.MSG_LOCAL_STORAGE)
                    }
                } else {
                    activity.showSnackbar(WarningSnackbar.MSG_UNABLE_SELECT_FOLDER)
                }
            }
        }
    }

    override fun onChangeNumberBackups(number: Int) {
        numberBackups?.summary = number.toString()
    }

    override fun onChangeRetention(range: Int) {
        retention?.summary = activity.getString(R.string.number_of_days_summary, range)
    }
}