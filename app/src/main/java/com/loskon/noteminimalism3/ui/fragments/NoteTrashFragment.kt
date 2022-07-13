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
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.managers.setButtonIconColor
import com.loskon.noteminimalism3.managers.setFabColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.snackbars.RestoreNoteSnackbar
import com.loskon.noteminimalism3.utils.changeTextSize
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import java.util.*

/**
 * Работа с заметкой, находящейся в корзине
 */

class NoteTrashFragment : Fragment() {

    private val commandCenter: CommandCenter = CommandCenter()

    private lateinit var activity: NoteActivity
    private lateinit var noteSnackbar: RestoreNoteSnackbar

    private lateinit var constLayout: ConstraintLayout
    private lateinit var linearLayout: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnDel: MaterialButton

    private lateinit var note: Note

    private var hasUpdateDateTime: Boolean = true
    private var color: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as NoteActivity
        hasUpdateDateTime = AppPreference.hasUpdateDateTime(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_trash, container, false)
        constLayout = view.findViewById(R.id.const_layout_note_trash)
        editText = view.findViewById(R.id.edit_text_note)
        linearLayout = view.findViewById(R.id.lin_layout_note)
        fab = view.findViewById(R.id.fab_note_trash)
        btnDel = view.findViewById(R.id.btn_del_note_trash)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObjects()
        establishViewsColor()
        configureEditText()
        setupViewsListeners()
    }

    private fun initObjects() {
        note = activity.getNote()
        noteSnackbar = RestoreNoteSnackbar(this, constLayout, fab)
    }

    private fun establishViewsColor() {
        color = activity.getColor()
        fab.setFabColor(color)
        btnDel.setButtonIconColor(color)
    }

    private fun configureEditText() {
        editText.disableFocus()
        editText.changeTextSize(activity.getNoteFontSize())
        editText.setText(note.title)

    }

    private fun setupViewsListeners() {
        fab.setDebounceClickListener { restoreNote() }
        btnDel.setDebounceClickListener { deleteNote() }
        linearLayout.setOnClickListener { showSnackbar() }
        editText.setOnClickListener { showSnackbar() }
    }

    fun restoreNote() {
        note.isDeleted = false
        if (hasUpdateDateTime) note.dateCreation = Date()
        commandCenter.update(note)
        callback?.onRestoreNote(note)
        activity.onBackPressed()
    }

    private fun deleteNote() {
        commandCenter.delete(note)
        callback?.onDeleteNote(note, false)
        activity.onBackPressed()
    }

    private fun showSnackbar() {
        noteSnackbar.show()
    }

    //--- interface ---------------------------------------------------------------------------------
    interface NoteTrashCallback {
        fun onDeleteNote(note: Note, hasFavStatus: Boolean)
        fun onRestoreNote(note: Note)
    }

    companion object {
        private var callback: NoteTrashCallback? = null

        fun registerNoteTrashCallback(callback: NoteTrashCallback) {
            Companion.callback = callback
        }

        fun newInstance(): NoteTrashFragment {
            return NoteTrashFragment()
        }
    }
}

// Extension functions
private fun EditText.disableFocus() {
    isClickable = true
    isCursorVisible = false
    isFocusable = false
}
