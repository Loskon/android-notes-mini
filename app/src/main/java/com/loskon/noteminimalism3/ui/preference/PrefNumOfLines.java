package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

public class PrefNumOfLines extends Preference {

    private static CallbackNumOfLines callbackNumOfLines;

    // for Callback
    public PrefNumOfLines(Context context) {
        super(context);
    }

    public void registerCallbackNumOfLines(CallbackNumOfLines callbackNumOfLines){
        PrefNumOfLines.callbackNumOfLines = callbackNumOfLines;
    }

    public PrefNumOfLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefNumOfLines(Context context, AttributeSet attrs, int defStyleAttr) {
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
        String key = getContext().getString(R.string.number_of_lines);

        Slider sliderNumOfLines = (Slider) holder.findViewById(R.id.slider_number_of_lines);

        MyColor.setColorSlider(getContext(), sliderNumOfLines);

        (new MyDialogColor()).registerCallBackColorSettingsApp(color ->
                MyColor.setColorSlider(getContext(), sliderNumOfLines));

        int numOfLines = GetSharedPref.getNumOfLines(getContext());

        sliderNumOfLines.setValue(numOfLines);

        sliderNumOfLines.addOnChangeListener((slider1, value, fromUser) -> {
            MySharedPref.setInt(getContext(), key, (int) value);
            callbackNumOfLines.callingBackNumOfLines((int) value);
        });
    }

    public interface CallbackNumOfLines {
        void callingBackNumOfLines(int numOfLines);
    }

}