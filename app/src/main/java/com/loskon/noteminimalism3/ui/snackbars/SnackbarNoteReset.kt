package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

class SnackbarNoteReset(
    private val context: Context,
    private val trashFragment: NoteTrashFragment
) {

    private val snackbar = Snackbar.make(
        trashFragment.getConstLayout,
        "",
        Snackbar.LENGTH_LONG
    )

    fun show() {
        val layout: SnackbarLayout = snackbar.view as SnackbarLayout
        val view: View = View.inflate(context, R.layout.snackbar_note_reset, null)
        val btnSnackbar: Button = view.findViewById(R.id.snackbar_action_note_reset)
        btnSnackbar.setTextColor(MyColor.getMyColor(context))
        layout.addView(view, 0)

        btnSnackbar.setOnSingleClickListener {
            snackbar.dismiss()
            trashFragment.restoreNoteFromTrash()
        }

        snackbar.anchorView = trashFragment.getFab
        snackbar.show()
    }
}