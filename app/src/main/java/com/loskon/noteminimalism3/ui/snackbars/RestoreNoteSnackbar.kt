package com.loskon.noteminimalism3.ui.snackbars

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Snackbar с текстом и кнопкой
 * для восстановления заметки из корзины
 */

class RestoreNoteSnackbar(
    private val fragment: NoteTrashFragment,
    private val layout: ViewGroup,
    private val fab: View
) {

    private var snackbar: BaseDefaultSnackbar? = null

    fun show() {
        snackbar = BaseDefaultSnackbar.make(R.layout.snackbar_note_restore, layout, fab, false)
        snackbar?.settingSnackbarView()
        snackbar?.show()
    }

    private fun BaseDefaultSnackbar?.settingSnackbarView() {
        val view: View? = this?.view
        val btnSnackbar: Button? = view?.findViewById(R.id.btn_snackbar_note_reset)
        btnSnackbar?.setOnSingleClickListener { restoreNoteFromTrash() }
    }

    private fun restoreNoteFromTrash() {
        snackbar?.dismiss()
        if (snackbar != null) fragment.restoreNote()
    }
}