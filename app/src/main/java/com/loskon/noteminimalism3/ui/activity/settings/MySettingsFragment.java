package com.loskon.noteminimalism3.ui.activity.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
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
import com.loskon.noteminimalism3.backup.BackupPath;
import com.loskon.noteminimalism3.backup.BackupPermissions;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.helper.snackbars.MySnackbar;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogNumOfBackup;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogPrefLinks;
import com.loskon.noteminimalism3.ui.preference.PrefHelper;

import java.util.Objects;

import static com.loskon.noteminimalism3.backup.BackupPermissions.REQUEST_CODE_PERMISSIONS;
import static com.loskon.noteminimalism3.helper.MyIntent.READ_REQUEST_CODE;


/**
 *
 */

public class MySettingsFragment extends PreferenceFragmentCompat implements
        Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {


    private Activity activity;
    private Fragment fragment;
    private LinearLayoutManager layoutManager;
    private int index, top, numOfBackup;
    private String prefKey;

    @SuppressWarnings("FieldCanBeLocal")
    private Preference intentFolderPref, customizeAppPref;
    private Preference numOfBackupPref, intentBackupPref;
    private Preference callDialogHyperlinksPref;
    @SuppressWarnings("FieldCanBeLocal")
    private SwitchPreference darkModeSwitchPref, autoBackupSwitchPref;

    private String darkModeString, autoBackupString, customizeAppString;
    private String numOfBackupStr, intentBackupString, intentFolderString;
    private String callDialogHyperlinksStr;

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

        setDivider(new ColorDrawable(MyColor.getColorDivider(activity)));
        setDividerHeight(30);

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
        index = GetSharedPref.getIndex(activity);
        top = GetSharedPref.getTop(activity);

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

        (new MyDialogNumOfBackup(activity))
                .registerCallBackColorNavIcon(this::setSummaryNumOfBackup);

        if (!BackupPermissions.verifyStoragePermissions(activity, fragment, false)) {
            autoBackupSwitchPref.setChecked(false);
        }

    }

    private void initialiseSettings() {
        activity = requireActivity();
        fragment = getParentFragmentManager().findFragmentById(R.id.fragment_settings);
        numOfBackup = GetSharedPref.getNumOfBackup(activity);
    }

    private void getPrefKeys() {
        customizeAppString = getString(R.string.custom_app_title);
        darkModeString = getString(R.string.dark_mode_title);
        autoBackupString = getString(R.string.auto_backup);
        numOfBackupStr = getString(R.string.pref_key_num_of_backup);
        intentBackupString = getString(R.string.backup_and_restore);
        intentFolderString  = getString(R.string.folder_for_backup);
        callDialogHyperlinksStr  = getString(R.string.hyperlinks_title);
    }

    private void setPreferences() {
        customizeAppPref = findPreference(customizeAppString);
        darkModeSwitchPref = findPreference(darkModeString);
        autoBackupSwitchPref = findPreference(autoBackupString);
        numOfBackupPref = findPreference(numOfBackupStr);
        intentBackupPref = findPreference(intentBackupString);
        intentFolderPref =  findPreference(intentFolderString);
        callDialogHyperlinksPref =  findPreference(callDialogHyperlinksStr);
    }

    private void setClickPreferences() {
        // ClickListener
        customizeAppPref.setOnPreferenceClickListener(this);
        numOfBackupPref.setOnPreferenceClickListener(this);
        intentBackupPref.setOnPreferenceClickListener(this);
        intentFolderPref.setOnPreferenceClickListener(this);
        callDialogHyperlinksPref.setOnPreferenceClickListener(this);
    }

    private void setChangePreferences() {
        // ChangeListener
        darkModeSwitchPref.setOnPreferenceChangeListener(this);
        autoBackupSwitchPref.setOnPreferenceChangeListener(this);
    }

    private void setSummaryPreferences() {
        setSummaryNumOfBackup(numOfBackup);
        setSummaryFolder();
    }

    private void setSummaryNumOfBackup(int numOfBackup) {
        numOfBackupPref.setSummary(PrefHelper.getNum(activity, numOfBackup));
    }

    private void setSummaryFolder() {
        intentFolderPref.setSummary(BackupPath.getSummary(activity));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String key = preference.getKey();
        prefKey = key;

        if (key.equals(darkModeString)) {
            MyColor.setDarkTheme((Boolean) newValue);
            return true;
        } else if (key.equals(autoBackupString)) {
            BackupPermissions.verifyStoragePermissions(activity, fragment, true);
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
            numOfBackup = GetSharedPref.getNumOfBackup(activity);
            (new MyDialogNumOfBackup(activity)).callDialog(numOfBackupStr, numOfBackup);
            return true;
        } else if (key.equals(callDialogHyperlinksStr)) {
            (new MyDialogPrefLinks(activity)).callDialog();
            return true;
        }

        return false;
    }

    private void goFindFolder() {
        if (BackupPermissions.verifyStoragePermissions(activity, fragment, true)) {
            MyIntent.goFindFolder(fragment);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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
        MySnackbar.makeSnackbar(activity, activity.
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
