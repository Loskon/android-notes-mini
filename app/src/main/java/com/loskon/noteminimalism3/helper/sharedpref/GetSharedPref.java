package com.loskon.noteminimalism3.helper.sharedpref;

import android.app.Activity;
import android.content.Context;

import com.loskon.noteminimalism3.R;

public class GetSharedPref {

    public static int getNumOfLines(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_NUM_OF_LINES, 3);
    }

    public static boolean isOneSize(Context context) {
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

    public static boolean isDarkModeOn(Activity activity) {
        String key = activity.getString(R.string.dark_mode_title);
        return MySharedPref.getBoolean(activity, key, false);
    }

    public static int getNotesCategory(Context context) {
        return MySharedPref.getInt(context,MyPrefKey.KEY_NOTES_CATEGORY, 0);
    }

    public static boolean isAutoBackup(Context context) {
        String key = context.getString(R.string.auto_backup);
        return MySharedPref.getBoolean(context, key, false);
    }

    public static boolean isNotAutoBackup(Context context) {
        String key = context.getString(R.string.notification_auto_backup);
        return MySharedPref.getBoolean(context, key, true);
    }

    public static boolean isWeb(Context context) {
        return MySharedPref.getBoolean(context, MyPrefKey.KEY_WEB, false);
    }

    public static boolean isMail(Context context) {
        return MySharedPref.getBoolean(context, MyPrefKey.KEY_MAIL, false);
    }

    public static boolean isPhone(Context context) {
        return MySharedPref.getBoolean(context, MyPrefKey.KEY_PHONE, false);
    }

    public static boolean isTypeSingle(Context context) {
        return MySharedPref.getBoolean(context, MyPrefKey.KEY_TYPE_NOTES, true);
    }

}
