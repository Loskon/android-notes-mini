package com.loskon.noteminimalism3.app.screens.notetrash.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.widget.snackbar.BaseSnackbar
import com.loskon.noteminimalism3.databinding.SnackbarNoteRestoreBinding
import com.loskon.noteminimalism3.model.Note

class NoteTrashRestoreSnackbar(
    private val context: Context,
    private val view: View,
    private val anchorView: View
) : BaseSnackbar() {

    private var binding: SnackbarNoteRestoreBinding? = null
    private var onCancelClickListener: ((Note) -> Unit)? = null

    fun make(note: Note): NoteTrashRestoreSnackbar {
        binding = SnackbarNoteRestoreBinding.inflate(LayoutInflater.from(context))

        make(view, binding?.root)
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