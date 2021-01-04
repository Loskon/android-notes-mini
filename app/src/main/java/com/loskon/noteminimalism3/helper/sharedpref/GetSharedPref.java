package com.loskon.noteminimalism3.helper.sharedpref;

import android.content.Context;

public class GetSharedPref {

    public static int getNumOfLines(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_NUM_OF_LINES, 3);
    }

    public static boolean getOneSize(Context context) {
        return MySharedPref.getBoolean(context, MyPrefKey.KEY_ONE_SIZE, false);
    }

    public static int getFontSize(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_TITLE_FONT_SIZE, 18);
    }

    public static int getDateFontSize(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_DATE_FONT_SIZE, 14);
    }

    public static int getIndex(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_POSITION_INDEX, 0);
    }

    public static int getTop(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_POSITION_TOP, 0);
    }

    public static int getNumOfBackup(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_NUM_OF_BACKUP, 3);
    }
}
