package com.loskon.noteminimalism3.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.update.NoteActivityUpdate
import com.loskon.noteminimalism3.ui.activities.update.ReceivingDataActivity
import com.loskon.noteminimalism3.ui.activities.update.SettingsActivityUpdate

class IntentUtil {
    companion object {
        const val PUT_EXTRA_NOTE = "put_extra_note"
        const val PUT_EXTRA_CATEGORY = "put_extra_category"
        const val PUT_EXTRA_RECEIVING_TEXT = "put_extra_receiving_text"
        const val PUT_EXTRA_HAS_RECEIVING_TEXT = "put_extra_has_receiving_text"


        fun openNote(context: Context, note: Note2, notesCategory: String) {
            val intent = Intent(context, NoteActivityUpdate::class.java)
            intent.putExtra(PUT_EXTRA_NOTE, note)
            intent.putExtra(PUT_EXTRA_CATEGORY, notesCategory)
            context.startActivity(intent)
        }

        fun openNoteFromDialog(context: Context, note: Note2, notesCategory: String) {
            val intent = Intent(context, NoteActivityUpdate::class.java)
            intent.putExtra(PUT_EXTRA_NOTE, note)
            intent.putExtra(PUT_EXTRA_CATEGORY, notesCategory)
            intent.putExtra(PUT_EXTRA_HAS_RECEIVING_TEXT, true)
            context.startActivity(intent)
        }

        fun openSettings(context: Context) {
            val intent = Intent(context, SettingsActivityUpdate::class.java)
            context.startActivity(intent)
        }

        fun openListNotes(context: Context, receivingText: String) {
            val intent = Intent(context, ReceivingDataActivity::class.java)
            intent.putExtra(PUT_EXTRA_RECEIVING_TEXT, receivingText)
            context.startActivity(intent)
        }

        fun launcherEmailClient(context: Context) {
            try {
                val email = context.getString(R.string.my_email)
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback))
                context.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                context.showToast(R.string.email_client_not_found)
            }
        }

        fun launcherShareText(context: Context, editText: EditText) {
            try {
                val sendIntent = Intent()
                sendIntent.action = Intent.ACTION_SEND
                sendIntent.putExtra(Intent.EXTRA_TEXT, editText.text.toString().trim())
                sendIntent.type = "text/plain"
                context.startActivity(Intent.createChooser(sendIntent, "share"))
            } catch (exception: ActivityNotFoundException) {
                context.showToast(R.string.sb_note_impossible_share)
            }
        }
    }
}