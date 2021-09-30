package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.view.View
import com.loskon.noteminimalism3.R

/**
 *
 */

class SnackbarMessage(
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

        // Для Заметок
        const val MSG_TEXT_NO_PERMISSION_NOTE = "msg_no_permission"
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
        const val MSG_ERROR = "msg_error"
    }

    fun show(typeMessage: String) {
        val message: String = getMessage(typeMessage)
        BaseSnackbar.make(context, layout, anchorView, message)
    }

    fun show(typeMessage: String, isSuccess: Boolean) {
        val message: String = getMessage(typeMessage)
        BaseSnackbar.make(context, layout, anchorView, message, isSuccess)
    }

    private fun getMessage(typeMessage: String): String = when (typeMessage) {
        // Для списка заметок
        MSG_AUTO_BACKUP_COMPLETED -> context.getString(R.string.toast_main_auto_backup_completed)
        MSG_AUTO_BACKUP_FAILED -> context.getString(R.string.toast_main_auto_backup_failed)
        MSG_AUTO_BACKUP_NOT_POSSIBLE -> context.getString(R.string.toast_main_auto_backup_not_possible)
        MSG_NOTE_RESTORED -> context.getString(R.string.sb_main_restored)
        MSG_COMBINED_NOTE_ADD -> context.getString(R.string.sb_main_combined_note_added)
        MSG_ERROR_COMBINING_NOTES -> context.getString(R.string.sb_main_error_combining_notes)
        // Для Заметок
        MSG_TEXT_NO_PERMISSION_NOTE -> context.getString(R.string.no_permissions)
        MSG_NOTE_IS_EMPTY -> context.getString(R.string.sb_note_is_empty)
        MSG_SAVE_TXT_COMPLETED -> context.getString(R.string.sb_note_save_txt_completed)
        MSG_SAVE_TXT_FAILED -> context.getString(R.string.sb_note_save_txt_failed)
        MSG_INVALID_LINK -> context.getString(R.string.sb_note_invalid_link)
        MSG_NEED_COPY_TEXT -> context.getString(R.string.sb_note_need_copy_text)
        MSG_INVALID_FORMAT -> context.getString(R.string.sb_note_invalid_format)
        MSG_NOTE_TEXT_COPIED -> context.getString(R.string.sb_note_text_copied)
        MSG_NOTE_HYPERLINKS_COPIED -> context.getString(R.string.sb_note_hyperlinks_copied)
        // Общее
        MSG_UNABLE_CREATE_FILE -> context.getString(R.string.toast_main_auto_backup_unable_create)
        else -> context.getString(R.string.unknown_error)
    }

    fun close() {
        BaseSnackbar.dismiss()
    }
}
