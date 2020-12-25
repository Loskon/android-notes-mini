package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.ui.Helper.ColorHelper;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;

public class MyPreferenceCategory extends PreferenceCategory {

    public MyPreferenceCategory(Context context) {
        super(context);
    }
    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyPreferenceCategory(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView titleView = (TextView) holder.findViewById(android.R.id.title);
        titleView.setTextColor(ColorHelper.getColorCustom(getContext()));
    }
}
