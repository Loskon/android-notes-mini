package com.loskon.noteminimalism3.ui.snackbars.update

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.update.NoteTrashFragmentUpdate
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

class SnackbarNoteResetUpdate(
    private val context: Context,
    private val fragment: NoteTrashFragmentUpdate,
) {

    private val snackbar = Snackbar.make(
        fragment.getConstLayout,
        "",
        Snackbar.LENGTH_LONG
    )

    fun show(color: Int) {
        val layout: SnackbarLayout = snackbar.view as SnackbarLayout
        val view: View = View.inflate(context, R.layout.snackbar_note_reset, null)
        val btnSnackbar: Button = view.findViewById(R.id.snackbar_action_note_reset)

        btnSnackbar.setTextColor(color)
        layout.addView(view, 0)

        btnSnackbar.setOnSingleClickListener {
            snackbar.dismiss()
            fragment.restoreNote()
        }

        snackbar.anchorView = fragment.getFab
        snackbar.show()
    }
}