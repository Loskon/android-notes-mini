package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.google.android.material.slider.Slider;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

public class PrefNumOfLines extends Preference {

    private static callbackNumOfLines callbackNumOfLines;

    // for Callback
    public PrefNumOfLines(Context context) {
        super(context);
    }

    public void registerCallbackNumOfLines(callbackNumOfLines callbackNumOfLines){
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

        Slider sliderNumOfLines = (Slider) holder.findViewById(R.id.slider_number_of_lines);

        MyColor.setColorSlider(getContext(), sliderNumOfLines);

        (new MyDialogColor()).registerCallBackColorSettingsApp(color ->
                MyColor.setColorSlider(getContext(), sliderNumOfLines));

        int numOfLines = MySharedPreference.loadInt(getContext(),
                MyPrefKey.KEY_NUM_OF_LINES, 3);

        sliderNumOfLines.setValue(numOfLines);

        sliderNumOfLines.addOnChangeListener((slider1, value, fromUser) -> {
            MySharedPreference.saveInt(getContext(),
                    MyPrefKey.KEY_NUM_OF_LINES, (int) value);
            callbackNumOfLines.callingBackNumOfLines((int) value);
        });
    }

    public interface callbackNumOfLines {
        void callingBackNumOfLines(int numOfLines);
    }

}