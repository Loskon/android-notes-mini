package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.GetSizeItem;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

import info.hoang8f.android.segmented.SegmentedGroup;


public class MySwitchPref extends SwitchPreference {

    public MySwitchPref(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySwitchPref(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.custom_switch);
    }

    @Override
    public String getKey() {
        return super.getKey();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(true);
        //holder.itemView.setFocusable(true);

        TextView titleTextView = (TextView) holder.findViewById(android.R.id.title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.itemView.setMinimumHeight(GetSizeItem.getHeightItem(getContext()));

        SegmentedGroup segmentedGroup = (SegmentedGroup) holder.findViewById(R.id.segmented_group);
        segmentedGroup.setTintColor(MyColor.getColorCustom(getContext()));

        for (int i = 0; i < segmentedGroup.getChildCount(); i++) {
            segmentedGroup.getChildAt(i).setClickable(false);
        }

        boolean toggleButton = MySharedPref
                .getBoolean(getContext(), getKey(), false);

        if (toggleButton) {
            segmentedGroup.check(R.id.btn_on);
        } else {
            segmentedGroup.check(R.id.btn_off);
        }
    }
}
