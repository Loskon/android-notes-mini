package com.loskon.noteminimalism3.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import static android.content.Context.MODE_PRIVATE;

// Класс для отрисовки

public class CustomPreferencesFragment extends PreferenceFragmentCompat {


    private RecyclerView recyclerView;
    private LinearLayoutManager myLayoutManager;
    private int index, top;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

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
        setDivider(new ColorDrawable(Color.TRANSPARENT));
        setDividerHeight(0);
        recyclerView = getListView();
        myLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollToItem();
                SharedPrefHelper.saveInt(requireContext(), "index", index);
                SharedPrefHelper.saveInt(requireContext(), "top", top);
                //savePositionSettings(requireContext(),index, top);
            }
        });

        index = SharedPrefHelper.loadInt(requireContext(), "index", 0);
        top = SharedPrefHelper.loadInt(requireContext(), "top", 0);
        //loadPositionSettings();
        myLayoutManager.scrollToPositionWithOffset(index, top);
    }

    private void scrollToItem() {
        index = myLayoutManager.findFirstVisibleItemPosition();
        View v = myLayoutManager.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - myLayoutManager.getPaddingTop());
    }

    public void savePositionSettings (Context context, int index, int top) {
        SharedPreferences sharedPref = context.getSharedPreferences("savePositionSettings", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("index", index);
        edit.putInt("top", top);
        edit.apply();
    }

    private void loadPositionSettings() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("savePositionSettings", MODE_PRIVATE);
        index = sharedPref.getInt("index", 0);
        top = sharedPref.getInt("top", 0);
    }
}
