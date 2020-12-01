package com.loskon.noteminimalism3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.activity.SettingsActivity;
import com.loskon.noteminimalism3.preference.item.PrefItemFontSize;

public class SettingsAppearanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_appearance);
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
        this.startActivity((
                new Intent(this, SettingsActivity.class)));
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
                    saveOneSize((Boolean) isOneSizeOn);
                    return true;
                }
            });
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setDivider(new ColorDrawable(Color.TRANSPARENT));
            setDividerHeight(0);
        }

        private void saveOneSize(boolean isOneSizeOn) {
            SharedPreferences sharedPref = getActivity().
                    getSharedPreferences("saveOneSize", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isOneSizeOn", isOneSizeOn);
            editor.apply();
        }

    }


}
