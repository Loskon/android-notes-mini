package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.command.ShortsCommandNote
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteReset
import com.loskon.noteminimalism3.utils.setButtonIconColor
import com.loskon.noteminimalism3.utils.setFabColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setTextSizeShort

/**
 * Работа с заметкой, находящейся в корзине
 */

class NoteTrashFragment : Fragment() {

    private lateinit var activity: NoteActivity
    private lateinit var shortsCommand: ShortsCommandNote

    private lateinit var constLayout: ConstraintLayout
    private lateinit var linearLayout: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnDel: MaterialButton

    private lateinit var note: Note

    private var color: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as NoteActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_trash_update, container, false)
        constLayout = view.findViewById(R.id.const_layout_note_up)
        editText = view.findViewById(R.id.edit_text_note_up)
        linearLayout = view.findViewById(R.id.linear_layout_note_up)
        fab = view.findViewById(R.id.fab_note_up)
        btnDel = view.findViewById(R.id.btn_delete)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObjects()
        establishColorViews()
        configureEditText()
        installHandlers()
    }

    private fun initObjects() {
        note = activity.getNote()
        shortsCommand = activity.getShortsCommand()
    }

    private fun establishColorViews() {
        color = activity.getColor()
        fab.setFabColor(color)
        btnDel.setButtonIconColor(color)
    }

    private fun configureEditText() {
        editText.apply {
            isClickable = true
            isCursorVisible = false
            isFocusable = false
            setTextSizeShort(activity.getFontSize())
            setText(note.title)
        }
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener { restoreNote() }
        btnDel.setOnSingleClickListener { deleteNote() }
        linearLayout.setOnClickListener { showSnackbar() }
        editText.setOnClickListener { showSnackbar() }
    }

    fun restoreNote() {
        note.isDelete = false
        shortsCommand.update(note)
        callback?.onNoteReset(note)
        activity.onBackPressed()
    }

    private fun deleteNote() {
        shortsCommand.delete(note)
        callback?.onNoteDelete(note, false)
        activity.onBackPressed()
    }

    private fun showSnackbar() {
        SnackbarNoteReset(activity, this).show(color)
    }

    val getConstLayout: ConstraintLayout
        get() {
            return constLayout
        }

    val getFab: FloatingActionButton
        get() {
            return fab
        }

    interface CallbackNoteTrash {
        fun onNoteDelete(note: Note, isFavorite: Boolean)
        fun onNoteReset(note: Note)
    }

    companion object {
        private var callback: CallbackNoteTrash? = null

        fun listenerCallback(callback: CallbackNoteTrash) {
            Companion.callback = callback
        }

        fun newInstance(): NoteTrashFragment {
            return NoteTrashFragment()
        }
    }
}
