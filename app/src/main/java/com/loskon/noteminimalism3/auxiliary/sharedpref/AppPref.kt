package com.loskon.noteminimalism3.auxiliary.sharedpref

import android.content.Context
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.loskon.noteminimalism3.R

class AppPref {
    companion object {

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        const val PREF_KEY_STATE_LINEAR_LIST = "key_type_notes"

        const val PREF_KEY_COLOR = "key_color"
        const val PREF_KEY_SEL_DIRECTORY = "key_sel_directory"
        const val PREF_KEY_TITLE_FONT_SIZE = "key_font_size"
        const val PREF_KEY_DATE_FONT_SIZE = "key_date_font_size"
        const val PREF_KEY_TEXT_FONT_SIZE_IN_NOTE = "key_title_font_size"
        const val PREF_KEY_POSITION_TOP = "key_position_top"
        const val PREF_KEY_POSITION_INDEX = "key_position_index"
        const val PREF_KEY_NOTES_CATEGORY = "key_sel_note_category"
        const val PREF_KEY_SORT = "key_sort"
        const val PREF_KEY_TYPE_FONT = "key_type_font_new"
        const val PREF_KEY_DIALOG_WARNING_SHOW = "key_dialog_warning_show"
        const val PREF_KEY_WEB = "key_web"
        const val PREF_KEY_MAIL = "key_mail"
        const val PREF_KEY_PHONE = "key_phone"
        const val PREF_KEY_PREFIX_WIDGET_ID = "key_widget_id_"

        ////////////////////////////////////////////////////////////////////////////////////////////
        //  string
        fun save(context: Context, key: String, value: String) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putString(key, value).apply()
        }

        private fun load(context: Context, key: String, defValue: String): String? {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue)
        }

        // int
        fun save(context: Context, key: String, value: Int) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putInt(key, value).apply()
        }

        private fun load(context: Context, key: String, defValue: Int): Int {
            return PreferenceManager.getDefaultSharedPreferences(context).getInt(key, defValue)
        }

        // boolean
        fun save(context: Context?, key: String?, value: Boolean) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            preferences.edit().putBoolean(key, value).apply()
        }

        private fun load(context: Context, key: String, defValue: Boolean): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defValue)
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun setStateLinearList(context: Context, hasLinearList: Boolean) {
            save(context, PREF_KEY_STATE_LINEAR_LIST, hasLinearList)
        }

        // int
        fun setTypeFont(context: Context, selectedFont: Int) {
            save(context, PREF_KEY_TYPE_FONT, selectedFont)
        }

        // string
        fun setBackupPath(context: Context, path: String) {
            save(context, PREF_KEY_SEL_DIRECTORY, path)
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        // boolean
        fun getOneSizeCards(context: Context): Boolean {
            val key = context.getString(R.string.one_size_title)
            return load(context, key, false)
        }

        fun isDarkMode(context: Context): Boolean {
            val key = context.getString(R.string.dark_mode_title)
            return load(context, key, false)
        }

        fun isAutoBackup(context: Context): Boolean {
            val key = context.getString(R.string.auto_backup_title)
            return load(context, key, false)
        }

        fun hasNotificationAutoBackup(context: Context): Boolean {
            val key = context.getString(R.string.notification_title)
            return load(context, key, true)
        }

        fun isUpdateDateTameWhenChanges(context: Context): Boolean {
            val key = context.getString(R.string.update_date_title)
            return load(context, key, false)
        }

        fun isWeb(context: Context): Boolean {
            return load(context, PREF_KEY_WEB, false)
        }

        fun isMail(context: Context): Boolean {
            return load(context, PREF_KEY_MAIL, false)
        }

        fun isPhone(context: Context): Boolean {
            return load(context, PREF_KEY_PHONE, false)
        }

        fun getLinearList(context: Context): Boolean {
            return load(context, PREF_KEY_STATE_LINEAR_LIST, true)
        }

        fun isDialogShow(context: Context): Boolean {
            return load(context, PREF_KEY_DIALOG_WARNING_SHOW, true)
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

        fun getIndexSettings(context: Context): Int {
            return load(context, PREF_KEY_POSITION_INDEX, 0)
        }

        fun getTopSettings(context: Context): Int {
            return load(context, PREF_KEY_POSITION_TOP, 0)
        }

        fun getNotesCategory(context: Context): Int {
            return load(context, PREF_KEY_NOTES_CATEGORY, 0)
        }

        fun getSortingWay(context: Context): Int {
            return load(context, PREF_KEY_SORT, 0)
        }

        fun getTypeFont(context: Context): Int {
            return load(context, PREF_KEY_TYPE_FONT, 0)
        }

        // string
        fun getSelectedDirectory(context: Context): String? {
            val defaultPath: String = Environment.getExternalStorageDirectory().toString()
            return load(
                context,
                PREF_KEY_SEL_DIRECTORY,
                defaultPath
            )
        }
    }
}