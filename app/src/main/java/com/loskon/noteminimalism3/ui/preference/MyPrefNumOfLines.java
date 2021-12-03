package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

/**
 * Кастомный элемент настроек со слайдером
 * для выбора числа строк в карточке
 */

public class MyPrefNumOfLines extends Preference {

    private static CallbackNumOfLines callbackNumOfLines;

    // for Callback
    public MyPrefNumOfLines(Context context) {
        super(context);
    }

    public static void regCallbackNumOfLines(CallbackNumOfLines callbackNumOfLines){
        MyPrefNumOfLines.callbackNumOfLines = callbackNumOfLines;
    }

    public MyPrefNumOfLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPrefNumOfLines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_item_num_of_lines);
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        holder.itemView.setClickable(false); // disable parent click
        String key = getContext().getString(R.string.num_of_lines_header);

        Slider sliderNumOfLines = (Slider) holder.findViewById(R.id.slider_number_of_lines);

        MyColor.setColorSlider(getContext(), sliderNumOfLines);

        MyDialogColor.regCallBackSettingsApp(color ->
                MyColor.setColorSlider(getContext(), sliderNumOfLines));

        int numOfLines = GetSharedPref.getNumOfLines(getContext());

        sliderNumOfLines.setValue(numOfLines);

        sliderNumOfLines.addOnChangeListener((slider1, value, fromUser) -> {
            MySharedPref.setInt(getContext(), key, (int) value);
            if (callbackNumOfLines != null) callbackNumOfLines.callBack((int) value);
        });
    }

    public interface CallbackNumOfLines {
        void callBack(int numOfLines);
    }

}