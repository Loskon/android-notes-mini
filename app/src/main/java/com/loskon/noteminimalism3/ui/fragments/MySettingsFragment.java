package com.loskon.noteminimalism3.ui.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsStorage;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogFontSize;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogPrefLinks;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogSlider;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogSort;
import com.loskon.noteminimalism3.ui.preference.PrefHelper;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_PERMISSIONS;
import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_READ;

/**
 * Общие настройки
 */

public class MySettingsFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    private Activity activity;
    private Fragment fragment;
    private LinearLayoutManager layoutManager;
    private int index, top, numOfBackup, rangeInDays;
    private String prefKey;
    private boolean isDialogShow;

    @SuppressWarnings("FieldCanBeLocal")
    private Preference intentFolderPref, sortPref, customizeAppPref, intentFontsPref;
    private Preference numOfBackupPref, intentBackupPref, communicationPref;
    private Preference callDialogHyperlinksPref, retentionPref, fontSizePref;
    @SuppressWarnings("FieldCanBeLocal")
    private SwitchPreference darkModeSwitchPref, autoBackupSwitchPref;

    private String darkModeString, autoBackupString, customizeAppString, sortString;
    private String numOfBackupStr, intentBackupString, intentFolderString, communicationStr;
    private String callDialogHyperlinksStr, retentionStr, fontSizeStr, intentFontsString;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            savePositionRecyclerView(0, 0);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDivider(null);
        setDividerHeight(0);

        recyclerViewHandler();
        setScrollPosition();
    }

    private void recyclerViewHandler() {
        RecyclerView recyclerView = getListView();
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                getScrollPosition();
            }
        });

        recyclerView.setVerticalScrollBarEnabled(false);
    }

    private void setScrollPosition() {
        index = GetSharedPref.getIndexSettings(activity);
        top = GetSharedPref.getTopSettings(activity);

        layoutManager.scrollToPositionWithOffset(index, top);
    }

    private void getScrollPosition() {
        index = layoutManager.findFirstVisibleItemPosition();
        View view = layoutManager.getChildAt(0);

        if (view == null) {
            top = 0;
        } else {
            top = (view.getTop() - layoutManager.getPaddingTop());
        }

        savePositionRecyclerView(index, top);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        initialiseSettings();
        getPrefKeys();
        setPreferences();
        setClickPreferences();
        setChangePreferences();
        setSummaryPreferences();

        (new MyDialogSlider(activity))
                .regCallBackNavIcon(this::setSummaryPreferences);

        if (!PermissionsStorage.verify(activity, fragment, false)) {
            autoBackupSwitchPref.setChecked(false);
        }
    }

    private void initialiseSettings() {
        activity = requireActivity();
        fragment = getParentFragmentManager().findFragmentById(R.id.fragment_settings);
    }

    private void getPrefKeys() {
        customizeAppString = getString(R.string.custom_app_title);
        intentFontsString= getString(R.string.type_font_title);
        sortString = getString(R.string.sort_title);
        darkModeString = getString(R.string.dark_mode_title);
        autoBackupString = getString(R.string.auto_backup_title);
        numOfBackupStr = getString(R.string.num_of_backup_title);
        intentBackupString = getString(R.string.backup_title);
        intentFolderString = getString(R.string.folder_title);
        callDialogHyperlinksStr = getString(R.string.hyperlinks_title);
        retentionStr = getString(R.string.retention_trash_title);
        fontSizeStr = getString(R.string.font_size_notes_title);
        communicationStr = getString(R.string.communication_title);
    }

    private void setPreferences() {
        customizeAppPref = findPreference(customizeAppString);
        intentFontsPref = findPreference(intentFontsString);
        sortPref = findPreference(sortString);
        darkModeSwitchPref = findPreference(darkModeString);
        autoBackupSwitchPref = findPreference(autoBackupString);
        numOfBackupPref = findPreference(numOfBackupStr);
        intentBackupPref = findPreference(intentBackupString);
        intentFolderPref = findPreference(intentFolderString);
        callDialogHyperlinksPref = findPreference(callDialogHyperlinksStr);
        retentionPref = findPreference(retentionStr);
        fontSizePref = findPreference(fontSizeStr);
        communicationPref = findPreference(communicationStr);
    }

    private void setClickPreferences() {
        // ClickListener
        customizeAppPref.setOnPreferenceClickListener(this);
        intentFontsPref.setOnPreferenceClickListener(this);
        sortPref.setOnPreferenceClickListener(this);
        numOfBackupPref.setOnPreferenceClickListener(this);
        intentBackupPref.setOnPreferenceClickListener(this);
        intentFolderPref.setOnPreferenceClickListener(this);
        callDialogHyperlinksPref.setOnPreferenceClickListener(this);
        retentionPref.setOnPreferenceClickListener(this);
        fontSizePref.setOnPreferenceClickListener(this);
        communicationPref.setOnPreferenceClickListener(this);
    }

    private void setChangePreferences() {
        // ChangeListener
        darkModeSwitchPref.setOnPreferenceChangeListener(this);
        autoBackupSwitchPref.setOnPreferenceChangeListener(this);
    }

    private void setSummaryPreferences() {
        loadSharedPref();
        retentionPref.setSummary(PrefHelper.getSummaryRange(activity, rangeInDays));
        numOfBackupPref.setSummary(String.valueOf(numOfBackup));
        intentFolderPref.setSummary(BackupPath.getSummaryPath(activity));
    }

    private void loadSharedPref() {
        numOfBackup = GetSharedPref.getNumOfBackup(activity);
        rangeInDays = GetSharedPref.getRangeInDays(activity);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        prefKey = key;

        if (key.equals(darkModeString)) {
            MyColor.setDarkTheme((Boolean) newValue);
            return true;
        } else if (key.equals(autoBackupString)) {
            PermissionsStorage.verify(activity, fragment, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        prefKey = key;

        if (key.equals(customizeAppString)) {
            MyIntent.openSettingsApp(activity);
            return true;
        } else if (key.equals(intentFontsString)) {
            MyIntent.openFonts(activity);
            return true;
        } else if (key.equals(sortString)) {
            (new MyDialogSort(activity)).call();
            return true;
        } else if (key.equals(intentBackupString)) {
            MyIntent.openBackupActivity(activity);
            return true;
        } else if (key.equals(intentFolderString)) {
            goFindFolder();
            return true;
        } else if (key.equals(numOfBackupStr)) {
            loadSharedPref();
            (new MyDialogSlider(activity)).call(numOfBackupStr, numOfBackup);
            return true;
        } else if (key.equals(callDialogHyperlinksStr)) {
            (new MyDialogPrefLinks(activity)).call();
            return true;
        } else if (key.equals(retentionStr)) {
            loadSharedPref();
            (new MyDialogSlider(activity)).call(retentionStr, rangeInDays);
            return true;
        } else if (key.equals(fontSizeStr)) {
            loadSharedPref();
            (new MyDialogFontSize(activity)).call();
            return true;
        } else if (key.equals(communicationStr)) {
            goMailClient();
            return true;
        }

        return false;
    }

    private void goFindFolder() {
        if (PermissionsStorage.verify(activity, fragment, true)) {
            MyIntent.startFindFolder(fragment);
        }
    }

    private void goMailClient() {
        try {
            MyIntent.startMailClient(activity);
        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
            showSnackbar(getString(R.string.email_client_not_found));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == REQUEST_CODE_READ && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                saveNewPath(resultData);
            }
        }
    }

    private void saveNewPath(Intent resultData) {
        String backupPath = BackupPath.findFullPath(resultData.getData().getPath());
        MySharedPref.setString(activity, MyPrefKey.KEY_SEL_DIRECTORY, backupPath);
        setSummaryPreferences();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (prefKey.equals(intentFolderString)) MyIntent.startFindFolder(fragment);

            } else {

                if (prefKey.equals(autoBackupString)) autoBackupSwitchPref.setChecked(false);
                showSnackbar(getString(R.string.no_permissions));
            }
        }
    }

    private void showSnackbar(String message) {
        SnackbarBuilder.makeSnackbar(activity, activity.
                        findViewById(R.id.cstLytSettings), message,
                                activity.findViewById(R.id.btmAppBarSettings), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Для изменения цвета
        if (getListView() != null) {
            getListView().getAdapter().notifyDataSetChanged();
        }
    }

    private void savePositionRecyclerView(int index, int top) {
        MySharedPref.setInt(activity, MyPrefKey.KEY_POSITION_INDEX, index);
        MySharedPref.setInt(activity, MyPrefKey.KEY_POSITION_TOP, top);
    }
}