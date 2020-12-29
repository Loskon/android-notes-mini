package com.loskon.noteminimalism3.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.loskon.noteminimalism3.R;

public class GetSizeItem {

    public static int getHeightItem(Context context) {
        return (int) context.getResources().getDimension(R.dimen.height_item);
    }

    public static int getRadiusLinLay(Context context) {
        return (int) context.getResources().getDimension(R.dimen.corner_radius);
    }

    public static int getStrokeLinLay(Context context) {
        return (int) context.getResources().getDimension(R.dimen.border_stroke);
    }
}
