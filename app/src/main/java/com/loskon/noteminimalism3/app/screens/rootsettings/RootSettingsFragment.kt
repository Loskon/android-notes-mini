package com.loskon.noteminimalism3.app.screens.rootsettings

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.contracts.FolderSelectContract
import com.loskon.noteminimalism3.base.contracts.StorageContract
import com.loskon.noteminimalism3.base.extension.dialogfragment.show
import com.loskon.noteminimalism3.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.base.extension.view.setPreferenceChangeListener
import com.loskon.noteminimalism3.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.managers.ColorManager
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.sheetdialogs.AboutAppSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.LinksSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.NoteFontSizeSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.NumberBackupsSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.RetentionTimeSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar

class RootSettingsFragment : BasePreferenceFragment() {

    private val storageContract = StorageContract(this)
    private val folderSelectContract = FolderSelectContract(this)

    // Appearance
    private var customization: Preference? = null
    private var typeFont: Preference? = null
    private var sorting: Preference? = null
    private var darkModeSwitch: SwitchPreference? = null

    // Data
    private var backup: Preference? = null
    private var folder: Preference? = null
    private var autoBackup: SwitchPreference? = null
    private var autoBackupNotifySwitch: SwitchPreference? = null
    private var numberBackups: Preference? = null

    // Notes
    private var hyperlinks: Preference? = null
    private var fontSize: Preference? = null

    // Other
    private var retention: Preference? = null
    private var communication: Preference? = null
    private var aboutApp: Preference? = null

    //
    private var selectedPreference: String = ""

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreferences()
        setupContractsListeners()
        setupPreferencesListeners()


        folder?.summary = BackupPath.getSummary(requireContext())

        val number = AppPreference.getNumberBackups(requireContext())
        numberBackups?.summary = number.toString()

        val range = AppPreference.getRetentionRange(requireContext())
        retention?.summary = requireContext().getString(R.string.number_of_days_summary, range)

        if (storageContract.storageAccess(requireContext()).not()) {
            autoBackup?.isChecked = false
            autoBackupNotifySwitch?.isChecked = false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            folder?.isEnabled = false
        }
    }

    private fun findPreferences() { // Appearance
        customization = findPreference(getString(R.string.custom_app_title))
        typeFont = findPreference(getString(R.string.type_font_title))
        sorting = findPreference(getString(R.string.sort_title))
        darkModeSwitch = findPreference(getString(R.string.dark_mode_key)) // Data
        backup = findPreference(getString(R.string.backup_title))
        folder = findPreference(getString(R.string.folder_title))
        autoBackup = findPreference(getString(R.string.auto_backup_key))
        autoBackupNotifySwitch = findPreference(getString(R.string.notification_key))
        numberBackups = findPreference(getString(R.string.number_of_backup_key)) // Notes
        hyperlinks = findPreference(getString(R.string.hyperlinks_title))
        fontSize = findPreference(getString(R.string.font_size_notes_title)) // Other
        retention = findPreference(getString(R.string.retention_trash_key))
        communication = findPreference(getString(R.string.communication_title))
        aboutApp = findPreference(getString(R.string.about_app_title))
    }

    private fun setupContractsListeners() {
        storageContract.setHandleGrantedListener { granted ->
            if (granted) {
                if (selectedPreference == "folderKey") folderSelectContract.launchFolderSelect()
            } else {
                if (selectedPreference == "autoBackupKey") autoBackup?.isChecked = false
                showSnackbar(getString(R.string.no_permissions), success = false)
            }
        }
        folderSelectContract.setHandleResultListener { granted, uri ->
            val path = uri?.path

            if (path != null) {
                saveSelectedPath(path)
            } else {
                showSnackbar(getString(R.string.sb_settings_unable_select_folder), success = false)
            }
        }
        folderSelectContract.setHandleErrorResultListener {

        }
    }

    private fun saveSelectedPath(selectedPath: String) {
        var path = selectedPath

        if (path.contains("/tree/home:")) {
            path = path.replace("/tree/home:", "/tree/primary:Documents/")
        }

        if (path.contains("/tree/downloads")) {
            path = path.replace("/tree/downloads", "/tree/primary:Downloads/")
        }

        if (path.contains("/tree/primary")) {
            try {
                val backupPath = BackupPath.findFullPath(path)
                AppPreference.setBackupPath(requireContext(), backupPath)
            } catch (exception: Exception) {
                // TODO
                showSnackbar(WarningSnackbar.MSG_UNKNOWN_ERROR, success = false)
            }
        } else {
            showSnackbar(getString(R.string.sb_settings_you_can_local_storage), success = false)
        }
    }

    private fun setupPreferencesListeners() {
        // Appearance
        customization?.setDebouncePreferenceClickListener {
            val action = RootSettingsFragmentDirections.actionOpenAppearanceSettingsFragment()
            findNavController().navigate(action)
        }
        typeFont?.setDebouncePreferenceClickListener {
            val action = RootSettingsFragmentDirections.actionOpenFontFragment()
            findNavController().navigate(action)
        }
        sorting?.setDebouncePreferenceClickListener {
            SortWaySheetDialogFragment().show(childFragmentManager)
        }
        darkModeSwitch?.setPreferenceChangeListener { value ->
            ColorManager.setDarkTheme(value)
        } // Data
        backup?.setDebouncePreferenceClickListener {
            val action = RootSettingsFragmentDirections.actionOpenBackupFragment()
            findNavController().navigate(action)
        }
        folder?.setDebouncePreferenceClickListener {
            selectedPreference = "folderKey"
            if (storageContract.storageAccess(requireContext())) {
                folderSelectContract.launchFolderSelect()
            } else {
                storageContract.launchAccessRequest()
            }
        }
        autoBackup?.setPreferenceChangeListener {
            selectedPreference = "autoBackupKey"
            if (storageContract.storageAccess(requireContext()).not()) storageContract.launchAccessRequest()
        }
        numberBackups?.setDebouncePreferenceClickListener {
            NumberBackupsSheetDialog(requireContext()).show()
        }
        // Notes
        hyperlinks?.setDebouncePreferenceClickListener {
            LinksSheetDialog(requireContext()).show()
        }
        fontSize?.setDebouncePreferenceClickListener {
            NoteFontSizeSheetDialog(requireContext()).show()
        }
        // Other
        retention?.setDebouncePreferenceClickListener {
            RetentionTimeSheetDialog(requireContext()).show()
        }
        communication?.setDebouncePreferenceClickListener {
            IntentManager.launchEmailClient(requireContext())
        }
        aboutApp?.setDebouncePreferenceClickListener {
            AboutAppSheetDialog(requireContext()).show()
        }
    }

    /*    override fun onChangeNumberBackups(number: Int) {
            numberBackups?.summary = number.toString()
        }

        override fun onChangeRetention(range: Int) {
            retention?.summary = requireContext().getString(R.string.number_of_days_summary, range)
        }*/
}