package com.loskon.noteminimalism3.toast

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.showToast

/**
 * Управление тостами
 */

class ToastManager {

    companion object {

        // Для списка заметок
        const val MSG_TOAST_AUTO_BACKUP_COMPLETED = "msg_toast_auto_backup_completed"
        const val MSG_TOAST_AUTO_BACKUP_FAILED = "msg_toast_auto_backup_failed"
        const val MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE = "msg_toast_auto_backup_not_possible"
        const val MSG_TOAST_UNABLE_CREATE_FILE = "msg_toast_unable_create_file"

        const val MSG_TOAST_FILE_MANAGER_NOT_FOUND = "msg_toast_file_manager_not_found"

        const val MSG_TOAST_BAD_IDEA = "msg_toast_bad_idea"

        const val MSG_TOAST_RESTORE_LIST_EMPTY = "msg_toast_restore_list_empty"

        const val MSG_TOAST_EMAIL_CLIENT_NOT_FOUND = "msg_toast_email_client_not_found"
        const val MSG_TOAST_IMPOSSIBLE_SHARE = "msg_toast_impossible_share"


        fun show(context: Context, typeMessage: String) {
            val message: String = getMessage(context, typeMessage)
            context.showToast(message)
        }

        private fun getMessage(context: Context, typeMessage: String): String {
            context.apply {
                val message: Int = when (typeMessage) {
                    MSG_TOAST_AUTO_BACKUP_COMPLETED -> R.string.toast_main_auto_backup_completed
                    MSG_TOAST_AUTO_BACKUP_FAILED -> R.string.toast_main_auto_backup_failed
                    MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE -> R.string.toast_main_auto_backup_not_possible
                    MSG_TOAST_UNABLE_CREATE_FILE -> R.string.toast_main_auto_backup_unable_create
                    MSG_TOAST_FILE_MANAGER_NOT_FOUND -> R.string.sb_settings_file_manager_not_found
                    MSG_TOAST_BAD_IDEA -> R.string.bad_idea
                    MSG_TOAST_RESTORE_LIST_EMPTY -> R.string.dg_restore_list_empty
                    MSG_TOAST_EMAIL_CLIENT_NOT_FOUND -> R.string.email_client_not_found
                    MSG_TOAST_IMPOSSIBLE_SHARE -> R.string.sb_note_impossible_share
                    else -> R.string.unknown_error
                }

                return getString(message)
            }
        }
    }
}