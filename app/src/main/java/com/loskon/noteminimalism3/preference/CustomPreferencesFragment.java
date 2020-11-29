package com.loskon.noteminimalism3.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.preference.prefdialog.CustomAlertDialogColor;
import com.loskon.noteminimalism3.preference.prefdialog.CustomAlertDialogSize;
import com.loskon.noteminimalism3.R;

import static android.content.Context.MODE_PRIVATE;

// Класс для отрисовки

public class CustomPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        //Preference myPref =  findPreference("signature");
        //assert myPref != null;
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


        //myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
          //  @Override
           //public boolean onPreferenceChange(Preference preference, Object newValue) {
                   // Toast toast = Toast.makeText(getContext(),
                      //      "Вы ввели: "+ newValue.toString(), Toast.LENGTH_SHORT);
                   // toast.show();
              //  return true;
         //   }
    //    });



        Preference myPref3 =  findPreference("color_picker_key");
        assert myPref3 != null;
        myPref3.setOnPreferenceClickListener(preference -> {
            CustomAlertDialogColor.alertDialogShowColor(getContext());
            return true;
        });
    }

    RecyclerView recyclerView;
    int scrollPosition;
    LinearLayoutManager myLayoutManager;
    int index;
    int top;

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
                savePositionSettings(getContext(),index, top);
            }
        });
        loadPositionSettings();
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
        SharedPreferences sharedPref = getActivity().getSharedPreferences("savePositionSettings", MODE_PRIVATE);
        index = sharedPref.getInt("index", 0);
        top = sharedPref.getInt("top", 0);
    }
}
