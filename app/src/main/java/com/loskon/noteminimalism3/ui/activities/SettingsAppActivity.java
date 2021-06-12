package com.loskon.noteminimalism3.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.ui.fragments.SettingsAppFragment;
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor;

/**
 * Класс для работы с настройками внешнего вида плиток
 */

public class SettingsAppActivity
        extends AppCompatActivity implements SheetPrefSelectColor.CallbackColorNavIcon {

    private BottomAppBar btmAppBarSettingsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        new AppFontManager(this).setFont();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_app);
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings_app, new SettingsAppFragment())
                    .commit();
        }

        btmAppBarSettingsApp = findViewById(R.id.btmAppBarSettingsApp);
        btmAppBarSettingsApp.setNavigationOnClickListener(
                v -> MyIntent.goSettingsActivity(this));

        SheetPrefSelectColor.regCallBackNavIcon2(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }

    @Override
    public void onCallBackNavIcon(int color) {
        MyColor.setNavIconColor(this, btmAppBarSettingsApp);
    }
}
