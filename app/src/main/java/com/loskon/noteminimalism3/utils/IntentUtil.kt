package com.loskon.noteminimalism3.utils

import android.content.Context
import android.content.Intent
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.update.NoteActivityUpdate

class IntentUtil {
    companion object {
        const val PUT_EXTRA_NOTE = "put_extra_note"
        const val PUT_EXTRA_CATEGORY = "put_extra_category"

        fun openNote(context: Context, note: Note2, category: String) {
            val intent = Intent(context, NoteActivityUpdate::class.java)
            intent.putExtra(PUT_EXTRA_NOTE, note)
            intent.putExtra(PUT_EXTRA_CATEGORY, category)
            context.startActivity(intent)
        }
    }
}