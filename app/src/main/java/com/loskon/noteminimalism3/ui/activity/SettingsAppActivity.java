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
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPrefKeys;
import com.loskon.noteminimalism3.ui.activity.settings.SettingsActivity;

public class SettingsAppActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarSettingsAppearance;
    private static CallbackColor34 callbackColor34;

    public void registerCallBack34(CallbackColor34 callbackColor34){
        SettingsAppActivity.callbackColor34 = callbackColor34;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_appearance);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsAppFragment())
                    .commit();
        }

        btmAppBarSettingsAppearance = findViewById(R.id.btmAppBarSettings2);
        btmAppBarSettingsAppearance.setNavigationOnClickListener(
                v -> MyIntent.goSettingsActivity(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyColor.setNavigationIconColor(this, btmAppBarSettingsAppearance);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }

    public interface CallbackColor34{
        void callingBackColor34(boolean isOneSizeOn);
    }

    public static class SettingsAppFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_appearance, rootKey);

            Preference myPref = findPreference(MySharedPrefKeys.KEY_ONE_SIZE);
            assert myPref != null;
            myPref.setOnPreferenceChangeListener((preference, newValue) -> {
                //MySharedPreference.saveBoolean(requireContext(),
                       // MySharedPrefKeys.KEY_ONE_SIZE, (Boolean) newValue);
                callbackColor34.callingBackColor34((Boolean) newValue);
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
