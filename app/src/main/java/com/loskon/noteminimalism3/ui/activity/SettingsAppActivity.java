package com.loskon.noteminimalism3.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;
import com.loskon.noteminimalism3.ui.fragments.MySettingsAppFragment;

public class SettingsAppActivity
        extends AppCompatActivity implements MyDialogColor.CallbackNavIcon {

    private BottomAppBar btmAppBarSettingsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_app);
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings_app, new MySettingsAppFragment())
                    .commit();
        }

        btmAppBarSettingsApp = findViewById(R.id.btmAppBarSettingsApp);
        btmAppBarSettingsApp.setNavigationOnClickListener(
                v -> MyIntent.goSettingsActivity(this));

        MyDialogColor.regCallBackNavIcon(this);
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
