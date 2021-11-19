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
        // activity_list
        const val MSG_NOTE_RESTORED = "msg_note_restored"
        const val MSG_COMBINED_NOTE_ADD = "msg_combined_note_added"
        const val MSG_ERROR_COMBINING_NOTES = "msg_error_combining_notes"
        const val MSG_BUT_EMPTY_TRASH = "msg_but_empty_trash"

        // fragment_note
        const val MSG_NOTE_IS_EMPTY = "msg_note_is_empty"
        const val MSG_SAVE_TXT_COMPLETED = "msg_save_txt_completed"
        const val MSG_SAVE_TXT_FAILED = "msg_save_txt_failed"
        const val MSG_INVALID_LINK = "msg_invalid_link"
        const val MSG_NEED_COPY_TEXT = "msg_need_copy_text"
        const val MSG_INVALID_FORMAT = "msg_invalid_format"
        const val MSG_NOTE_TEXT_COPIED = "msg_text_copied"
        const val MSG_NOTE_HYPERLINKS_COPIED = "msg_hyperlinks_copied"
        const val MSG_UNABLE_CREATE_TEXT_FILE = "msg__unable_create_text_file"
        const val MSG_INSERTED_TEXT = "msg_text_inserted"

        // fragment_settings
        const val MSG_UNABLE_SELECT_FOLDER = "msg_unable_select_folder"
        const val MSG_LOCAL_STORAGE = "msg_you_can_local_storage"

        // fragment_backup
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
        const val MSG_INTERNET_PROBLEM = "msg_internet_problem"
        const val MSG_BACKUP_NOT_SELECTED = "msg_backup_not_selected"
        const val MSG_INVALID_FORMAT_FILE = "msg_invalid_format_file"

        // other
        const val MSG_NO_PERMISSION = "msg_no_permission"
        const val MSG_UNKNOWN_ERROR = "msg_unknown_error"
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
                // activity_list
                MSG_NOTE_RESTORED -> R.string.sb_note_restored
                MSG_COMBINED_NOTE_ADD -> R.string.sb_combined_note_added
                MSG_ERROR_COMBINING_NOTES -> R.string.sb_error_combining_notes
                MSG_BUT_EMPTY_TRASH -> R.string.sb_but_empty_trash
                // fragment_note
                MSG_NOTE_IS_EMPTY -> R.string.sb_note_is_empty
                MSG_SAVE_TXT_COMPLETED -> R.string.sb_note_create_text_files_completed
                MSG_SAVE_TXT_FAILED -> R.string.sb_note_create_text_file_failed
                MSG_INVALID_LINK -> R.string.sb_note_invalid_link
                MSG_NEED_COPY_TEXT -> R.string.sb_note_need_copy_text
                MSG_INVALID_FORMAT -> R.string.sb_note_invalid_format
                MSG_NOTE_TEXT_COPIED -> R.string.sb_note_text_copied
                MSG_NOTE_HYPERLINKS_COPIED -> R.string.sb_note_hyperlinks_copied
                MSG_UNABLE_CREATE_TEXT_FILE -> R.string.sb_unable_create_text_file
                MSG_INSERTED_TEXT -> R.string.sb_note_text_inserted
                // fragment_settings
                MSG_UNABLE_SELECT_FOLDER -> R.string.sb_settings_unable_select_folder
                MSG_LOCAL_STORAGE -> R.string.sb_settings_you_can_local_storage
                // fragment_backup
                MSG_TEXT_ERROR -> R.string.sb_bp_text_error
                MSG_BACKUP_COMPLETED -> R.string.sb_bp_completed
                MSG_BACKUP_FAILED -> R.string.sb_bp_failed
                MSG_RESTORE_COMPLETED -> R.string.sb_bp_restore_completed
                MSG_RESTORE_FAILED -> R.string.sb_bp_restore_failed
                MSG_BACKUP_FILES_DELETED -> R.string.sb_bp_backup_files_deleted
                MSG_TEXT_SIGN_IN_FAILED -> R.string.sb_bp_sign_in_failed
                MSG_TEXT_OUT -> R.string.sb_bp_logged_account
                MSG_TEXT_NO_INTERNET -> R.string.sb_bp_no_internet
                MSG_DEL_DATA -> R.string.sb_bp_delete_data_from_cloud
                MSG_BACKUP_NOT_FOUND -> R.string.sb_bp_backup_not_found
                MSG_INTERNET_PROBLEM -> R.string.sb_bp_problem_internet
                MSG_BACKUP_NOT_SELECTED -> R.string.sb_bp_backup_not_selected
                MSG_INVALID_FORMAT_FILE -> R.string.sb_bp_invalid_format_file
                // other
                MSG_NO_PERMISSION -> R.string.no_permissions
                else -> R.string.unknown_error
            }

            return getString(message)
        }
    }

    private fun getSuccess(typeMessage: String): Boolean {
        context.apply {
            val message: Boolean = when (typeMessage) {
                // activity_list
                MSG_NOTE_RESTORED -> true
                MSG_COMBINED_NOTE_ADD -> true
                // fragment_note
                MSG_SAVE_TXT_COMPLETED -> true
                MSG_NOTE_TEXT_COPIED -> true
                MSG_NOTE_HYPERLINKS_COPIED -> true
                MSG_INSERTED_TEXT -> true
                // fragment_backup
                MSG_BACKUP_COMPLETED -> true
                MSG_RESTORE_COMPLETED -> true
                MSG_BACKUP_FILES_DELETED -> true
                MSG_TEXT_OUT -> true
                MSG_DEL_DATA -> true
                else -> false
            }

            return message
        }
    }
}
