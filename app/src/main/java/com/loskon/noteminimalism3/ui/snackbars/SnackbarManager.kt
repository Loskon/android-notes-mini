package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.view.View
import com.loskon.noteminimalism3.R

/**
 * Управление Snackbar
 */

class SnackbarManager(
    private val context: Context,
    private val layout: View,
    private val anchorView: View
) {

    companion object {
        // Для списка заметок
        const val MSG_AUTO_BACKUP_COMPLETED = "msg_auto_backup_completed"
        const val MSG_AUTO_BACKUP_FAILED = "msg_auto_backup_failed"
        const val MSG_AUTO_BACKUP_NOT_POSSIBLE = "msg_auto_backup_not_possible"
        const val MSG_NOTE_RESTORED = "msg_note_restored"
        const val MSG_COMBINED_NOTE_ADD = "msg_combined_note_added"
        const val MSG_ERROR_COMBINING_NOTES = "msg_error_combining_notes"
        const val MSG_BUT_EMPTY_TRASH = "msg_but_empty_trash"

        // Для Заметок
        const val MSG_NOTE_IS_EMPTY = "msg_note_is_empty"
        const val MSG_SAVE_TXT_COMPLETED = "msg_save_txt_completed"
        const val MSG_SAVE_TXT_FAILED = "msg_save_txt_failed"
        const val MSG_INVALID_LINK = "msg_invalid_link"
        const val MSG_NEED_COPY_TEXT = "msg_need_copy_text"
        const val MSG_INVALID_FORMAT = "msg_invalid_format"
        const val MSG_NOTE_TEXT_COPIED = "msg_text_copied"
        const val MSG_NOTE_HYPERLINKS_COPIED = "msg_hyperlinks_copied"

        // Общее
        const val MSG_UNABLE_CREATE_FILE = "msg_unable_create_file"
        const val MSG_UNKNOWN_ERROR = "msg_unknown_error"

        // Настройки
        const val MSG_NO_PERMISSION = "msg_no_permission"
        const val MSG_UNABLE_SELECT_FOLDER = "msg_unable_select_folder"

        const val MSG_TEXT_NO_FOLDER = "msg_text_no_folder"
        const val MSG_TEXT_ERROR = "msg_text_error"

        const val MSG_BACKUP_COMPLETED = "msg_backup_completed"
        const val MSG_BACKUP_FAILED = "msg_backup_failed"

        const val MSG_RESTORE_COMPLETED = "msg_restore_completed"
        const val MSG_RESTORE_FAILED = "msg_restore_failed"

        const val MSG_BACKUP_FILES_DELETED = "msg_backup_files_deleted"

        const val MSG_TEXT_SIGN_IN_FAILED = "msg_sign_in_failed"
        const val MSG_TEXT_OUT = "msg_out"
        const val MSG_TEXT_NO_INTERNET = "msg_no_internet"
        const val MSG_DEL_DATA = "msg_del_data"

        const val MSG_BACKUP_NOT_FOUND = "msg_backup_not_found"
        const val MSG_LOCAL_STORAGE = "msg_you_can_local_storage"

        const val MSG_INSERTED_TEXT = "msg_text_inserted"
        const val MSG_INTERNET_PROBLEM = "msg_internet_problem"
    }

    fun show(typeMessage: String) {
        val message: String = getMessage(typeMessage)
        val isSuccess: Boolean = getSuccess(typeMessage)
        BaseSnackbar.make(context, layout, anchorView, message, isSuccess)
    }

    fun show(typeMessage: String, isSuccess: Boolean) {
        val message: String = getMessage(typeMessage)
        BaseSnackbar.make(context, layout, anchorView, message, isSuccess)
    }

    private fun getMessage(typeMessage: String): String {
        context.apply {
            val message: Int = when (typeMessage) {
                // Для списка заметок
                MSG_AUTO_BACKUP_COMPLETED -> R.string.toast_main_auto_backup_completed
                MSG_AUTO_BACKUP_FAILED -> R.string.toast_main_auto_backup_failed
                MSG_AUTO_BACKUP_NOT_POSSIBLE -> R.string.toast_main_auto_backup_not_possible
                MSG_NOTE_RESTORED -> R.string.sb_main_restored
                MSG_COMBINED_NOTE_ADD -> R.string.sb_main_combined_note_added
                MSG_ERROR_COMBINING_NOTES -> R.string.sb_main_error_combining_notes
                MSG_BUT_EMPTY_TRASH -> R.string.sb_main_but_empty_trash
                // Для Заметок
                MSG_NOTE_IS_EMPTY -> R.string.sb_note_is_empty
                MSG_SAVE_TXT_COMPLETED -> R.string.sb_note_save_txt_completed
                MSG_SAVE_TXT_FAILED -> R.string.sb_note_save_txt_failed
                MSG_INVALID_LINK -> R.string.sb_note_invalid_link
                MSG_NEED_COPY_TEXT -> R.string.sb_note_need_copy_text
                MSG_INVALID_FORMAT -> R.string.sb_note_invalid_format
                MSG_NOTE_TEXT_COPIED -> R.string.sb_note_text_copied
                MSG_NOTE_HYPERLINKS_COPIED -> R.string.sb_note_hyperlinks_copied
                // Общее
                MSG_UNABLE_CREATE_FILE -> R.string.toast_main_auto_backup_unable_create
                // Настройки
                MSG_NO_PERMISSION -> R.string.no_permissions
                MSG_UNABLE_SELECT_FOLDER -> R.string.sb_settings_unable_select_folder
                MSG_TEXT_NO_FOLDER -> R.string.sb_bp_text_no_folder
                MSG_TEXT_ERROR -> R.string.sb_bp_text_error
                MSG_BACKUP_COMPLETED -> R.string.sb_bp_completed
                MSG_BACKUP_FAILED -> R.string.sb_bp_failed
                MSG_RESTORE_COMPLETED -> R.string.sb_bp_restore_completed
                MSG_RESTORE_FAILED -> R.string.sb_bp_restore_failed
                MSG_BACKUP_FILES_DELETED -> R.string.sb_bp_backup_files_deleted
                MSG_TEXT_SIGN_IN_FAILED -> R.string.sb_bp_sign_in_failed
                MSG_TEXT_OUT -> R.string.sb_bp_logged_account
                MSG_TEXT_NO_INTERNET -> R.string.sb_bp_no_internet
                MSG_DEL_DATA -> R.string.sb_bp_del_acc
                MSG_BACKUP_NOT_FOUND -> R.string.sb_bp_backup_not_found
                MSG_LOCAL_STORAGE -> R.string.sb_bp_you_can_local_storage
                MSG_INSERTED_TEXT -> R.string.sb_note_text_inserted
                MSG_INTERNET_PROBLEM -> R.string.sb_bp_problem_internet
                else -> R.string.unknown_error
            }

            return getString(message)
        }
    }

    private fun getSuccess(typeMessage: String): Boolean {
        context.apply {
            val message: Boolean = when (typeMessage) {
                // Для списка заметок
                MSG_AUTO_BACKUP_COMPLETED -> true
                MSG_AUTO_BACKUP_FAILED -> false
                MSG_AUTO_BACKUP_NOT_POSSIBLE -> false
                MSG_NOTE_RESTORED -> true
                MSG_COMBINED_NOTE_ADD -> true
                MSG_ERROR_COMBINING_NOTES -> false
                MSG_BUT_EMPTY_TRASH -> false
                // Для Заметок
                MSG_NOTE_IS_EMPTY -> false
                MSG_SAVE_TXT_COMPLETED -> true
                MSG_SAVE_TXT_FAILED -> false
                MSG_INVALID_LINK -> false
                MSG_NEED_COPY_TEXT -> false
                MSG_INVALID_FORMAT -> false
                MSG_NOTE_TEXT_COPIED -> true
                MSG_NOTE_HYPERLINKS_COPIED -> true
                // Общее
                MSG_UNABLE_CREATE_FILE -> false
                // Настройки
                MSG_NO_PERMISSION -> false
                MSG_UNABLE_SELECT_FOLDER -> false
                MSG_TEXT_NO_FOLDER -> false
                MSG_TEXT_ERROR -> false
                MSG_BACKUP_COMPLETED -> true
                MSG_BACKUP_FAILED -> false
                MSG_RESTORE_COMPLETED -> true
                MSG_RESTORE_FAILED -> false
                MSG_BACKUP_FILES_DELETED -> true
                MSG_TEXT_SIGN_IN_FAILED -> false
                MSG_TEXT_OUT -> true
                MSG_TEXT_NO_INTERNET -> false
                MSG_DEL_DATA -> true
                MSG_BACKUP_NOT_FOUND -> false
                MSG_LOCAL_STORAGE -> false
                MSG_INSERTED_TEXT -> true
                MSG_INTERNET_PROBLEM -> false
                else -> false
            }

            return message
        }
    }
}
