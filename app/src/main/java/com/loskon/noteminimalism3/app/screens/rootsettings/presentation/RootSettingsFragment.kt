package com.loskon.noteminimalism3.app.screens.rootsettings.presentation

import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs.AboutAppSheetDialogFragment
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs.ActiveLinksSheetDialogFragment
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs.BackupsCountSheetDialogFragment
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs.NoteFontSizeSheetDialogFragment
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs.RetentionTimeSheetDialogFragment
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs.SortWaySheetDialogFragment
import com.loskon.noteminimalism3.base.contracts.FolderSelectContract
import com.loskon.noteminimalism3.base.contracts.StorageContract
import com.loskon.noteminimalism3.base.extension.dialogfragment.show
import com.loskon.noteminimalism3.base.extension.fragment.setFragmentResultListener
import com.loskon.noteminimalism3.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.base.extension.view.setPreferenceChangeListener
import com.loskon.noteminimalism3.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.managers.ColorManager
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.FilePathUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootSettingsFragment : BasePreferenceFragment() {

    private val viewModel: RootSettingsViewModel by viewModel()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(BACKUPS_COUNT_REQUEST_KEY) { bundle -> changeBackupsCountSummary(bundle) }
        setFragmentResultListener(RETENTION_TIME_REQUEST_KEY) { bundle -> changeRetentionTimeSummary(bundle) }
    }

    private fun changeBackupsCountSummary(bundle: Bundle) {
        val count = bundle.getInt(BACKUPS_COUNT_REQUEST_KEY)
        val backupPath = AppPreference.getBackupPath(requireContext())

        viewModel.deleteExtraFiles(backupPath, count)
        numberBackups?.summary = count.toString()
    }

    private fun changeRetentionTimeSummary(bundle: Bundle) {
        val days = bundle.getInt(RETENTION_TIME_REQUEST_KEY)
        retention?.summary = getString(R.string.number_of_days_summary, days)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreferences()
        setPreferencesSummary()
        disablePreferences()
        setupContractsListeners()
        setupPreferencesListeners()
    }

    private fun findPreferences() {
        // Appearance
        customization = findPreference(getString(R.string.custom_app_title))
        typeFont = findPreference(getString(R.string.type_font_title))
        sorting = findPreference(getString(R.string.sort_title))
        darkModeSwitch = findPreference(getString(R.string.dark_mode_key))
        // Data
        backup = findPreference(getString(R.string.backup_title))
        folder = findPreference(getString(R.string.folder_title))
        autoBackup = findPreference(getString(R.string.auto_backup_key))
        autoBackupNotifySwitch = findPreference(getString(R.string.notification_key))
        numberBackups = findPreference(getString(R.string.number_of_backup_key))
        // Notes
        hyperlinks = findPreference(getString(R.string.hyperlinks_title))
        fontSize = findPreference(getString(R.string.font_size_notes_title))
        // Other
        retention = findPreference(getString(R.string.retention_trash_key))
        communication = findPreference(getString(R.string.communication_title))
        aboutApp = findPreference(getString(R.string.about_app_title))
    }

    private fun setPreferencesSummary() {
        val backupPath = AppPreference.getBackupPath(requireContext())
        folder?.summary = FilePathUtil.getBackupPathSummary(backupPath)

        val number = AppPreference.getBackupsCount(requireContext())
        numberBackups?.summary = number.toString()

        val days = AppPreference.getRetentionDays(requireContext())
        retention?.summary = getString(R.string.number_of_days_summary, days)
    }

    private fun disablePreferences() {
        if (storageContract.storageAccess(requireContext()).not()) {
            autoBackup?.isChecked = false
            autoBackupNotifySwitch?.isChecked = false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            folder?.isEnabled = false
        }
    }

    private fun setupContractsListeners() {
        storageContract.setHandleGrantedListener { granted ->
            if (granted) {
                folderSelectContract.launchFolderSelect()
            } else {
                showSnackbar(R.string.no_permissions, success = false)
            }
        }
        folderSelectContract.setHandleResultListener { granted, uri ->
            if (granted) {
                val path = uri?.path

                if (path != null) {
                    checkAndSaveSelectedPath(path)
                } else {
                    showSnackbar(R.string.sb_settings_unable_select_folder, success = false)
                }
            }
        }
    }

    private fun checkAndSaveSelectedPath(selectedPath: String) {
        val path = FilePathUtil.getCorrectedTreePath(selectedPath)

        if (path != null) {
            val backupPath = FilePathUtil.findFullPath(path)

            if (backupPath != null) {
                AppPreference.setBackupPath(requireContext(), backupPath)
            } else { // TODO
                showSnackbar(R.string.unknown_error, success = false)
            }
        } else {
            showSnackbar(R.string.sb_settings_you_can_local_storage, success = false)
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
            SortWaySheetDialogFragment().show(parentFragmentManager)
        }
        darkModeSwitch?.setPreferenceChangeListener { value ->
            ColorManager.setDarkTheme(value)
        }
        // Data
        backup?.setDebouncePreferenceClickListener {
            val action = RootSettingsFragmentDirections.actionOpenBackupFragment()
            findNavController().navigate(action)
        }
        folder?.setDebouncePreferenceClickListener {
            if (storageContract.storageAccess(requireContext())) {
                folderSelectContract.launchFolderSelect()
            } else {
                storageContract.launchAccessRequest()
            }
        }
        numberBackups?.setDebouncePreferenceClickListener {
            BackupsCountSheetDialogFragment().show(parentFragmentManager)
        }
        // Notes
        hyperlinks?.setDebouncePreferenceClickListener {
            ActiveLinksSheetDialogFragment().show(parentFragmentManager)
        }
        fontSize?.setDebouncePreferenceClickListener {
            NoteFontSizeSheetDialogFragment().show(parentFragmentManager)
        }
        // Other
        retention?.setDebouncePreferenceClickListener {
            RetentionTimeSheetDialogFragment().show(parentFragmentManager)
        }
        communication?.setDebouncePreferenceClickListener {
            IntentManager.launchEmailClient(requireContext())
        }
        aboutApp?.setDebouncePreferenceClickListener {
            AboutAppSheetDialogFragment().show(parentFragmentManager)
        }
    }

    companion object {
        const val BACKUPS_COUNT_REQUEST_KEY = "BACKUPS_COUNT_REQUEST_KEY"
        const val RETENTION_TIME_REQUEST_KEY = "RETENTION_TIME_REQUEST_KEY"
    }
}