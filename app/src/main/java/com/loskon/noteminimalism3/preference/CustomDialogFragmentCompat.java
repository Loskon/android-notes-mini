package com.loskon.noteminimalism3.preference;

import android.os.Bundle;
import android.view.View;

import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.flask.colorpicker.ColorPickerView;
import com.loskon.noteminimalism3.R;

// Для диалога!

public class CustomDialogFragmentCompat extends PreferenceDialogFragmentCompat {

    private ColorPickerView colorPickerView;

    public static CustomDialogFragmentCompat newInstance(String key) {
        final CustomDialogFragmentCompat
                fragment = new CustomDialogFragmentCompat();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    // обновите представление значениями по вашему выбору
    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        colorPickerView = view.findViewById(R.id.color_picker_view);

        if (colorPickerView == null) {
            throw new IllegalStateException("Dialog view must contain a ColorPickerView " +
                    "with id 'color_picker_view'");
        }

        String color = null;
        DialogPreference preference = getPreference();
        if (preference instanceof CustomPreference) {
            color = ((CustomPreference) preference).getColor();
        }

        // Set the color to the ColorPickerView
        if (color != null) {
                colorPickerView.setColor(CustomPreference.getColor(color), false);
        }
    }


    // если положительный результат истинен, сохраните значение (я) из вашего представления в SharedPreferences.
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Получаем текущие значения из TimePicker
            String color =  String.valueOf(colorPickerView.getSelectedColor());
            //String color = "#" + Integer.toHexString(colorPickerView.getColor()).substring(2);
            // Generate value to save
            DialogPreference preference = getPreference();
            if (preference instanceof CustomPreference) {
                CustomPreference customPreference = ((CustomPreference) preference);
                if (customPreference.callChangeListener(color)) {
                    customPreference.setColor(color);
                }
            }
        }
    }
}
