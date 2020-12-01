package com.loskon.noteminimalism3.preference.item;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 */

public class PrefItemCard extends Preference {

    private TextView txtFontSize, txtDateFontSize;
    private CardView cardViewSettings;
    private SharedPreferences sharedPref;
    private int fontSize, cornerCard, dateFontSize, cornerCard_dp;
    private Slider srFontSize, srDateFontSize, srCardCorner;
    private Button btnResetCardView;

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
        txtFontSize = (TextView) holder.findViewById(R.id.txt_card_in_settings);
        txtDateFontSize = (TextView) holder.findViewById(R.id.txt_date_card_in_settings);
        cardViewSettings = (CardView) holder.findViewById(R.id.card_view_settings);
        srFontSize = (Slider) holder.findViewById(R.id.slider_font_size);
        srDateFontSize = (Slider) holder.findViewById(R.id.slider_date_font_size);
        srCardCorner = (Slider) holder.findViewById(R.id.slider_corner_card);
        btnResetCardView = (Button) holder.findViewById(R.id.btn_reset_card_view);
    }

    private void loadAppearanceSettings() {
        // Загружаем настройки
        sharedPref = getContext().getSharedPreferences("saveCardAppearance", MODE_PRIVATE);
        fontSize = sharedPref.getInt("fontSize", 18);
        dateFontSize = sharedPref.getInt("dateFontSize", 14);
        cornerCard = sharedPref.getInt("cornerCard", 6);
    }

    private void initInstallationSets() {
        // Устанавлием размеры шрифтов и закругление углов
        srFontSize.setValue(fontSize);
        txtFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);

        srDateFontSize.setValue(dateFontSize);
        txtDateFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);

        convertingToDp();

        srCardCorner.setValue(cornerCard);
        cardViewSettings.setRadius(cornerCard_dp);
    }

    private void convertingToDp(){
         // Преобразовние int значений слайдера в dp
         cornerCard_dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                cornerCard, getContext().getResources().getDisplayMetrics());
    }

    private void sliderHandlers() {
        // Установка минимального значения 12
        Slider.OnChangeListener changeListenerSlider = (slider, value, fromUser) -> {
            int id = slider.getId();

            if (id == R.id.slider_font_size) {
                fontSize = (int) value;
                txtFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            } else if (id == R.id.slider_date_font_size) {
                dateFontSize = (int) value;  // Установка минимального значения 12
                txtDateFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);
            } else if (id == R.id.slider_corner_card) {
                cornerCard = (int) value;
                convertingToDp();
                cardViewSettings.setRadius(cornerCard_dp);
            }

            saveAppearanceSettings(false);
        };

        srFontSize.addOnChangeListener(changeListenerSlider);
        srDateFontSize.addOnChangeListener(changeListenerSlider);
        srCardCorner.addOnChangeListener(changeListenerSlider);

        btnResetCardView.setOnClickListener(view -> resetClick());
    }

    private void resetClick() {
        saveAppearanceSettings(true);
        loadAppearanceSettings();
        initInstallationSets();
    }

    private void saveAppearanceSettings(Boolean isCleanUp) {
        sharedPref = getContext().getSharedPreferences("saveCardAppearance", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();

        if (isCleanUp) {
            edit.clear();
        } else {
            edit.putInt("fontSize", fontSize);
            edit.putInt("dateFontSize", dateFontSize);
            edit.putInt("cornerCard", cornerCard);
        }

        edit.apply();
    }


}