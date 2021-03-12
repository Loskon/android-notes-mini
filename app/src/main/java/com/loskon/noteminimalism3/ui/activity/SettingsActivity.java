package com.loskon.noteminimalism3.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.ui.fragments.MySettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings, new MySettingsFragment())
                    .commit();
        }

        btmAppBarSettings = findViewById(R.id.btmAppBarSettings);
        btmAppBarSettings.setNavigationOnClickListener(v -> MyIntent.goMainActivity(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyColor.setNavIconColor(this, btmAppBarSettings);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goMainActivity(this);
    }
}