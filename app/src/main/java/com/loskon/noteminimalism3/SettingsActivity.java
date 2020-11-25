package com.loskon.noteminimalism3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.activity.MainActivity;

public class SettingsActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings, new CustomPreferencesFragment())
                    .commit();
        }
        btmAppBarSettings = findViewById(R.id.btmAppBarSettings);
        btmAppBarSettings.setNavigationOnClickListener(v -> this.startActivity((
                new Intent(this, MainActivity.class))));
    }
}