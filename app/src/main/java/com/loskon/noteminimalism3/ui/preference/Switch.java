package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.Helper.ColorHelper;
import com.loskon.noteminimalism3.ui.Helper.SharedPrefHelper;


public class Switch extends SwitchPreference {

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setLayoutResource(R.layout.pref_select_color);
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

        TextView titleTextView = (TextView) holder.findViewById(android.R.id.title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        int heightItem_dp = (int) getContext().getResources().getDimension(R.dimen.height_item);
        holder.itemView.setMinimumHeight(heightItem_dp);

        SwitchMaterial switchMaterial = (SwitchMaterial ) holder.findViewById(R.id.switch1);
        switchMaterial.setClickable(false);

        boolean toggleButton = SharedPrefHelper.loadBoolean(getContext(), getKey(), false);

        if (toggleButton) {
            switchMaterial.getThumbDrawable().setColorFilter(ColorHelper.getColorCustom(getContext()), PorterDuff.Mode.SRC_ATOP);
            switchMaterial.getTrackDrawable().setColorFilter(ColorHelper.getColorCustom(getContext()), PorterDuff.Mode.SRC_ATOP);
        } else {
            switchMaterial.getThumbDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            switchMaterial.getTrackDrawable().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }

        switchMaterial.setChecked(toggleButton);
    }
}
