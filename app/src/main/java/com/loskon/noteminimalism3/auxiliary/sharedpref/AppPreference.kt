package com.loskon.noteminimalism3.auxiliary.sharedpref

import android.content.Context
import android.preference.PreferenceManager

class AppPreference {
    companion object {
        // Keys
        const val PREF_KEY_TYPE_NOTES = "key_type_notes"
        const val PREF_KEY_COLOR = "key_color"
        const val PREF_KEY_SEL_DIRECTORY = "key_sel_directory"
        const val PREF_KEY_TITLE_FONT_SIZE = "key_font_size"
        const val PREF_KEY_DATE_FONT_SIZE = "key_date_font_size"
        const val PREF_KEY_TITLE_FONT_SIZE_NOTE = "key_title_font_size"
        const val PREF_KEY_POSITION_TOP = "key_position_top"
        const val PREF_KEY_POSITION_INDEX = "key_position_index"
        const val PREF_KEY_NOTES_CATEGORY = "key_sel_note_category"
        const val PREF_KEY_SORT = "key_sort"
        const val PREF_KEY_PREFIX_WIDGET_ID = "key_widget_id_"
        const val PREF_KEY_TYPE_FONT = "key_type_font"
        const val PREF_KEY_DIALOG_WARNING_SHOW = "key_dialog_warning_show"
        const val PREF_KEY_WEB = "key_web"
        const val PREF_KEY_MAIL = "key_mail"
        const val PREF_KEY_PHONE = "key_phone"


        // String
        fun saveString(context: Context, key: String, value: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putString(key, value).apply()
        }

        fun loadString(context: Context, key: String, defValue: String): String? {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue)
        }

        // int
        fun saveInt(context: Context, key: String, value: Int) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putInt(key, value).apply()
        }

        fun loadInt(context: Context, key: String, defValue: Int): Int {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defValue)
        }

        // boolean
        fun saveBoolean(context: Context?, key: String?, value: Boolean) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putBoolean(key, value).apply()
        }

        fun loadBoolean(context: Context, key: String, defValue: Boolean): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defValue)
        }


        // Getters
    }
}