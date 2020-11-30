package com.loskon.noteminimalism3.preference.item;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import com.loskon.noteminimalism3.R;

public class PrefItemSelectColor extends Preference {

    public PrefItemSelectColor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefItemSelectColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setLayoutResource(R.layout.pref_select_color);
        setWidgetLayoutResource(R.layout.pref_select_color);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(true); // disable parent click
        TextView titleTextView = (TextView) holder.findViewById(android.R.id.title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.itemView.setMinimumHeight(162);
        ImageView imageViewColorForSettings = (ImageView) holder.findViewById(R.id.imageViewColorForSettings);
    }
}