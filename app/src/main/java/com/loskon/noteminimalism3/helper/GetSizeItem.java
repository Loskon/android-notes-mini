package com.loskon.noteminimalism3.helper;

import android.content.Context;

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

    public static int getBorder(Context context) {
        return (int) context.getResources().getDimension(R.dimen.border);
    }

    public static int getPadding(Context context) {
        return (int) context.getResources().getDimension(R.dimen.padding_text_title);
    }
}
