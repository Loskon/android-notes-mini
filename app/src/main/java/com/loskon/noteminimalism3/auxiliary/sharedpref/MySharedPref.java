package com.loskon.noteminimalism3.auxiliary.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

/**
 * Помощник для работы с данными постоянного хранилища
 */

public class MySharedPref {

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
}
