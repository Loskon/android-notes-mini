package com.loskon.noteminimalism3.ui.preference.item;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;

public class PrefItemNumOfLines extends Preference {

    private static callbackNumOfLines callbackNumOfLines;

    // for Callback
    public PrefItemNumOfLines(Context context) {
        super(context);
    }

    public void registerCallbackNumOfLines(callbackNumOfLines callbackNumOfLines){
        PrefItemNumOfLines.callbackNumOfLines = callbackNumOfLines;
    }

    public PrefItemNumOfLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefItemNumOfLines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_number_of_lines);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        holder.itemView.setClickable(false); // disable parent click

        Slider sliderNumOfLines = (Slider) holder.findViewById(R.id.slider_number_of_lines);

        int numOfLines = SharedPrefHelper.loadInt(getContext(),
                SharedPrefHelper.KEY_NUM_OF_LINES, 3);

        sliderNumOfLines.setValue(numOfLines);

        sliderNumOfLines.addOnChangeListener((slider1, value, fromUser) -> {
            SharedPrefHelper.saveInt(getContext(),
                    SharedPrefHelper.KEY_NUM_OF_LINES, (int) value);
            callbackNumOfLines.callingBackNumOfLines((int) value);
        });
    }

    public interface callbackNumOfLines {
        void callingBackNumOfLines(int numOfLines);
    }

}