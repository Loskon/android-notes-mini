package com.loskon.noteminimalism3.preference.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;

import static android.content.Context.MODE_PRIVATE;

public class PrefItemNumberOfLines extends Preference {

    private SharedPreferences sharedPref;
    private int numberOfLines;

    public PrefItemNumberOfLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefItemNumberOfLines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_number_of_lines);
        //setWidgetLayoutResource(R.layout.preference_theme);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        holder.itemView.setClickable(false); // disable parent click

        Slider slider = (Slider) holder.findViewById(R.id.slider_number_of_lines);

        loadAppearanceSettings();
        slider.setValue(numberOfLines);

        slider.addOnChangeListener((slider1, value, fromUser) -> {
            numberOfLines = (int) value;
            saveNumberOfLines();
        });
    }

    private void loadAppearanceSettings() {
        // Загружаем настройки
        sharedPref = getContext().getSharedPreferences("saveNumberOfLines", MODE_PRIVATE);
        numberOfLines = sharedPref.getInt("numberOfLines", 3);
    }

    private void saveNumberOfLines() {
        // Сохрнаяем настройки
        sharedPref = getContext().getSharedPreferences("saveNumberOfLines", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("numberOfLines", numberOfLines);
        edit.apply();
    }
}