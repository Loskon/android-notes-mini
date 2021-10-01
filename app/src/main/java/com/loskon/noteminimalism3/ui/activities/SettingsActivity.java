package com.loskon.noteminimalism3.ui.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.ui.fragments.SettingsFragment;

/**
 * Класс для работы с основными настройками
 */

public class SettingsActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        AppFontManager.setFont(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings, new SettingsFragment())
                    .commit();
        }

        btmAppBarSettings = findViewById(R.id.btmAppBarSettings);
        btmAppBarSettings.setNavigationOnClickListener(v -> onBackPressed());


    }

    public void fff() {
        Intent intent = new Intent(this, ListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyColor.setNavIconColor(this, btmAppBarSettings);
    }
}