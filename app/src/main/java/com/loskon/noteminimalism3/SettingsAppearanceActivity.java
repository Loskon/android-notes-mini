package com.loskon.noteminimalism3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.activity.SettingsActivity;
import com.loskon.noteminimalism3.preference.item.PrefItemFontSize;

public class SettingsAppearanceActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettings2;

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

        btmAppBarSettings2 = findViewById(R.id.btmAppBarSettings2);
        btmAppBarSettings2.setNavigationOnClickListener(v -> goMainActivity());
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_appearance, rootKey);

            PrefItemFontSize toggle = (PrefItemFontSize) findPreference("2");
           toggle.setExternalListener(() -> Toast.makeText(getContext(),
                    "You clicked the preference without changing its value", Toast.LENGTH_LONG).show());
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setDivider(new ColorDrawable(Color.TRANSPARENT));
            setDividerHeight(0);
        }

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
}
