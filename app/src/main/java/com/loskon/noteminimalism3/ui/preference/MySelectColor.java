package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;

public class MySelectColor extends Preference {

    private final Context context;

    public MySelectColor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySelectColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWidgetLayoutResource(R.layout.pref_widget_color);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(true); // родительский клик
        PrefHelper.setTitleSetting(context, holder, true);

        ImageView imageViewColorForSettings =
                (ImageView) holder.findViewById(R.id.imageViewColorForSettings);
        imageViewColorForSettings.setColorFilter(MyColor.getColorCustom(context));

        (new MyDialogColor())
                .registerCallBackColorSettingsApp(imageViewColorForSettings::setColorFilter);
    }

}