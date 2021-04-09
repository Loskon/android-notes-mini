package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.fragments.MySettingsAppFragment;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

/**
 * Кастомный элемент настроек для изменения шрифта текста в карточках
 */

public class MyPrefCardView extends Preference implements MySettingsAppFragment.CallbackReset {

    private View view;
    private TextView txtFontSize, txtDateFontSize, titleCategory;
    private int fontSize;
    private int dateFontSize;
    private Slider sliderFontSize;

    private static CallbackFontSize callbackFontSize;

    public static void regCallbackFontSize(CallbackFontSize callbackFontSize){
        MyPrefCardView.callbackFontSize = callbackFontSize;
    }

    public MyPrefCardView(Context context) {
        super(context);
    }

    public MyPrefCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPrefCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_item_card_view);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // Отключаем родительский клик

        initView(holder);
        loadAppearanceSettings();
        initialiseWidgets();
        sliderHandler();
    }

    private void initView(PreferenceViewHolder holder) {
        view = holder.findViewById(R.id.viewFavForCard);
        txtFontSize = (TextView) holder.findViewById(R.id.txt_title_card_note);
        txtDateFontSize = (TextView) holder.findViewById(R.id.txt_date_card_note);
        titleCategory = (TextView) holder.findViewById(R.id.tv_category_title);
        sliderFontSize = (Slider) holder.findViewById(R.id.slider_font_size_card_note);
        MySettingsAppFragment.registerCallBackReset(this);
    }

    private void loadAppearanceSettings() {
        fontSize = GetSharedPref.getFontSize(getContext());
    }

    private void initialiseWidgets() {
        txtFontSize.setText(getContext().getString(R.string.title_card_view));
        txtDateFontSize.setText(getContext().getString(R.string.date_card_view));

        sliderFontSize.setValue(fontSize);
        txtFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        txtDateFontSize.setTextSize(TypedValue
                .COMPLEX_UNIT_SP, PrefHelper.getDateFontSize(fontSize));

        int color = MyColor.getMyColor(getContext());
        changeColor(color);

        MyDialogColor.regCallBackSettingsApp(this::changeColor);
    }

    private void changeColor(int color) {
        view.setBackgroundTintList(ColorStateList.valueOf(color));
        titleCategory.setTextColor(color);
        view.setBackgroundTintList(ColorStateList.valueOf(color));
        MyColor.setColorSlider(getContext(), sliderFontSize);
    }

    private void sliderHandler() {
        sliderFontSize.addOnChangeListener((slider, value, fromUser) -> {
            fontSize = (int) value;
            dateFontSize = PrefHelper.getDateFontSize(fontSize);
            txtFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
            txtDateFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateFontSize);
            saveAppearanceSettings(fontSize, dateFontSize);
            goCallbackInMain();
        });
    }

    private void saveAppearanceSettings(int fontSize, int dateFontSize) {
        MySharedPref
                .setInt(getContext(), MyPrefKey.KEY_TITLE_FONT_SIZE, fontSize);
        MySharedPref
                .setInt(getContext(), MyPrefKey.KEY_DATE_FONT_SIZE, dateFontSize);
    }

    @Override
    public void callBackReset() {
        fontSize = 18;
        dateFontSize = PrefHelper.getDateFontSize(fontSize);
        saveAppearanceSettings(fontSize, dateFontSize);
        initialiseWidgets();
        goCallbackInMain();
    }

    private void goCallbackInMain() {
        if (callbackFontSize != null) callbackFontSize.callBack(fontSize, dateFontSize);
    }

    public interface CallbackFontSize {
        void callBack(int fontSize, int dateFontSize);
    }
}