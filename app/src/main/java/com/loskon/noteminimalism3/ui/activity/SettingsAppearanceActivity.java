package com.loskon.noteminimalism3.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.mainHelper.ColorHelper;
import com.loskon.noteminimalism3.ui.mainHelper.SharedPrefHelper;

public class SettingsAppearanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_appearance);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndNavView(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        BottomAppBar btmAppBarSettingsAppearance = findViewById(R.id.btmAppBarSettings2);
        btmAppBarSettingsAppearance.setNavigationOnClickListener(v -> goMainActivity());
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goMainActivity();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_appearance, rootKey);

            //PrefItemFontSize toggle = (PrefItemFontSize) findPreference("2");

            Preference myPref = findPreference("6");
            assert myPref != null;
            myPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object isOneSizeOn) {
                    SharedPrefHelper.saveBoolean(requireContext(),"isOneSizeOn", (Boolean) isOneSizeOn);
                    return true;
                }
            });
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setDivider(new ColorDrawable(Color.TRANSPARENT));
            setDividerHeight(0);
            //view.setBackgroundColor(Color.BLUE);
        }

    }


}
