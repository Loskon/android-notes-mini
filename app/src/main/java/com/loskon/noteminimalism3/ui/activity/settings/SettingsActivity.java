package com.loskon.noteminimalism3.ui.activity.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;

public class SettingsActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Меняем цвет статус бара
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