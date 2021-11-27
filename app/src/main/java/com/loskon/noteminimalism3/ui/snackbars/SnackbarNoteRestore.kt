package com.loskon.noteminimalism3.ui.snackbars

import android.content.Context
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Восстановление заметки из корзины
 */

class SnackbarNoteRestore(
    private val context: Context,
    private val fragment: NoteTrashFragment,
) {

    private val snackbar = Snackbar.make(
        fragment.getConstLayout,
        "",
        Snackbar.LENGTH_LONG
    )

    fun show() {
        val layout: SnackbarLayout = snackbar.view as SnackbarLayout
        val view: View = View.inflate(context, R.layout.snackbar_note_restore, null)
        val btnSnackbar: Button = view.findViewById(R.id.btn_snackbar_note_reset)

        view.setOnClickListener { snackbar.dismiss() }
        layout.addView(view, 0)

        btnSnackbar.setOnSingleClickListener {
            snackbar.dismiss()
            fragment.restoreNote()
        }

        snackbar.anchorView = fragment.getFab
        snackbar.show()
    }
}