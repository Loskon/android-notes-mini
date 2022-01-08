package com.loskon.noteminimalism3.managers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.toast.WarningToast

/**
 * Intents
 */

class IntentManager {
    companion object {
        const val PUT_EXTRA_NOTE = "put_extra_note"
        const val PUT_EXTRA_CATEGORY = "put_extra_category"
        const val PUT_EXTRA_HAS_RECEIVING_TEXT = "put_extra_has_receiving_text"

        fun openNote(context: Context, note: Note, category: String) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(PUT_EXTRA_NOTE, note)
            intent.putExtra(PUT_EXTRA_CATEGORY, category)
            context.startActivity(intent)
        }

        fun openNoteFromDialog(context: Context, note: Note, category: String) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(PUT_EXTRA_NOTE, note)
            intent.putExtra(PUT_EXTRA_CATEGORY, category)
            intent.putExtra(PUT_EXTRA_HAS_RECEIVING_TEXT, true)
            context.startActivity(intent)
        }

        fun openSettings(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }

        fun launchEmailClient(context: Context) {
            try {
                val email = context.getString(R.string.my_email)
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback_by_email))
                context.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                WarningToast.show(context, WarningToast.MSG_TOAST_EMAIL_CLIENT_NOT_FOUND)
            }
        }

        fun launchEmailClient(context: Context, link: String) {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(link)
                context.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                WarningToast.show(context, WarningToast.MSG_TOAST_EMAIL_CLIENT_NOT_FOUND)
            }
        }

        fun launchShareText(context: Context, shareText: String) {
            try {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText)
                sendIntent.type = "text/plain"
                context.startActivity(Intent.createChooser(sendIntent, "share"))
            } catch (exception: ActivityNotFoundException) {
                WarningToast.show(context, WarningToast.MSG_TOAST_IMPOSSIBLE_SHARE)
            }
        }

        fun launchWebClient(context: Context, link: String) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link)
                context.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                WarningToast.show(context, WarningToast.MSG_TOAST_WEB_CLIENT_NOT_FOUND)
            }
        }

        fun launchPhoneClient(context: Context, link: String) {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(link)
                context.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                WarningToast.show(context, WarningToast.MSG_TOAST_PHONE_CLIENT_NOT_FOUND)
            }
        }
    }
}