package com.loskon.noteminimalism3.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
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
import com.loskon.noteminimalism3.backup.second.BackupPath;
import com.loskon.noteminimalism3.helper.permissions.PermissionsStorage;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogFontSize;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogPrefLinks;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogSlider;
import com.loskon.noteminimalism3.ui.preference.PrefHelper;

import java.util.Objects;

import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_PERMISSIONS;
import static com.loskon.noteminimalism3.helper.RequestCode.REQUEST_CODE_READ;

/**
 *
 */

public class MySettingsFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {


    private Activity activity;
    private Fragment fragment;
    private LinearLayoutManager layoutManager;
    private int index, top, numOfBackup, rangeInDays;
    private String prefKey;

    @SuppressWarnings("FieldCanBeLocal")
    private Preference intentFolderPref, customizeAppPref;
    private Preference numOfBackupPref, intentBackupPref, communicationPref;
    private Preference callDialogHyperlinksPref, retentionPref, fontSizePref;
    @SuppressWarnings("FieldCanBeLocal")
    private SwitchPreference darkModeSwitchPref, autoBackupSwitchPref, updateDatePref;

    private String darkModeString, autoBackupString, customizeAppString;
    private String numOfBackupStr, intentBackupString, intentFolderString, communicationStr;
    private String callDialogHyperlinksStr, retentionStr, fontSizeStr, updateDateStr;

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
                .registerCallBackColorNavIcon(this::setSummaryPreferences);

        if (!PermissionsStorage.verifyStoragePermissions(activity, fragment, false)) {
            autoBackupSwitchPref.setChecked(false);
        }

    }

    private void initialiseSettings() {
        activity = requireActivity();
        fragment = getParentFragmentManager().findFragmentById(R.id.fragment_settings);
    }

    private void getPrefKeys() {
        customizeAppString = getString(R.string.custom_app_title);
        darkModeString = getString(R.string.dark_mode_title);
        autoBackupString = getString(R.string.auto_backup);
        numOfBackupStr = getString(R.string.num_of_backup);
        intentBackupString = getString(R.string.backup_and_restore);
        intentFolderString = getString(R.string.folder_for_backup);
        callDialogHyperlinksStr = getString(R.string.hyperlinks_title);
        retentionStr = getString(R.string.retention_trash_title);
        fontSizeStr = getString(R.string.font_size_notes_title);
        //updateDateStr = getString(R.string.update_date_title);
        communicationStr = getString(R.string.communication);
    }

    private void setPreferences() {
        customizeAppPref = findPreference(customizeAppString);
        darkModeSwitchPref = findPreference(darkModeString);
        autoBackupSwitchPref = findPreference(autoBackupString);
        numOfBackupPref = findPreference(numOfBackupStr);
        intentBackupPref = findPreference(intentBackupString);
        intentFolderPref = findPreference(intentFolderString);
        callDialogHyperlinksPref = findPreference(callDialogHyperlinksStr);
        retentionPref = findPreference(retentionStr);
        fontSizePref = findPreference(fontSizeStr);
        //updateDatePref = findPreference(updateDateStr);
        communicationPref = findPreference(communicationStr);
    }

    private void setClickPreferences() {
        // ClickListener
        customizeAppPref.setOnPreferenceClickListener(this);
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
        setSummaryNum(numOfBackupPref, numOfBackupStr, numOfBackup);
        setSummaryNum(retentionPref, retentionStr, rangeInDays);
        setSummaryFolder();
    }

    private void loadSharedPref() {
        numOfBackup = GetSharedPref.getNumOfBackup(activity);
        rangeInDays = GetSharedPref.getRangeInDays(activity);
    }

    private void setSummaryNum(Preference pref, String prefString, int prefValue) {
        pref.setSummary(PrefHelper.getPrefSummary(activity, prefString, prefValue));
    }

    private void setSummaryFolder() {
        intentFolderPref.setSummary(BackupPath.getSummary(activity));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        prefKey = key;

        if (key.equals(darkModeString)) {
            int color = MyColor.getColorCustom(activity);

            boolean isDarkModeOn = MyColor.isDarkMode(activity);

            if ((isDarkModeOn && color == -16777216) || (!isDarkModeOn && color == -1)) {
                color = Color.GRAY;
                MySharedPref.setInt(activity, MyPrefKey.KEY_COLOR, color);
            }

            MyColor.setDarkTheme((Boolean) newValue);
            return true;
        } else if (key.equals(autoBackupString)) {
            PermissionsStorage.verifyStoragePermissions(activity, fragment, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        prefKey = key;

        if (key.equals(customizeAppString)) {
            MyIntent.intentSettingsApp(activity);
            return true;
        } else if (key.equals(intentBackupString)) {
            MyIntent.intentBackupActivity(activity);
            return true;
        } else if (key.equals(intentFolderString)) {
            goFindFolder();
            return true;
        } else if (key.equals(numOfBackupStr)) {
            loadSharedPref();
            (new MyDialogSlider(activity)).callDialog(numOfBackupStr, numOfBackup);
            return true;
        } else if (key.equals(callDialogHyperlinksStr)) {
            (new MyDialogPrefLinks(activity)).callDialog();
            return true;
        } else if (key.equals(retentionStr)) {
            loadSharedPref();
            (new MyDialogSlider(activity)).callDialog(retentionStr, rangeInDays);
            return true;
        } else if (key.equals(fontSizeStr)) {
            loadSharedPref();
            (new MyDialogFontSize(activity)).call();
            return true;
        } else if (key.equals(communicationStr)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"andreyrochev23@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback));
            startActivity(intent);
            return true;
        }

        return false;
    }

    private void goFindFolder() {
        if (PermissionsStorage.verifyStoragePermissions(activity, fragment, true)) {
            MyIntent.goFindFolder(fragment);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == REQUEST_CODE_READ && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                String backupPath = BackupPath.findFullPath(resultData.getData().getPath());
                MySharedPref.setString(activity, MyPrefKey.KEY_SEL_DIRECTORY, backupPath);
                setSummaryFolder();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (prefKey.equals(intentFolderString)) {
                    MyIntent.goFindFolder(fragment);
                }

            } else {

                if (prefKey.equals(autoBackupString)) {
                    autoBackupSwitchPref.setChecked(false);
                }

                showSnackbar();
            }
        }
    }

    private void showSnackbar() {
        SnackbarBuilder.makeSnackbar(activity, activity.
                        findViewById(R.id.cstSetting), getString(R.string.no_permissions),
                                activity.findViewById(R.id.btmAppBarSettings), false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Для изменения цвета
        if (getListView() != null) {
            Objects.requireNonNull(getListView().getAdapter()).notifyDataSetChanged();
        }
    }

    private void savePositionRecyclerView(int index, int top) {
        MySharedPref.setInt(activity, MyPrefKey.KEY_POSITION_INDEX, index);
        MySharedPref.setInt(activity, MyPrefKey.KEY_POSITION_TOP, top);
    }
}
