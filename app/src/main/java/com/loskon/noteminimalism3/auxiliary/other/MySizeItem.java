package com.loskon.noteminimalism3.auxiliary.other;

import android.content.Context;

import com.loskon.noteminimalism3.R;

/**
 * Размеры элементов в dp
 */

public class MySizeItem {

    public static int getRadiusLinLay(Context context) {
        return (int) context.getResources().getDimension(R.dimen.corner_radius);
    }

    public static int getStrokeLinLay(Context context) {
        return (int) context.getResources().getDimension(R.dimen.border_stroke);
    }

    public static int getBorderWidghtSwitch(Context context) {
        return (int) context.getResources().getDimension(R.dimen.border_width_switch);
    }

    public static int getSizeRefresh(Context context) {
        return (int) context.getResources().getDimension(R.dimen.height_refresh_layout);
    }
}
