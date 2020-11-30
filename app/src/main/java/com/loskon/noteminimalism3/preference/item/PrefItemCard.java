package com.loskon.noteminimalism3.preference.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;

import static android.content.Context.MODE_PRIVATE;

public class PrefItemCard extends Preference {

    private TextView txtMainFontSize, txtDateFontSize;
    private CardView cardViewSettings;
    private SharedPreferences sharedPref;
    private int fontSize, cornerCard, dateFontSize;
    private Slider sliderMainFontSize, sliderDateFontSize, sliderCardCorner;


    public PrefItemCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefItemCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_item_card);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // Отключаем родительский клик
        initView(holder);
        loadAppearanceSettings();
        initInstallationSets();
        sliderHandlers();
    }

    private void initView(PreferenceViewHolder holder) {
        txtMainFontSize = (TextView) holder.findViewById(R.id.txt_main_card_in_settings);
        txtDateFontSize = (TextView) holder.findViewById(R.id.txt_date_card_in_settings);
        cardViewSettings = (CardView) holder.findViewById(R.id.card_view_settings);
        sliderMainFontSize = (Slider) holder.findViewById(R.id.slider_main_font_size);
        sliderDateFontSize = (Slider) holder.findViewById(R.id.slider_date_font_size);
        sliderCardCorner = (Slider) holder.findViewById(R.id.slider_corner_card);
    }

    private void loadAppearanceSettings() {
        sharedPref = getContext().getSharedPreferences("saveFontSize", MODE_PRIVATE);
        fontSize = sharedPref.getInt("fontSize", 18);
        cornerCard = sharedPref.getInt("cornerCard", 6);
        dateFontSize = sharedPref.getInt("dateFontSize", 14);
    }

    private void initInstallationSets() {
        sliderMainFontSize.setValue(fontSize);
        txtMainFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        sliderDateFontSize.setValue(dateFontSize);
        txtDateFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);

        sliderCardCorner.setValue(cornerCard);
        cardViewSettings.setRadius(cornerCard);
    }

    private void sliderHandlers() {
        sliderMainFontSize.addOnChangeListener((slider, value, fromUser) -> {
            fontSize = (int) value;
            txtMainFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            saveAppearanceSettings();
        });

        sliderDateFontSize.addOnChangeListener((slider, value, fromUser) -> {
            dateFontSize = (int) value;  // Установка минимального значения 12
            txtDateFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);
            saveAppearanceSettings();
        });

        sliderCardCorner.addOnChangeListener((slider, value, fromUser) -> {
            cornerCard = (int) value;
            cardViewSettings.setRadius(cornerCard);
            saveAppearanceSettings();
        });
    }

    private void saveAppearanceSettings() {
        sharedPref = getContext().getSharedPreferences("saveFontSize", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putInt("fontSize", fontSize);
        edit.putInt("cornerCard", cornerCard);
        edit.putInt("dateFontSize", dateFontSize);
        edit.apply();
    }
}