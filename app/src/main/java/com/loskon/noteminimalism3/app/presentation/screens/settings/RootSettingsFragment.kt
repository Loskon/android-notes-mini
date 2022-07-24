package com.loskon.noteminimalism3.app.presentation.screens.settings

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortPreferenceChangeListener
import com.loskon.noteminimalism3.app.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.managers.ColorManager
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.requests.activity.REQUEST_CODE_FOLDER_FOR_BACKUP
import com.loskon.noteminimalism3.requests.activity.ResultActivity
import com.loskon.noteminimalism3.requests.activity.ResultActivityInterface
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.requests.storage.ResultStorageAccess
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.sheetdialogs.AboutAppSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.LinksSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.NoteFontSizeSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.NumberBackupsSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.RetentionTimeSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.SortWaySheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar

class RootSettingsFragment : BasePreferenceFragment(),
    NumberBackupsSheetDialog.NumberBackupsCallback,
    RetentionTimeSheetDialog.RetentionTimeCallback,
    ResultAccessStorageInterface,
    ResultActivityInterface {

    private lateinit var storageAccess: ResultStorageAccess
    private lateinit var resultActivity: ResultActivity

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        configureRequestPermissions(context)
    }

    private fun configureRequestPermissions(context: Context) {
        storageAccess = ResultStorageAccess(context, this, this).also { it.installingContracts() }
        resultActivity = ResultActivity(context, this, this).also { it.installingContracts() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installCallbacks()
    }

    private fun installCallbacks() {
        NumberBackupsSheetDialog.registerNumberBackupsCallback(this)
        RetentionTimeSheetDialog.registerRetentionTimeCallback(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreferences()
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
            SortWaySheetDialog(requireContext()).show()
        }
        darkModeSwitch?.setShortPreferenceChangeListener { newValue: Boolean ->
            ColorManager.setDarkTheme(newValue)
        }
        // Data
        backup?.setDebouncePreferenceClickListener {
            val action = RootSettingsFragmentDirections.actionOpenBackupFragment()
            findNavController().navigate(action)
        }
        folder?.setDebouncePreferenceClickListener {
            selectedPreference = "folderKey"
            if (storageAccess.hasAccessStorageRequest()) resultActivity.launchFolderSelect()
        }
        autoBackup?.setShortPreferenceChangeListener {
            selectedPreference = "autoBackupKey"
            storageAccess.hasAccessStorageRequest()
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

    override fun onResume() {
        super.onResume()
        installSummaryPreferences()
        otherConfigurations()
    }

    private fun installSummaryPreferences() {
        // Data
        folder?.summary = BackupPath.getSummary(requireContext())

        val number: Int = AppPreference.getNumberBackups(requireContext())
        numberBackups?.summary = number.toString()

        // Other
        val range: Int = AppPreference.getRetentionRange(requireContext())
        retention?.summary = requireContext().getString(R.string.number_of_days_summary, range)
    }

    private fun otherConfigurations() {
        if (!storageAccess.hasAccessStorage()) {
            autoBackup?.isChecked = false
            autoBackupNotifySwitch?.isChecked = false
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            folder?.isEnabled = false
        }
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            if (selectedPreference == "folderKey") {
                resultActivity.launchFolderSelect()
            }
        } else {
            if (selectedPreference == "autoBackupKey") {
                autoBackup?.isChecked = false
            }

            showSnackbar(getString(R.string.no_permissions), false)
        }
    }

    override fun onRequestActivityResult(isGranted: Boolean, requestCode: Int, data: Uri?) {
        if (isGranted) {
            if (requestCode == REQUEST_CODE_FOLDER_FOR_BACKUP) {
                val path: String? = data?.path

                if (data != null && path != null) {
                    savingSelectedPath(path)
                } else {
                    showSnackbar(WarningSnackbar.MSG_UNABLE_SELECT_FOLDER, false)
                }
            }
        }
    }

    private fun savingSelectedPath(noNullPath: String) {
        var path: String = noNullPath

        if (path.contains("/tree/home:")) {
            path = path.replace("/tree/home:", "/tree/primary:Documents/")
        }

        if (path.contains("/tree/downloads")) {
            path = path.replace("/tree/downloads", "/tree/primary:Downloads/")
        }

        if (path.contains("/tree/primary")) {
            try {
                val backupPath: String = BackupPath.findFullPath(path)
                AppPreference.setBackupPath(requireContext(), backupPath)
            } catch (exception: Exception) {
                showSnackbar(WarningSnackbar.MSG_UNKNOWN_ERROR, false)
            }
        } else {
            showSnackbar(WarningSnackbar.MSG_LOCAL_STORAGE, false)
        }
    }

    override fun onChangeNumberBackups(number: Int) {
        numberBackups?.summary = number.toString()
    }

    override fun onChangeRetention(range: Int) {
        retention?.summary = requireContext().getString(R.string.number_of_days_summary, range)
    }

    override fun onDetach() {
        removeCallbacks()
        super.onDetach()
    }

    private fun removeCallbacks() {
        NumberBackupsSheetDialog.registerNumberBackupsCallback(null)
        RetentionTimeSheetDialog.registerRetentionTimeCallback(null)
    }

    companion object {
        fun newInstance(): RootSettingsFragment = RootSettingsFragment()
    }
}