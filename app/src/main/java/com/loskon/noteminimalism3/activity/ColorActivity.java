package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.textfield.TextInputEditText;
import com.loskon.noteminimalism3.R;

import top.defaults.colorpicker.ColorPickerView;

public class ColorActivity extends AppCompatActivity {

    private BottomAppBar btmAppBarColor;
    private ColorPickerView colorPickerView;
    private TextInputEditText textInputEditText;
    private ImageView imageViewColor;
    private ImageButton imageButtonColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        initView();

        btmAppBarColor.setNavigationOnClickListener(v -> goMainActivity());

        SharedPreferences sharedPref = getApplication().getSharedPreferences("saveColor", MODE_PRIVATE);
        colorPickerView.setInitialColor(sharedPref.getInt("saveColor", -16711872));

        colorPickerView.subscribe((color, fromUser, shouldPropagate) -> {
            textInputEditText.clearFocus();
            hideSoftKeyboard(this);
            colorChangeInPicker(color);
            saveColor(color);
        });

        imageButtonColor.setOnClickListener(v -> colorChangeInEditText());
    }

    private void colorChangeInPicker(int color){
        String text = Integer.toHexString(color);
        // Удаляем параметры прозрачности
        textInputEditText.setText(text.substring(2));
        imageViewColor.setColorFilter(color);
        changeColorNavigationIcon(color);
    }

    private void colorChangeInEditText () {
        String fromEditText = textInputEditText.getText().toString();
        // Добавляем параметры прозрачности
        int colorHex = (int) Long.parseLong("ff" + fromEditText, 16);
        colorPickerView.setInitialColor(colorHex);
        imageViewColor.setColorFilter(colorHex);
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity == null) return;
        if (activity.getCurrentFocus() == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void changeColorNavigationIcon (int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            btmAppBarColor.getNavigationIcon().setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
        } else {
            btmAppBarColor.getNavigationIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void initView () {
        btmAppBarColor = findViewById(R.id.btmAppBarColor);
        colorPickerView = findViewById(R.id.colorPicker);
        textInputEditText = findViewById(R.id.text_input_color);
        imageViewColor = findViewById(R.id.imageViewColor);
        imageButtonColor = findViewById(R.id.imageButtonColor);
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

    private void saveColor (int color) {
        SharedPreferences sharedPref = getApplication().getSharedPreferences("saveColor", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("color", color);
        edit.apply();
    }
}