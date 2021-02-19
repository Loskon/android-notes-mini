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
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.fragments.MySettingsAppFragment;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

/**
 *
 */

public class PrefCardView extends Preference implements MySettingsAppFragment.CallbackReset {

    private View view;
    private TextView txtFontSize, txtDateFontSize, titleMainText;
    private int fontSize;
    private int dateFontSize;
    private Slider sliderFontSize;

    private static CallbackFontSize callbackFontSize;

    public PrefCardView(Context context) {
        super(context);
    }

    public void registerCallBackFontSize(CallbackFontSize callbackFontSize){
        PrefCardView.callbackFontSize = callbackFontSize;
    }

    public PrefCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_item_card_view);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // Отключаем родительский клик

        initView(holder);
        loadAppearanceSettings();
        initInstallationSets();
        sliderHandler();
    }

    private void initView(PreferenceViewHolder holder) {
        view = holder.findViewById(R.id.view);
        txtFontSize = (TextView) holder.findViewById(R.id.title_item_note);
        txtDateFontSize = (TextView) holder.findViewById(R.id.date_item_note);
        titleMainText = (TextView) holder.findViewById(R.id.textView3);
        sliderFontSize = (Slider) holder.findViewById(R.id.slider_font_size);
        (new MySettingsAppFragment()).registerCallBackReset(this);
    }

    private void loadAppearanceSettings() {
        fontSize = GetSharedPref.getFontSize(getContext());
    }

    private void initInstallationSets() {
        txtFontSize.setText(getContext().getString(R.string.font_size_change));
        txtDateFontSize.setText(getContext().getString(R.string.date_cards));

        sliderFontSize.setValue(fontSize);
        txtFontSize.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        txtDateFontSize.setTextSize(TypedValue
                .COMPLEX_UNIT_SP, PrefHelper.getDateFontSize(fontSize));

        int color = MyColor.getColorCustom(getContext());

        changeColor(color);

        (new MyDialogColor()).registerCallBackColorSettingsApp(this::changeColor);
    }

    private void changeColor(int color) {
        view.setBackgroundTintList(ColorStateList.valueOf(color));
        titleMainText.setTextColor(color);
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
    public void callingBackReset() {
        fontSize = 18;
        dateFontSize = PrefHelper.getDateFontSize(fontSize);
        saveAppearanceSettings(fontSize, dateFontSize);
        //loadAppearanceSettings();
        initInstallationSets();
        goCallbackInMain();
    }

    private void goCallbackInMain() {
        if (callbackFontSize != null) {
            callbackFontSize.callingBackFontSize(fontSize, dateFontSize);
        }
    }

    public interface CallbackFontSize {
        void callingBackFontSize(int fontSize, int dateFontSize);
    }
}