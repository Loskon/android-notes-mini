package com.loskon.noteminimalism3.toast

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.showToast

class ToastApp(private val context: Context) {
    companion object {
        // Для списка заметок
        const val MSG_TOAST_AUTO_BACKUP_COMPLETED = "msg_toast_auto_backup_completed"
        const val MSG_TOAST_AUTO_BACKUP_FAILED = "msg_toast_auto_backup_failed"
        const val MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE = "msg_toast_auto_backup_not_possible"
        const val MSG_TOAST_UNABLE_CREATE_FILE = "msg_toast_unable_create_file"
    }

    fun show(typeMessage: String) {
        val message: String = getMessage(typeMessage)
        context.showToast(message)
    }

    private fun getMessage(typeMessage: String): String = when (typeMessage) {
        MSG_TOAST_AUTO_BACKUP_COMPLETED -> context.getString(R.string.toast_main_auto_backup_completed)
        MSG_TOAST_AUTO_BACKUP_FAILED -> context.getString(R.string.toast_main_auto_backup_failed)
        MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE -> context.getString(R.string.toast_main_auto_backup_not_possible)
        MSG_TOAST_UNABLE_CREATE_FILE -> context.getString(R.string.toast_main_auto_backup_unable_create)
        else -> context.getString(R.string.unknown_error)
    }
}