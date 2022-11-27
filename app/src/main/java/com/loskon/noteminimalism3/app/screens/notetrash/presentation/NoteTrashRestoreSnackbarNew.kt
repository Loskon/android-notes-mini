package com.loskon.noteminimalism3.app.screens.notetrash.presentation

import android.view.LayoutInflater
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.widget.snackbar.BaseSnackbar
import com.loskon.noteminimalism3.databinding.SnackbarNoteRestoreBinding
import com.loskon.noteminimalism3.model.Note

class NoteTrashRestoreSnackbarNew : BaseSnackbar() {

    private var binding: SnackbarNoteRestoreBinding? = null
    private var onCancelClickListener: ((Note) -> Unit)? = null

   // private val binding2 by

    fun make(view: View, anchorView: View, note: Note): NoteTrashRestoreSnackbarNew {
        binding = SnackbarNoteRestoreBinding.inflate(LayoutInflater.from(view.context))

        make(view, binding?.root, Snackbar.LENGTH_LONG)
        setAnchorView(anchorView)

        binding?.btnSnackbarNoteReset?.setDebounceClickListener { handleUndoClick(note) }
        setOnDismissedListener { binding = null }

        return this
    }

    private fun handleUndoClick(note: Note) {
        onCancelClickListener?.invoke(note)
        dismiss()
    }

    fun setRestoreClickListener(onCancelClickListener: ((Note) -> Unit)?) {
        this.onCancelClickListener = onCancelClickListener
    }
}