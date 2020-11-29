package com.loskon.noteminimalism3.preference.item;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;

public class PrefItemNumberOfLines extends Preference {

    public PrefItemNumberOfLines(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefItemNumberOfLines(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutResource(R.layout.pref_number_of_lines);
        //setWidgetLayoutResource(R.layout.preference_theme);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // disable parent click
        //View button = holder.findViewById(R.id.theme_dark);
        //button.setClickable(true); // enable custom view click

        // the rest of the click binding
    }
}