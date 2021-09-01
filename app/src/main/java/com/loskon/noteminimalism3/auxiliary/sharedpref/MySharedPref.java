package com.loskon.noteminimalism3.auxiliary.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey.PREF_PREFIX_WIDGET_ID;

/**
 * Помощник для работы с данными постоянного хранилища
 */

public class MySharedPref {

    private static final String PREFS_NAME_WIDGET = "name_for_widget";

    // String
    public static void setString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defValue) {
        return PreferenceManager
                .getDefaultSharedPreferences(context).getString(key, defValue);
    }


    // int
    public static void setInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        return PreferenceManager
                .getDefaultSharedPreferences(context).getInt(key, defValue);
    }


    // boolean
    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(key, defValue);
    }


    // Custom pref
    public static void setCustomInt(Context context, long noteId, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_WIDGET, 0).edit();
        prefs.putInt(PREF_PREFIX_WIDGET_ID + noteId, appWidgetId);
        prefs.apply();
    }

    public static int getCustomInt(Context context, long noteId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME_WIDGET, 0);
        return prefs.getInt(PREF_PREFIX_WIDGET_ID + noteId, -1);
    }

    public static void deleteCustomInt(Context context, long noteId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME_WIDGET, 0).edit();
        prefs.remove(PREF_PREFIX_WIDGET_ID + noteId).apply();
    }
}
