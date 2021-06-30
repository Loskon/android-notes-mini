package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R

/**
 *
 */

class SnackbarNoteMessage(
    private val context: Context,
    private val constraintLayout: ConstraintLayout,
    private val fab: FloatingActionButton
) {

    companion object {
        const val MSG_TEXT_NO_PERMISSION_NOTE = "msg_no_permission"
        const val MSG_NOTE_IS_EMPTY = "msg_note_is_empty"
        const val MSG_SAVE_TXT_COMPLETED = "msg_save_txt_completed"
        const val MSG_SAVE_TXT_FAILED = "msg_save_txt_failed"
        const val MSG_INVALID_LINK = "msg_invalid_link"
        const val MSG_NEED_COPY_TEXT = "msg_need_copy_text"
        const val MSG_INVALID_FORMAT = "msg_invalid_format"
        const val MSG_NOTE_TEXT_COPIED = "msg_text_copied"
        const val MSG_NOTE_HYPERLINKS_COPIED = "msg_hyperlinks_copied"
        const val MSG_ERROR = "msg_error"
    }

    fun show(typeMessage: String, isSuccess: Boolean) {
        val message: String = getMessage(typeMessage)

        BaseSnackbar.makeSnackbar(
            context,
            constraintLayout,
            message,
            fab,
            isSuccess
        )
    }

    private fun getMessage(typeMessage: String): String = when (typeMessage) {

        MSG_TEXT_NO_PERMISSION_NOTE -> context.getString(R.string.no_permissions)
        MSG_NOTE_IS_EMPTY -> context.getString(R.string.sb_note_is_empty)
        MSG_SAVE_TXT_COMPLETED -> context.getString(R.string.sb_note_save_txt_completed)
        MSG_SAVE_TXT_FAILED -> context.getString(R.string.sb_note_save_txt_failed)
        MSG_INVALID_LINK -> context.getString(R.string.sb_note_invalid_link)
        MSG_NEED_COPY_TEXT -> context.getString(R.string.sb_note_need_copy_text)
        MSG_INVALID_FORMAT -> context.getString(R.string.sb_note_invalid_format)
        MSG_NOTE_TEXT_COPIED -> context.getString(R.string.sb_note_text_copied)
        MSG_NOTE_HYPERLINKS_COPIED -> context.getString(R.string.sb_note_hyperlinks_copied)

        else -> context.getString(R.string.unknown_error)
    }
}
