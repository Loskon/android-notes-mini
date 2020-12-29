package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.helper.MyColor;

public class MyPrefCategory extends PreferenceCategory {

    public MyPrefCategory(Context context) {
        super(context);
    }
    public MyPrefCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyPrefCategory(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        titleView.setTextColor(MyColor.getColorCustom(getContext()));
    }
}
