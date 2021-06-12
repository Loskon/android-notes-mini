package com.loskon.noteminimalism3.ui.preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor;

/**
 * Добавление бокового виджета
 */

public class MyPrefSelectColor extends Preference {

    private final Context context;

    public MyPrefSelectColor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyPrefSelectColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWidgetLayoutResource(R.layout.pref_widget_color);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(true); // родительский клик

        ImageView imageViewColorForSettings =
                (ImageView) holder.findViewById(R.id.imageViewColor);
        imageViewColorForSettings.setColorFilter(MyColor.getMyColor(context));

        SheetPrefSelectColor
                .regCallBackSettingsApp2(imageViewColorForSettings::setColorFilter);
    }

}