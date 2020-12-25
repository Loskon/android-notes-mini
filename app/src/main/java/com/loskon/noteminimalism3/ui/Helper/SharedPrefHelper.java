package com.loskon.noteminimalism3.ui.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/**
 *
 */

public class SharedPrefHelper {

    public static final String KEY_ONE_SIZE = "isOneSizeOn";
    public static final String KEY_NUM_OF_LINES = "numOfLines";
    public static final String KEY_TYPE_NOTES = "isTypeNotesSingleOn";
    public static final String KEY_COLOR = "color";
    public static final String KEY_SEL_CATEGORY = "selNotesCategory";


    public static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadString(Context context, String key, String defValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defValue);
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int loadInt(Context context, String key, int defValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defValue);
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean loadBoolean(Context context, String key, boolean defValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defValue);
    }

}
