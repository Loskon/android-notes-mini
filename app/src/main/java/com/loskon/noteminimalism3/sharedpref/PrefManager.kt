package com.loskon.noteminimalism3.sharedpref

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.loskon.noteminimalism3.R

/**
 * Работа с SharedPreferences
 */

class PrefManager {
    companion object {

        private const val PREF_KEY_STATE_LINEAR_LIST = "key_type_notes"
        private const val PREF_KEY_COLOR = "key_color"
        private const val PREF_KEY_TITLE_FONT_SIZE = "key_font_size"
        private const val PREF_KEY_DATE_FONT_SIZE = "key_date_font_size"
        private const val PREF_KEY_TEXT_FONT_SIZE_IN_NOTE = "key_title_font_size"
        private const val PREF_KEY_SORT = "key_sort"
        private const val PREF_KEY_TYPE_FONT = "key_type_font_new"
        private const val PREF_KEY_DIALOG_WARNING_SHOW = "key_dialog_warning_show"
        private const val PREF_KEY_WEB = "key_web"
        private const val PREF_KEY_MAIL = "key_mail"
        private const val PREF_KEY_PHONE = "key_phone"
        const val PREF_KEY_SEL_DIRECTORY = "key_sel_directory"

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun save(context: Context?, key: String?, value: Boolean) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putBoolean(key, value).apply()
        }

        fun load(context: Context, key: String, defValue: Boolean): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defValue)
        }

        // int
        fun save(context: Context, key: String, value: Int) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putInt(key, value).apply()
        }

        fun load(context: Context, key: String, defValue: Int): Int {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defValue)
        }

        // string
        fun save(context: Context, key: String, value: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putString(key, value).apply()
        }

        @JvmStatic
        fun load(context: Context, key: String, defValue: String): String? {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue)
        }


        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun setStateLinearList(context: Context, hasLinearList: Boolean) {
            save(context, PREF_KEY_STATE_LINEAR_LIST, hasLinearList)
        }

        fun setStatusDialogShow(context: Context) {
            save(context, PREF_KEY_DIALOG_WARNING_SHOW, false)
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

        // int
        fun setTypeFont(context: Context, selectedFont: Int) {
            save(context, PREF_KEY_TYPE_FONT, selectedFont)
        }

        fun setFontSizeTitle(context: Context, fontSizeTitle: Int) {
            save(context, PREF_KEY_TITLE_FONT_SIZE, fontSizeTitle)
        }

        fun setFontSizeDate(context: Context, fontSizeDate: Int) {
            save(context, PREF_KEY_DATE_FONT_SIZE, fontSizeDate)
        }

        fun setSortingWay(context: Context, sortingWay: Int) {
            save(context, PREF_KEY_SORT, sortingWay)
        }

        fun setFontSizeNote(context: Context, fontSizeNote: Int) {
            save(context, PREF_KEY_TEXT_FONT_SIZE_IN_NOTE, fontSizeNote)
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
        fun getOneSizeCards(context: Context): Boolean {
            val key: String = context.getString(R.string.one_size_cards_title)
            return load(context, key, false)
        }

        fun isAutoBackup(context: Context): Boolean {
            val key = context.getString(R.string.auto_backup_title)
            return load(context, key, false)
        }

        fun isDarkMode(context: Context): Boolean {
            val key: String = context.getString(R.string.dark_mode_title)
            return load(context, key, false)
        }

        fun hasNotificationAutoBackup(context: Context): Boolean {
            val key: String = context.getString(R.string.notification_title)
            return load(context, key, false)
        }

        fun isWeb(context: Context): Boolean {
            return load(
                context,
                PREF_KEY_WEB,
                false
            )
        }

        fun isMail(context: Context): Boolean {
            return load(
                context,
                PREF_KEY_MAIL,
                false
            )
        }

        fun isPhone(context: Context): Boolean {
            return load(
                context,
                PREF_KEY_PHONE,
                false
            )
        }

        fun getLinearList(context: Context): Boolean {
            return load(
                context,
                PREF_KEY_STATE_LINEAR_LIST,
                true
            )
        }

        fun isDialogShow(context: Context): Boolean {
            return load(
                context,
                PREF_KEY_DIALOG_WARNING_SHOW,
                true
            )
        }

        // int
        fun getAppColor(context: Context): Int {
            return load(
                context,
                PREF_KEY_COLOR,
                ContextCompat.getColor(context, R.color.material_blue)
            )

        }

        fun getNumberBackups(context: Context): Int {
            val key = context.getString(R.string.num_of_backup_title)
            return load(
                context,
                key,
                context.resources.getInteger(R.integer.number_of_backups)
            )
        }

        fun getRetentionRange(context: Context): Int {
            val key = context.getString(R.string.retention_trash_title)
            return load(
                context,
                key,
                context.resources.getInteger(R.integer.number_retention_note_in_trash)
            )
        }

        fun getNumberLines(context: Context): Int {
            val key = context.getString(R.string.num_of_lines_header)
            return load(
                context,
                key,
                context.resources.getInteger(R.integer.number_lines)
            )
        }

        fun getFontSize(context: Context): Int {
            return load(
                context,
                PREF_KEY_TITLE_FONT_SIZE,
                context.resources.getInteger(R.integer.number_font_size_title)
            )
        }

        fun getDateFontSize(context: Context): Int {
            return load(
                context,
                PREF_KEY_DATE_FONT_SIZE,
                context.resources.getInteger(R.integer.number_font_size_date)
            )
        }

        fun getFontSizeNote(context: Context): Int {
            return load(
                context,
                PREF_KEY_TEXT_FONT_SIZE_IN_NOTE,
                context.resources.getInteger(R.integer.number_font_size_title)
            )
        }

        fun getSortingWay(context: Context): Int {
            return load(
                context,
                PREF_KEY_SORT,
                0
            )
        }

        fun getTypeFont(context: Context): Int {
            return load(
                context,
                PREF_KEY_TYPE_FONT,
                0
            )
        }

        // string
        @Suppress("DEPRECATION")
        fun getSelectedDirectory(context: Context): String? {
            return load(
                context,
                PREF_KEY_SEL_DIRECTORY,
                Environment.getExternalStorageDirectory().toString()
            )
        }
    }
}