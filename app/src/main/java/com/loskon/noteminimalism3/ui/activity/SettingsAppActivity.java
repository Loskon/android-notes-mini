package com.loskon.noteminimalism3.ui.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

import java.util.Objects;

public class SettingsAppActivity extends AppCompatActivity implements MyDialogColor.CallbackColorNavIcon {

    private BottomAppBar btmAppBarSettingsApp;
    private static CallbackOneSize callbackOneSize;
    private static CallbackReset callbackReset;

    public void registerCallBackOneSize(CallbackOneSize callbackOneSize){
        SettingsAppActivity.callbackOneSize = callbackOneSize;
    }

    public void registerCallBackReset(CallbackReset callbackReset){
        SettingsAppActivity.callbackReset = callbackReset;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_app);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsAppFragment())
                    .commit();
        }

        btmAppBarSettingsApp = findViewById(R.id.btmAppBarSettingsApp);
        btmAppBarSettingsApp.setNavigationOnClickListener(
                v -> MyIntent.goSettingsActivity(this));

        (new MyDialogColor()).registerCallBackColorNavIcon(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyIntent.goSettingsActivity(this);
    }

    @Override
    public void callingBackColorNavIcon(int color) {
        MyColor.setNavigationIconColor(this, btmAppBarSettingsApp);
    }

    public static class SettingsAppFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_appearance, rootKey);

            SwitchPreference myPref = findPreference(getString(R.string.pref_key_one_size));
            assert myPref != null;
            myPref.setOnPreferenceChangeListener((preference, newValue) -> {
                if (callbackOneSize != null) {
                    callbackOneSize.callingBackOneSize((Boolean) newValue);
                }
                return true;
            });

            Preference myPref2 = findPreference(getString(R.string.reset));
            assert myPref2 != null;
            myPref2.setOnPreferenceClickListener(preference -> {
                if (callbackReset != null) {
                    callbackReset.callingBackReset();
                }
                return false;
            });

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            (new MyDialogColor()).registerCallBackColorNotifyData(color -> {
                if (getListView() != null) {
                    Objects.requireNonNull(getListView().getAdapter()).notifyDataSetChanged();
                }
            });

            setDivider(new ColorDrawable(getResources().getColor(R.color.color_divider_light)));
            setDividerHeight(30);
            //view.setBackgroundColor(Color.BLUE);
        }
    }

    public interface CallbackOneSize {
        void callingBackOneSize(boolean isOneSizeOn);
    }

    public interface CallbackReset {
        void callingBackReset();
    }
}
