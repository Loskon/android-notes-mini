package com.loskon.noteminimalism3.ui.activity.settingsapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

public class SettingsAppActivity extends AppCompatActivity implements MyDialogColor.CallbackColorNavIcon {

    private BottomAppBar btmAppBarSettingsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_app);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings_app, new MySettingsAppFragment())
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
        MyColor.setNavIconColor(this, btmAppBarSettingsApp);
    }
}
