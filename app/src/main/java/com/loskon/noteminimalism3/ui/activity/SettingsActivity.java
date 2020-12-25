package com.loskon.noteminimalism3.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.Helper.ColorHelper;
import com.loskon.noteminimalism3.ui.preference.CustomPreferencesFragment;

public class SettingsActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings, new CustomPreferencesFragment())
                    .commit();
        }

        btmAppBarSettings = findViewById(R.id.btmAppBarSettings);
        btmAppBarSettings.setNavigationOnClickListener(v -> goMainActivity());

        ColorHelper.setNavigationIconColor(this, btmAppBarSettings);
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goMainActivity();
    }
}