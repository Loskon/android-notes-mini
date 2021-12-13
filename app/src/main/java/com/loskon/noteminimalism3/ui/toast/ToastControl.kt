package com.loskon.noteminimalism3.ui.toast

import android.content.Context
import android.widget.Toast
import com.loskon.noteminimalism3.R

/**
 * Управление Toast
 */

class ToastControl {
    companion object {

        // activity_list
        const val MSG_TOAST_AUTO_BACKUP_COMPLETED = "auto_backup_completed_msg_toast"
        const val MSG_TOAST_AUTO_BACKUP_FAILED = "auto_backup_failed_msg_toast"
        const val MSG_TOAST_AUTO_BACKUP_NOT_POSSIBLE = "auto_backup_not_possible_msg_toast"
        const val MSG_UNABLE_CREATE_FOLDER = "unable_create_folder_msg_toast"

        // intent ACTION_OPEN_DOCUMENT_TREE
        const val MSG_TOAST_FILE_MANAGER_NOT_FOUND = "file_manager_not_found_msg_toast"

        // sheet_list_files
        const val MSG_TOAST_RESTORE_LIST_EMPTY = "restore_list_empty_msg_toast"

        // intent ACTION_SENDTO
        const val MSG_TOAST_EMAIL_CLIENT_NOT_FOUND = "email_client_not_found_msg_toast"

        // intent ACTION_SEND
        const val MSG_TOAST_IMPOSSIBLE_SHARE = "impossible_share_msg_toast"

        // intent ACTION_VIEW
        const val MSG_TOAST_WEB_CLIENT_NOT_FOUND = "web_client_not_found_msg_toast"

        // intent ACTION_DIAL
        const val MSG_TOAST_PHONE_CLIENT_NOT_FOUND = "phone_client_not_found_msg_toast"

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
                    MSG_UNABLE_CREATE_FOLDER -> R.string.sb_bp_unable_created_folder
                    // intent ACTION_OPEN_DOCUMENT_TREE
                    MSG_TOAST_FILE_MANAGER_NOT_FOUND -> R.string.toast_file_manager_not_found
                    // sheet_list_files
                    MSG_TOAST_RESTORE_LIST_EMPTY -> R.string.toast_restore_list_empty
                    // intent ACTION_SENDTO
                    MSG_TOAST_EMAIL_CLIENT_NOT_FOUND -> R.string.toast_email_client_not_found
                    // intent ACTION_SEND
                    MSG_TOAST_IMPOSSIBLE_SHARE -> R.string.toast_impossible_share_note
                    // intent ACTION_VIEW
                    MSG_TOAST_WEB_CLIENT_NOT_FOUND -> R.string.toast_web_client_not_found
                    // intent ACTION_DIAL
                    MSG_TOAST_PHONE_CLIENT_NOT_FOUND -> R.string.toast_phone_client_not_found
                    else -> R.string.unknown_error
                }

                return getString(message)
            }
        }
    }
}

fun Context.showToast(message: String) {
    var toast: Toast? = null
    toast?.cancel()

    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}
