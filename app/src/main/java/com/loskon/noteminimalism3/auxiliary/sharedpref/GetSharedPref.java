package com.loskon.noteminimalism3.auxiliary.sharedpref;

import android.content.Context;

import com.loskon.noteminimalism3.R;

/**
 * Получение сохраненных значений
 */

public class GetSharedPref {

    // boolean
    public static boolean isOneSize(Context context) {
        String key = context.getString(R.string.one_size_title);
        return MySharedPref.getBoolean(context, key, false);
    }

    public static boolean isDarkMode(Context context) {
        String key = context.getString(R.string.dark_mode_title);
        return MySharedPref.getBoolean(context, key, false);
    }

    public static boolean isAutoBackup(Context context) {
        String key = context.getString(R.string.auto_backup_title);
        return MySharedPref.getBoolean(context, key, false);
    }

    public static boolean hasNotificationAutoBackup(Context context) {
        String key = context.getString(R.string.notification_title);
        return MySharedPref.getBoolean(context, key, true);
    }

    public static boolean isUpdateDateTameWhenChanges(Context context) {
        String key = context.getString(R.string.update_date_title);
        return MySharedPref.getBoolean(context, key, false);
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

    public static boolean isDialogShow(Context context) {
        return MySharedPref.getBoolean(context, MyPrefKey.KEY_DIALOG_WARNING_SHOW, true);
    }

    // int
    public static int getNumOfBackup(Context context) {
        String key = context.getString(R.string.num_of_backup_title);
        return MySharedPref.getInt(context, key, 3);
    }

    public static int getRangeInDays(Context context) {
        String key = context.getString(R.string.retention_trash_title);
        return MySharedPref.getInt(context, key, 2);
    }

    public static int getNumOfLines(Context context) {
        String key = context.getString(R.string.num_of_lines_header);
        return MySharedPref.getInt(context, key, 3);
    }

    public static int getFontSize(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_TITLE_FONT_SIZE, 18);
    }

    public static int getDateFontSize(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_DATE_FONT_SIZE, 14);
    }

    public static int getFontSizeNote(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_TITLE_FONT_SIZE_NOTE, 18);
    }

    public static int getIndexSettings(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_POSITION_INDEX, 0);
    }

    public static int getTopSettings(Context context) {
        return MySharedPref.getInt(context, MyPrefKey.KEY_POSITION_TOP, 0);
    }

    public static int getNotesCategory(Context context) {
        return MySharedPref.getInt(context,MyPrefKey.KEY_NOTES_CATEGORY, 0);
    }

    public static int getSort(Context context) {
        return MySharedPref.getInt(context,MyPrefKey.KEY_SORT, 0);
    }

    public static int getTypeFont(Context context) {
        return MySharedPref.getInt(context,MyPrefKey.KEY_TYPE_FONT, -1);
    }
}
