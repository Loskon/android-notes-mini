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
import com.loskon.noteminimalism3.ui.Helper.ColorHelper;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;

public class SettingsAppearanceActivity extends AppCompatActivity {

    private static CallbackColor34 callbackColor34;

    public void registerCallBack34(CallbackColor34 callbackColor34){
        SettingsAppearanceActivity.callbackColor34 = callbackColor34;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_appearance);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        BottomAppBar btmAppBarSettingsAppearance = findViewById(R.id.btmAppBarSettings2);
        btmAppBarSettingsAppearance.setNavigationOnClickListener(v -> goMainActivity());

        ColorHelper.setNavigationIconColor(this, btmAppBarSettingsAppearance);
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

    public interface CallbackColor34{
        void callingBackColor34(boolean isOneSizeOn);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_appearance, rootKey);

            //PrefItemFontSize toggle = (PrefItemFontSize) findPreference("2");

            Preference myPref = findPreference(getString(R.string.one_size_cards));
            assert myPref != null;
            myPref.setOnPreferenceChangeListener((preference, newValue) -> {
                SharedPrefHelper.saveBoolean(requireContext(),
                        SharedPrefHelper.KEY_ONE_SIZE, (Boolean) newValue);
                callbackColor34.callingBackColor34((Boolean) newValue);
                SharedPrefHelper.saveBoolean(requireContext(),
                        getString(R.string.one_size_cards), (Boolean) newValue );
                return true;
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
