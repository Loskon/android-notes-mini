package com.loskon.noteminimalism3.ui.activity.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.BackupPath;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.MySnackbar;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogNumOfBackup;

import java.util.Objects;

import static com.loskon.noteminimalism3.db.backup.BackupPermissions.PERMISSIONS_STORAGE;
import static com.loskon.noteminimalism3.helper.MainHelper.REQUEST_CODE_PERMISSIONS;

/**
 *
 */

public class MySettingsFragment extends PreferenceFragmentCompat {

    private static final int READ_REQUEST_CODE = 297;
    private Activity activity;
    private Fragment fragment;
    private LinearLayoutManager layoutManager;
    private int index, top, prefId;

    private SwitchPreference myPref6;
    private Preference myPref5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savePositionRecyclerView(0, 0);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        activity = requireActivity();

        Preference myPref =  findPreference(getString(R.string.dark_mode_title));
        assert myPref != null;
        myPref.setOnPreferenceChangeListener((preference, newValue) -> {
            (new Handler()).postDelayed(() -> {
                if ((Boolean) newValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }, 260);
            //MySharedPreference.saveBoolean(requireContext(),
                    //getString(R.string.dark_mode_title), (Boolean) newValue );
            return true;
        });

        //myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
          //  public boolean onPreferenceClick(Preference preference) {
               // Toast toast = Toast.makeText(getContext(),
                       // "Клик", Toast.LENGTH_SHORT);
                //toast.show();
               // return true;
           // }
       //});



        myPref6 = findPreference(MyPrefKey.KEY_AUTO_BACKUP);
        assert myPref6 != null;
        myPref6.setOnPreferenceChangeListener((preference, newValue) -> {
            prefId = 0;
            check(activity, fragment);
            return true;
        });

        //myPref6.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            //public boolean onPreferenceClick(Preference preference) {
                //Toast toast = Toast.makeText(getContext(),
                 //       "Клик", Toast.LENGTH_SHORT);
                //toast.show();
               // return true;
           // }
        //});

        Preference myPref2 =  findPreference("appearance");
        assert myPref2 != null;
        myPref2.setOnPreferenceClickListener(preference -> {
            MyIntent.intentSettingsApp(requireContext());
            return true;
        });


        String string = getString(R.string.num_backup_summary);

        String findKey = getString(R.string.pref_key_num_of_backup);
        Preference myPref8 =  findPreference(findKey);
        assert myPref8 != null;
        (new MyDialogNumOfBackup(activity)).registerCallBackColorNavIcon(numOfBackup -> {
            myPref8.setSummary(string + " \u2014 " +numOfBackup);
        });

        int numOfBackup = GetSharedPref.getNumOfBackup(activity);

        myPref8.setSummary(string +" \u2014 " +numOfBackup);

        myPref8.setOnPreferenceClickListener(preference -> {
           (new MyDialogNumOfBackup(activity)).callDialogNumOfBackup(findKey, numOfBackup);
            return true;
        });


        Preference myPref4 =  findPreference(getString(R.string.backup_and_restore));
        assert myPref4 != null;
        myPref4.setOnPreferenceClickListener(preference -> {
            MyIntent.intentBackupActivity(requireContext());
            return true;
        });

        myPref5 =  findPreference(getString(R.string.folder_for_backup));
        assert myPref5 != null;
        myPref5.setSummary(BackupPath.getPathForSummary(requireContext()));
        myPref5.setOnPreferenceClickListener(preference -> {
            prefId = 1;
            if (checkStoragePermissionsFragment()) {
                Intent intent = MyIntent.goFindFolder();
                startActivityForResult(Intent.createChooser(intent,
                        "Choose directory"), READ_REQUEST_CODE);
            }
            return true;
        });

        Preference myPref3 =  findPreference("color_picker_key");
        assert myPref3 != null;
        myPref3.setEnabled(false);
        myPref3.setOnPreferenceClickListener(preference -> {
            return true;
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                MySharedPref.setString(activity,
                        MyPrefKey.KEY_SEL_DIRECTORY, BackupPath
                                .findFullPath(resultData.getData().getPath()));
                myPref5.setSummary(BackupPath.getPathForSummary(requireContext()));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (prefId == 1) {
                    Intent intent = MyIntent.goFindFolder();
                    startActivityForResult(Intent.createChooser(intent,
                            "Choose directory"), READ_REQUEST_CODE);
                }
            } else {
                if (prefId == 0) {
                    myPref6.setChecked(false);
                }
                MySnackbar.makeSnackbar(activity, activity.
                        findViewById(R.id.cstSetting), getString(R.string.no_permissions),
                        activity.findViewById(R.id.btmAppBarSettings), false);
            }
        }
    }

    private boolean checkStoragePermissionsFragment() {

        int writePermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
            requestPermissions(
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE_PERMISSIONS
            );
            return false;
        } else {
            return true;
        }
    }

    private static boolean check(Activity activity, Fragment fragment) {
        int writePermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
            fragment.requestPermissions(
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE_PERMISSIONS
            );
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Для изменения цвета
        if (getListView() != null) {
            Objects.requireNonNull(getListView().getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDivider(new ColorDrawable(getResources().getColor(R.color.color_divider_light)));
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
                savePositionRecyclerView(index, top);
            }
        });
    }

    private void setScrollPosition() {
        index = GetSharedPref.getIndex(requireContext());
        top = GetSharedPref.getTop(requireContext());

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
    }

    private void savePositionRecyclerView(int index, int top) {
        MySharedPref.setInt(requireContext(),
                MyPrefKey.KEY_POSITION_INDEX, index);
        MySharedPref.setInt(requireContext(),
                MyPrefKey.KEY_POSITION_TOP, top);
    }
}