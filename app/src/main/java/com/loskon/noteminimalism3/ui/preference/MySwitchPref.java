package com.loskon.noteminimalism3.ui.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;

import com.addisonelliott.segmentedbutton.SegmentedButtonGroup;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.GetSizeItem;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;


public class MySwitchPref extends SwitchPreference {

    private Context context;

    public MySwitchPref(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySwitchPref(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setWidgetLayoutResource(R.layout.custom_switch2);
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

        PrefHelper.setItemsSize(context, holder);

       // SegmentedGroup segmentedGroup = (SegmentedGroup) holder.findViewById(R.id.segmented_group);
        //segmentedGroup.setTintColor(MyColor.getColorCustom(getContext()));

        SegmentedButtonGroup segmentedButtonGroup =
                (SegmentedButtonGroup) holder.findViewById(R.id.buttonGroup_lordOfTheRings);

        int color = MyColor.getColorCustom(context);
        int borderGroup = GetSizeItem.getBorder(context);

        segmentedButtonGroup.setSelectedBackgroundColor(color);
        segmentedButtonGroup.setBorder(borderGroup, color, 0, 0);
        segmentedButtonGroup.setClickable(false);

        boolean toggleButton = MySharedPref.getBoolean(context, getKey(), false);

        if (toggleButton) {
            segmentedButtonGroup.setPosition(1, false);
        } else {
            segmentedButtonGroup.setPosition(0, false);
        }
    }
}
