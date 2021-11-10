package com.loskon.noteminimalism3.toast

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.utils.showToast

/**
 * Управление тостами
 */

class ToastManager {

    companion object {

        // activity_list
        const val MSG_TOAST_AUTO_BACKUP_COMPLETED = "msg_toast_auto_backup_completed"
        const val MSG_TOAST_AUTO_BACKUP_FAILED = "msg_toast_auto_backup_failed"
        const val MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE = "msg_toast_auto_backup_not_possible"
        const val MSG_TOAST_UNABLE_CREATE_FILE = "msg_toast_unable_create_file"

        // intent ACTION_OPEN_DOCUMENT_TREE
        const val MSG_TOAST_FILE_MANAGER_NOT_FOUND = "msg_toast_file_manager_not_found"

        // sheet_list_files
        const val MSG_TOAST_RESTORE_LIST_EMPTY = "msg_toast_restore_list_empty"

        // intent ACTION_SENDTO
        const val MSG_TOAST_EMAIL_CLIENT_NOT_FOUND = "msg_toast_email_client_not_found"

        // intent ACTION_SEND
        const val MSG_TOAST_IMPOSSIBLE_SHARE = "msg_toast_impossible_share"

        fun show(context: Context, typeMessage: String) {
            val message: String = getMessage(context, typeMessage)
            context.showToast(message)
        }

        private fun getMessage(context: Context, typeMessage: String): String {
            context.apply {
                val message: Int = when (typeMessage) {
                    // activity_list
                    MSG_TOAST_AUTO_BACKUP_COMPLETED -> R.string.toast_auto_bp_completed
                    MSG_TOAST_AUTO_BACKUP_FAILED -> R.string.toast_auto_bp_failed
                    MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE -> R.string.toast_auto_bp_not_possible
                    MSG_TOAST_UNABLE_CREATE_FILE -> R.string.toast_auto_bp_unable_create
                    // intent ACTION_OPEN_DOCUMENT_TREE
                    MSG_TOAST_FILE_MANAGER_NOT_FOUND -> R.string.toast_file_manager_not_found
                    // sheet_list_files
                    MSG_TOAST_RESTORE_LIST_EMPTY -> R.string.toast_restore_list_empty
                    // intent ACTION_SENDTO
                    MSG_TOAST_EMAIL_CLIENT_NOT_FOUND -> R.string.toast_email_client_not_found
                    // intent ACTION_SEND
                    MSG_TOAST_IMPOSSIBLE_SHARE -> R.string.toast_impossible_share_note
                    else -> R.string.unknown_error
                }

                return getString(message)
            }
        }
    }
}