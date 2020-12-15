package com.loskon.noteminimalism3.ui.preference;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.activity.BackupActivity;
import com.loskon.noteminimalism3.ui.activity.SettingsAppearanceActivity;
import com.loskon.noteminimalism3.ui.mainHelper.SharedPrefHelper;
import com.obsez.android.lib.filechooser.ChooserDialog;
import com.obsez.android.lib.filechooser.tool.DirAdapter;


import java.io.File;

// Класс для отрисовки

public class CustomPreferencesFragment extends PreferenceFragmentCompat {

    private static final int READ_REQUEST_CODE = 297;
    private LinearLayoutManager layoutManager;
    private int index, top;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        Preference myPref =  findPreference("dark_theme");
        assert myPref != null;
        myPref.setOnPreferenceChangeListener((preference, newValue) -> {
            (new Handler()).postDelayed(() -> {
                if ((Boolean) newValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }, 260);
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

        Preference myPref2 =  findPreference("appearance");
        assert myPref2 != null;
        myPref2.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(requireActivity(), SettingsAppearanceActivity.class);
            (new Handler()).postDelayed(() -> {
                startActivity(intent);
            }, 50);
            return true;
        });

        Preference myPref4 =  findPreference(getString(R.string.backup_and_restore));
        assert myPref4 != null;
        myPref4.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(requireActivity(), BackupActivity.class);
            (new Handler()).postDelayed(() -> {
                startActivity(intent);
            }, 50);

            return true;
        });

        Preference myPref5 =  findPreference(getString(R.string.folder_for_backup));
        assert myPref5 != null;
        myPref5.setOnPreferenceClickListener(preference -> {
            new ChooserDialog(requireActivity())
                    .withFilter(true, false)
                    .withStartFile("Note Mini")
                    .customizePathView((pathView) -> {
                        pathView.setGravity(Gravity.CENTER);
                    })
                    .withAdapterSetter(new ChooserDialog.AdapterSetter() {
                        @Override
                        public void apply(DirAdapter adapter) {
                            adapter.overrideGetView((file, isSelected, isFocused, convertView, parent, inflater) -> {
                                ViewGroup view = (ViewGroup) inflater.inflate(R.layout.li_row, parent, false);
                                TextView txtTitleFiles = view.findViewById(R.id.textView);
                                txtTitleFiles.setText(file.getName());
                                return view;
                            });
                        }
                    })
                   // .withResources(R.string.app_name, R.string.action_import, R.string.dialog_cancel)
                    .withChosenListener(new ChooserDialog.Result() {
                        @Override
                        public void onChoosePath(String path, File pathFile) {
                            Toast.makeText(requireContext(), "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build()
                    .show();
            return true;
        });

        Preference myPref3 =  findPreference("color_picker_key");
        assert myPref3 != null;
        //myPref3.setEnabled(false);
        myPref3.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            startActivityForResult(Intent.createChooser(intent, "Choose directory"), READ_REQUEST_CODE);
            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode == READ_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                Toast.makeText(requireContext(), "" +
                        FindPathHelper.findFullPath(resultData.getData().getPath()), Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Убираем разделитель
        setDivider(new ColorDrawable(Color.TRANSPARENT));
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
    }

    private void setScrollPosition() {
        index = SharedPrefHelper.loadInt(requireContext(), "index", 0);
        top = SharedPrefHelper.loadInt(requireContext(), "top", 0);

        layoutManager.scrollToPositionWithOffset(index, top);
    }

    private void getScrollPosition() {
        index = layoutManager.findFirstVisibleItemPosition();
        View v = layoutManager.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - layoutManager.getPaddingTop());

        SharedPrefHelper.saveInt(requireContext(), "index", index);
        SharedPrefHelper.saveInt(requireContext(), "top", top);
    }
}
