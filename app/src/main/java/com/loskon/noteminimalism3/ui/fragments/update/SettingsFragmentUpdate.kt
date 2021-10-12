package com.loskon.noteminimalism3.ui.fragments.update

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.RequestCode
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.files.ActivityResultInterface
import com.loskon.noteminimalism3.files.Result
import com.loskon.noteminimalism3.permissions.PermissionsInterface
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.ui.activities.update.SettingsActivityUpdate
import com.loskon.noteminimalism3.ui.sheets.SheetPrefLinks
import com.loskon.noteminimalism3.ui.sheets.SheetPrefNoteFontSize
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSort
import com.loskon.noteminimalism3.ui.sheets.update.SheetPrefSliderNumberBackups
import com.loskon.noteminimalism3.ui.sheets.update.SheetPrefSliderRetention
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.ColorUtil
import com.loskon.noteminimalism3.utils.IntentUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Форма общих настроек
 */

class SettingsFragmentUpdate :
    PreferenceFragmentCompat(),
    Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener,
    SheetPrefSliderNumberBackups.CallbackNumberBackups,
    SheetPrefSliderRetention.CallbackRetention,
    PermissionsInterface,
    ActivityResultInterface {

    private lateinit var activity: SettingsActivityUpdate

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
    private var numberBackupsKey: String = ""

    // Notes
    private var hyperlinksKey: String = ""
    private var fontSizeKey: String = ""

    // Other
    private var retentionKey: String = ""
    private var communicationKey: String = ""

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
    private var numberBackups: Preference? = null

    // Notes
    private var hyperlinks: Preference? = null
    private var fontSize: Preference? = null

    // Other
    private var retention: Preference? = null
    private var communication: Preference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivityUpdate
        PermissionsStorageUpdate.installingVerification(this, this)
        Result.installing(activity, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(null)
        setDividerHeight(0)
        listView.isVerticalScrollBarEnabled = false

        configurationBottomBar()
        installCallbacks()
    }

    private fun configurationBottomBar() {
        activity.apply {
            bottomBar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun installCallbacks() {
        SheetPrefSliderNumberBackups.listenerCallback(this)
        SheetPrefSliderRetention.listenerCallback(this)
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
        darkModeKey = getString(R.string.dark_mode_title)
        // Data
        backupKey = getString(R.string.backup_title)
        folderKey = getString(R.string.folder_title)
        autoBackupKey = getString(R.string.auto_backup_title)
        numberBackupsKey = getString(R.string.num_of_backup_title)
        // Notes
        hyperlinksKey = getString(R.string.hyperlinks_title)
        fontSizeKey = getString(R.string.font_size_notes_title)
        // Other
        retentionKey = getString(R.string.retention_trash_title)
        communicationKey = getString(R.string.communication_title)
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
        numberBackups = findPreference(numberBackupsKey)
        // Notes
        hyperlinks = findPreference(hyperlinksKey)
        fontSize = findPreference(fontSizeKey)
        // Other
        retention = findPreference(retentionKey)
        communication = findPreference(communicationKey)
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
    }

    override fun onResume() {
        super.onResume()
        installSummaryPreferences()
        otherConfigurations()
    }

    private fun installSummaryPreferences() {
        // Data
        folder?.summary = BackupPath.getNamePath(activity)

        val number: Int = AppPref.getNumberBackups(activity)
        numberBackups?.summary = number.toString()

        // Other
        val range: Int = AppPref.getRetentionRange(activity)
        retention?.summary = activity.getString(R.string.number_of_days_summary, range)
    }

    private fun otherConfigurations() {
        if (!PermissionsStorageUpdate.hasAccessStorage(activity)) autoBackup?.isChecked = false
    }

    private val hasAccessStorageRequest: Boolean
        get() {
            return PermissionsStorageUpdate.hasAccessStorageRequest(activity)
        }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        val key: String? = preference?.key
        selectedPreference = key.toString()

        activity.apply {
            when (key) {
                customizationKey -> {
                    lifecycleScope.launch {
                        delay(200L)
                        replaceFragment(SettingsAppFragmentUpdate())
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
                    SheetPrefSort(this).show()
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
                    if (hasAccessStorageRequest) Result.launcherSelectingFolder(
                        this
                    )
                    return true
                }

                numberBackupsKey -> {
                    SheetPrefSliderNumberBackups(this).show()
                    return true
                }

                hyperlinksKey -> {
                    SheetPrefLinks(this).show()
                    return true
                }

                retentionKey -> {
                    SheetPrefSliderRetention(this).show()
                    return true
                }

                fontSizeKey -> {
                    SheetPrefNoteFontSize(this).show()
                    return true
                }

                communicationKey -> {
                    IntentUtil.launcherEmailClient(this)
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
            ColorUtil.setDarkTheme(newValue as Boolean)
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
                Result.launcherSelectingFolder(activity)
            }
        } else {
            if (selectedPreference == autoBackupKey) {
                autoBackup?.isChecked = false
            }

            activity.showSnackbar(SnackbarApp.MSG_NO_PERMISSION)
        }
    }

    override fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?) {
        if (isGranted) {
            if (requestCode == RequestCode.REQUEST_CODE_READ) {
                if (data != null) {
                    if (data.path?.contains("primary") == true) {
                        val backupPath: String = BackupPath.findFullPath(data.path)
                        AppPref.setBackupPath(activity, backupPath)
                    } else {
                        activity.showSnackbar(SnackbarApp.MSG_LOCAL_STORAGE)
                    }
                } else {
                    activity.showSnackbar(SnackbarApp.MSG_UNABLE_SELECT_FOLDER)
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