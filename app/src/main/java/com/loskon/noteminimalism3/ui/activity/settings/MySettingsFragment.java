package com.loskon.noteminimalism3.ui.activity.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.BuildToast;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPrefKeys;
import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsAppActivity;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;
import com.loskon.noteminimalism3.db.backup.FindPathHelper;

import java.util.Objects;

import static com.loskon.noteminimalism3.helper.MainHelper.REQUEST_CODE_PERMISSIONS;
import static com.loskon.noteminimalism3.db.backup.Permissions.PERMISSIONS_STORAGE;

/**
 *
 */

public class MySettingsFragment extends PreferenceFragmentCompat {

    private static final int READ_REQUEST_CODE = 297;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int index, top, prefId;


    SwitchPreference myPref6;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

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



        myPref6 = findPreference(MySharedPrefKeys.KEY_AUTO_BACKUP);
        assert myPref6 != null;
        myPref6.setOnPreferenceChangeListener((preference, newValue) -> {
            prefId = 0;
            checkStoragePermissionsFragment();
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

        Preference myPref4 =  findPreference(getString(R.string.backup_and_restore));
        assert myPref4 != null;
        myPref4.setOnPreferenceClickListener(preference -> {
            MyIntent.intentBackupActivity(requireContext());
            return true;
        });

        Preference myPref5 =  findPreference(getString(R.string.folder_for_backup));
        assert myPref5 != null;
        myPref5.setOnPreferenceClickListener(preference -> {
            prefId = 1;
            if (checkStoragePermissionsFragment()) {
                MyIntent.goFindFolder(requireActivity());
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
                MySharedPreference.saveString(requireActivity(),
                        MySharedPrefKeys.KEY_SEL_DIRECTORY, FindPathHelper
                                .findFullPath(resultData.getData().getPath()));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (prefId == 1) {
                    MyIntent.goFindFolder(requireActivity());
                }
            } else {
                if (prefId == 0) {
                    myPref6.setChecked(false);
                }
                BuildToast.showToast(requireContext(), getString(R.string.no_permissions));
            }
        }
    }

    private boolean checkStoragePermissionsFragment() {

        int writePermission = ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        int granted = PackageManager.PERMISSION_GRANTED;

        if (writePermission != granted || readPermission != granted) {
            requestPermissions( //Method of Fragment
                    PERMISSIONS_STORAGE,
                    REQUEST_CODE_PERMISSIONS
            );
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Убираем разделитель
        setDivider(new ColorDrawable(getResources().getColor(R.color.color_divider)));
        setDividerHeight(30);

        recyclerViewHandler();
        setScrollPosition();
    }

    private void recyclerViewHandler() {
        recyclerView = getListView();
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                getScrollPosition();
            }
        });
    }

    private void setScrollPosition() {
        index = MySharedPreference.loadInt(requireContext(), "index", 0);
        top = MySharedPreference.loadInt(requireContext(), "top", 0);

        layoutManager.scrollToPositionWithOffset(index, top);
    }

    private void getScrollPosition() {
        index = layoutManager.findFirstVisibleItemPosition();
        View v = layoutManager.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - layoutManager.getPaddingTop());

        MySharedPreference.saveInt(requireContext(), "index", index);
        MySharedPreference.saveInt(requireContext(), "top", top);
    }
}
