package com.loskon.noteminimalism3.ui.preference.item;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.ui.mainHelper.SharedPrefHelper;
import com.loskon.noteminimalism3.ui.preference.prefdialog.CustomAlertDialogColorPicker;

public class PrefItemSelectColor extends Preference {

    private int color;

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
        holder.itemView.setClickable(true); // Отключаем родительский клик
        TextView titleTextView = (TextView) holder.findViewById(android.R.id.title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        int heightItem_dp = (int) getContext().getResources().getDimension(R.dimen.height_item);
        holder.itemView.setMinimumHeight(heightItem_dp); // Устанавливаем высоту кастомного элемента

        holder.itemView.setOnClickListener(view -> {
           CustomAlertDialogColorPicker.alertDialogShowColorPicker(getContext());
           // DialogFontSize.alertDialogShowColorPicker2(getContext());
        });

        color = SharedPrefHelper.loadInt(getContext(),
                "color",-16739862);

        ImageView imageViewColorForSettings = (ImageView) holder.findViewById(R.id.imageViewColorForSettings);
        imageViewColorForSettings.setColorFilter(color);

        (new CustomAlertDialogColorPicker()).registerCallBack(imageViewColorForSettings::setColorFilter);
    }

}