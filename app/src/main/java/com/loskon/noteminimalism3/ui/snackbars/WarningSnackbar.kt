package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.loskon.noteminimalism3.R

/**
 * Управление Snackbar
 */

object WarningSnackbar {

    // activity_list
    const val MSG_NOTE_RESTORED = "note_restored_msg_snack"
    const val MSG_COMBINED_NOTE_ADD = "combined_note_added_msg_snack"
    const val MSG_ERROR_COMBINING_NOTES = "error_combining_notes_msg_snack"
    const val MSG_BUT_EMPTY_TRASH = "but_empty_trash_msg_snack"
    const val MSG_SELECT_ONE_NOTE = "select_one_note_msg_snack"

    // fragment_note
    const val MSG_NOTE_IS_EMPTY = "note_is_empty_msg_snack"
    const val MSG_SAVE_TXT_COMPLETED = "save_txt_completed_msg_snack"
    const val MSG_SAVE_TXT_FAILED = "save_txt_failed_msg_snack"
    const val MSG_INVALID_LINK = "invalid_link_msg_snack"
    const val MSG_NEED_COPY_TEXT = "need_copy_text_msg_snack"
    const val MSG_INVALID_FORMAT = "invalid_format_msg_snack"
    const val MSG_NOTE_TEXT_COPIED = "text_copied_msg_snack"
    const val MSG_NOTE_HYPERLINKS_COPIED = "hyperlinks_copied_msg_snack"

    // fragment_settings
    const val MSG_UNABLE_SELECT_FOLDER = "unable_select_folder_msg_snack"
    const val MSG_LOCAL_STORAGE = "you_can_local_storage_msg_snack"

    // fragment_backup
    const val MSG_UNABLE_CREATE_FOLDER = "unable_create_folder_msg_snack"
    const val MSG_BACKUP_COMPLETED = "backup_completed_msg_snack"
    const val MSG_BACKUP_FAILED = "backup_failed_msg_snack"
    const val MSG_RESTORE_COMPLETED = "restore_completed_msg_snack"
    const val MSG_RESTORE_FAILED = "restore_failed_msg_snack"
    const val MSG_BACKUP_FILES_DELETED = "backup_files_deleted_msg_snack"
    const val MSG_TEXT_SIGN_IN_FAILED = "sign_in_failed_msg_snack"
    const val MSG_TEXT_OUT = "out_msg_snack"
    const val MSG_TEXT_NO_INTERNET = "no_internet_msg_snack"
    const val MSG_DEL_DATA = "del_data_msg_snack"
    const val MSG_BACKUP_NOT_FOUND = "backup_not_found_msg_snack"
    const val MSG_INTERNET_PROBLEM = "internet_problem_msg_snack"
    const val MSG_BACKUP_NOT_SELECTED = "backup_not_selected_msg_snack"
    const val MSG_INVALID_FORMAT_FILE = "invalid_format_file_msg_snack"

    // other
    const val MSG_NO_PERMISSION = "no_permission_msg_snack"
    const val MSG_UNKNOWN_ERROR = "unknown_error_msg_snack"

    fun show(layout: ViewGroup, anchorView: View, typeMessage: String) {
        val context: Context = layout.context
        val message: String = getMessage(context, typeMessage)
        val isSuccess: Boolean = getSuccess(context, typeMessage)

        WarningBaseSnackbar.make(layout, anchorView, message, isSuccess)
    }

    private fun getMessage(context: Context, typeMessage: String): String {
        context.apply {
            val message: Int = when (typeMessage) {
                // activity_list
                MSG_NOTE_RESTORED -> R.string.sb_note_restored
                MSG_COMBINED_NOTE_ADD -> R.string.sb_combined_note_added
                MSG_ERROR_COMBINING_NOTES -> R.string.sb_error_combining_notes
                MSG_BUT_EMPTY_TRASH -> R.string.sb_but_empty_trash
                MSG_SELECT_ONE_NOTE -> R.string.sb_select_one_note
                // fragment_note
                MSG_NOTE_IS_EMPTY -> R.string.sb_note_is_empty
                MSG_SAVE_TXT_COMPLETED -> R.string.sb_note_create_text_files_completed
                MSG_SAVE_TXT_FAILED -> R.string.sb_note_create_text_file_failed
                MSG_INVALID_LINK -> R.string.sb_note_invalid_link
                MSG_NEED_COPY_TEXT -> R.string.sb_note_need_copy_text
                MSG_INVALID_FORMAT -> R.string.sb_note_invalid_format
                MSG_NOTE_TEXT_COPIED -> R.string.sb_note_text_copied
                MSG_NOTE_HYPERLINKS_COPIED -> R.string.sb_note_hyperlinks_copied
                // fragment_settings
                MSG_UNABLE_SELECT_FOLDER -> R.string.sb_settings_unable_select_folder
                MSG_LOCAL_STORAGE -> R.string.sb_settings_you_can_local_storage
                // fragment_backup
                MSG_UNABLE_CREATE_FOLDER -> R.string.sb_bp_unable_created_folder
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

    private fun getSuccess(context: Context, typeMessage: String): Boolean {
        context.apply {
            val message: Boolean = when (typeMessage) {
                // activity_list
                MSG_NOTE_RESTORED -> true
                MSG_COMBINED_NOTE_ADD -> true
                // fragment_note
                MSG_SAVE_TXT_COMPLETED -> true
                MSG_NOTE_TEXT_COPIED -> true
                MSG_NOTE_HYPERLINKS_COPIED -> true
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
