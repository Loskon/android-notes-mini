package com.loskon.noteminimalism3.ui.preference.item;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.GetSizeItem;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

public class PrefSelectColor extends Preference {

    public PrefSelectColor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefSelectColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.pref_select_color);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(true); // родительский клик
        TextView titleTextView = (TextView) holder.findViewById(android.R.id.title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        holder.itemView.setMinimumHeight(GetSizeItem.getHeightItem(getContext()));

        holder.itemView.setOnClickListener(view -> {
           MyDialogColor.alertDialogShowColorPicker(getContext());
        });

        ImageView imageViewColorForSettings = (ImageView) holder.findViewById(R.id.imageViewColorForSettings);
        imageViewColorForSettings.setColorFilter(MyColor.getColorCustom(getContext()));

        (new MyDialogColor()).registerCallBack(imageViewColorForSettings::setColorFilter);
    }

}