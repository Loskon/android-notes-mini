package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.loskon.noteminimalism3.R;

import top.defaults.colorpicker.ColorPickerView;

public class ColorActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarColor;
    private ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        btmAppBarColor = findViewById(R.id.btmAppBarColor);
        btmAppBarColor.setNavigationOnClickListener(v -> goMainActivity());

        colorPickerView = findViewById(R.id.colorPicker);
        colorPickerView.setInitialColor(-16711872);
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