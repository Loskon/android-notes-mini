package com.loskon.noteminimalism3.sharedpref

import android.content.Context
import android.os.Environment
import androidx.preference.PreferenceManager
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.getShortColor
import com.loskon.noteminimalism3.utils.getShortInt

/**
 * Менеджер для работы с SharedPreferences
 */

class PrefManager {
    companion object {

        private const val PREF_KEY_STATE_LINEAR_LIST = "key_type_notes"
        private const val PREF_KEY_COLOR = "key_color"
        private const val PREF_KEY_TITLE_FONT_SIZE = "key_font_size"
        private const val PREF_KEY_DATE_FONT_SIZE = "key_date_font_size"
        private const val PREF_KEY_TEXT_NOTE_FONT_SIZE = "key_title_font_size"
        private const val PREF_KEY_SORT = "key_sort"
        private const val PREF_KEY_TYPE_FONT = "key_type_font_new"
        private const val PREF_KEY_DIALOG_WARNING_SHOW = "key_dialog_warning_show"
        private const val PREF_KEY_WEB = "key_web"
        private const val PREF_KEY_MAIL = "key_mail"
        private const val PREF_KEY_PHONE = "key_phone"
        private const val PREF_KEY_SEL_DIRECTORY = "key_sel_directory"

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun save(context: Context, key: String, value: Boolean) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putBoolean(key, value).apply()
        }

        fun load(context: Context, key: String, defValue: Boolean): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(key, defValue)
        }

        // int
        fun save(context: Context, key: String, value: Int) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putInt(key, value).apply()
        }

        fun load(context: Context, key: String, defValue: Int): Int {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(key, defValue)
        }

        // string
        fun save(context: Context, key: String, value: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putString(key, value).apply()
        }

        fun load(context: Context, key: String, defValue: String): String? {
            return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, defValue)
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun setStateLinearList(context: Context, hasLinearList: Boolean) {
            save(context, PREF_KEY_STATE_LINEAR_LIST, hasLinearList)
        }

        fun setResultWeb(context: Context, isChecked: Boolean) {
            save(context, PREF_KEY_WEB, isChecked)
        }

        fun setResultMail(context: Context, isChecked: Boolean) {
            save(context, PREF_KEY_MAIL, isChecked)
        }

        fun setResultPhone(context: Context, isChecked: Boolean) {
            save(context, PREF_KEY_PHONE, isChecked)
        }

        fun setStatusDialogShow(context: Context) {
            save(context, PREF_KEY_DIALOG_WARNING_SHOW, false)
        }

        // int
        fun setTypeFont(context: Context, selectedFont: Int) {
            save(context, PREF_KEY_TYPE_FONT, selectedFont)
        }

        fun setTitleFontSize(context: Context, fontSizeTitle: Int) {
            save(context, PREF_KEY_TITLE_FONT_SIZE, fontSizeTitle)
        }

        fun setDateFontSize(context: Context, fontSizeDate: Int) {
            save(context, PREF_KEY_DATE_FONT_SIZE, fontSizeDate)
        }

        fun setSortingWay(context: Context, sortingWay: Int) {
            save(context, PREF_KEY_SORT, sortingWay)
        }

        fun setNoteFontSize(context: Context, fontSizeNote: Int) {
            save(context, PREF_KEY_TEXT_NOTE_FONT_SIZE, fontSizeNote)
        }

        fun setAppColor(context: Context, color: Int) {
            save(context, PREF_KEY_COLOR, color)
        }

        // string
        fun setBackupPath(context: Context, path: String) {
            save(context, PREF_KEY_SEL_DIRECTORY, path)
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun hasAutoBackup(context: Context): Boolean {
            val key = context.getString(R.string.auto_backup_key)
            return load(context, key, false)
        }

        fun hasDarkMode(context: Context): Boolean {
            val key: String = context.getString(R.string.dark_mode_key)
            return load(context, key, false)
        }

        fun hasNotificationAutoBackup(context: Context): Boolean {
            val key: String = context.getString(R.string.notification_key)
            return load(context, key, false)
        }

        fun hasUpdateDateTime(context: Context): Boolean {
            val key: String = context.getString(R.string.update_date_key)
            return load(context, key, true)
        }

        fun hasOneSizeCards(context: Context): Boolean {
            val key: String = context.getString(R.string.one_size_cards_key)
            return load(context, key, false)
        }

        fun hasLinearList(context: Context): Boolean {
            return load(context, PREF_KEY_STATE_LINEAR_LIST, true)
        }

        fun isBottomWidgetShow(context: Context): Boolean {
            val key: String = context.getString(R.string.show_bottom_widget_key)
            return load(context, key, true)
        }

        fun isDialogShow(context: Context): Boolean {
            return load(context, PREF_KEY_DIALOG_WARNING_SHOW, true)
        }

        fun isWeb(context: Context): Boolean {
            return load(context, PREF_KEY_WEB, true)
        }

        fun isMail(context: Context): Boolean {
            return load(context, PREF_KEY_MAIL, true)
        }

        fun isPhone(context: Context): Boolean {
            return load(context, PREF_KEY_PHONE, true)
        }

        // int
        fun getAppColor(context: Context): Int {
            val defValue: Int = context.getShortColor(R.color.material_blue)
            return load(context, PREF_KEY_COLOR, defValue)
        }

        fun getNumberBackups(context: Context): Int {
            val key: String = context.getString(R.string.number_of_backup_key)
            val defValue: Int = context.getShortInt(R.integer.number_of_backups_int)
            return load(context, key, defValue)
        }

        fun getRetentionRange(context: Context): Int {
            val key: String = context.getString(R.string.retention_trash_key)
            val defValue: Int = context.getShortInt(R.integer.retention_trash_int)
            return load(context, key, defValue)
        }

        fun getNumberLines(context: Context): Int {
            val key: String = context.getString(R.string.number_of_lines_key)
            val defValue: Int = context.getShortInt(R.integer.number_lines_int)
            return load(context, key, defValue)
        }

        fun getTitleFontSize(context: Context): Int {
            val defValue: Int = context.getShortInt(R.integer.title_font_size_int)
            return load(context, PREF_KEY_TITLE_FONT_SIZE, defValue)
        }

        fun getDateFontSize(context: Context): Int {
            val defValue: Int = context.getShortInt(R.integer.date_font_size_int)
            return load(context, PREF_KEY_DATE_FONT_SIZE, defValue)
        }

        fun getNoteFontSize(context: Context): Int {
            val defValue: Int = context.getShortInt(R.integer.note_font_size_int)
            return load(context, PREF_KEY_TEXT_NOTE_FONT_SIZE, defValue)
        }

        fun getSortingWay(context: Context): Int {
            return load(context, PREF_KEY_SORT, 0)
        }

        fun getTypeFont(context: Context): Int {
            return load(context, PREF_KEY_TYPE_FONT, 0)
        }

        // string
        @Suppress("DEPRECATION")
        fun getSelectedDirectory(context: Context): String {
            val defValue: String = Environment.getExternalStorageDirectory().toString()
            return load(context, PREF_KEY_SEL_DIRECTORY, defValue)!!
        }
    }
}