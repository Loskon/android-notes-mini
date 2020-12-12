package com.loskon.noteminimalism3.preference;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.activity.mainHelper.SharedPrefHelper;
import com.loskon.noteminimalism3.db.backup.LocalBackup;

import java.io.File;

// Класс для отрисовки

public class CustomPreferencesFragment extends PreferenceFragmentCompat {


    private LinearLayoutManager layoutManager;
    private int index, top;
    private LocalBackup localBackup;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        localBackup = new LocalBackup(requireActivity());

        Preference myPref =  findPreference("dark_theme");
        assert myPref != null;
        //myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
          //  public boolean onPreferenceClick(Preference preference) {
               // Toast toast = Toast.makeText(getContext(),
                       // "Клик", Toast.LENGTH_SHORT);
                //toast.show();
               // return true;
           // }
       //});

        //Preference myPref2 =  findPreference("font_size_key");
        //assert myPref2 != null;
       // myPref2.setOnPreferenceClickListener(preference -> {
          //  CustomAlertDialogSize.alertDialogShowSize(getContext());
           // return true;
      //  });

       // myPref2.setOnPreferenceChangeListener((preference, newValue) -> {
          //  Toast toast = Toast.makeText(getContext(),
             //       "Вы ввели в дилоге: "+ newValue.toString(), Toast.LENGTH_SHORT);
          //  toast.show();
         //   return true;
    //    });


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



        Preference myPref4 =  findPreference("backup");
        assert myPref4 != null;
        myPref4.setOnPreferenceClickListener(preference -> {
            String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
            //localBackup.performBackup(outFileName);
            localBackup.performBackup(outFileName);
            return true;
        });

        Preference myPref5 =  findPreference("restore");
        assert myPref5 != null;
        myPref5.setOnPreferenceClickListener(preference -> {
            //localBackup.performRestore();
            localBackup.performRestore();
            return true;
        });



        Preference myPref3 =  findPreference("color_picker_key");
        assert myPref3 != null;
        //myPref3.setEnabled(false);
        myPref3.setOnPreferenceClickListener(preference -> {

            return true;
        });
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
