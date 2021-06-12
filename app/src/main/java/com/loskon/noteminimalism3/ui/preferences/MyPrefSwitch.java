package com.loskon.noteminimalism3.ui.preferences;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MySizeItem;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

/**
 * Добавление и настройки кастомного переключателя
 */

public class MyPrefSwitch extends SwitchPreference {

    private final Context context;
    private SegmentedButtonGroup segmentedButtonGroup;
    private int color;

    public MyPrefSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPrefSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWidgetLayoutResource(R.layout.pref_widget_switch);
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(true);

        segmentedButtonGroup =
                (SegmentedButtonGroup) holder.findViewById(R.id.buttonGroup_lordOfTheRings);

        setMyColor();
        initSettings();
        initSwitchPosition();
    }

    private void initSettings() {
        int borderGroup = MySizeItem.getBorderWidghtSwitch(context);

        segmentedButtonGroup.setSelectedBackgroundColor(color);
        segmentedButtonGroup.setBorder(borderGroup, color, 0, 0);
        segmentedButtonGroup.setClickable(false);
    }

    private void setMyColor() {
        if (isEnabled()) {
            color = MyColor.getMyColor(context);
        } else {
            color = Color.GRAY;
        }
    }

    private void initSwitchPosition() {
        boolean toggleButton = MySharedPref.getBoolean(context, getKey(), false);

        if (toggleButton) {
            segmentedButtonGroup.setPosition(1, false);
        } else {
            segmentedButtonGroup.setPosition(0, false);
        }
    }
}
