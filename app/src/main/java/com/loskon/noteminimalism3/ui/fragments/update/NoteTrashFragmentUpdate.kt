package com.loskon.noteminimalism3.ui.fragments.update

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
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
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.update.NoteActivityUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarNoteResetUpdate
import com.loskon.noteminimalism3.utils.setButtonIconColor
import com.loskon.noteminimalism3.utils.setFabColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand

/**
 *
 */

class NoteTrashFragmentUpdate : Fragment() {

    private lateinit var activity: NoteActivityUpdate
    private lateinit var shortsCommand: AppShortsCommand

    private lateinit var constLayout: ConstraintLayout
    private lateinit var linearLayout: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnDel: MaterialButton

    private lateinit var note: Note2

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as NoteActivityUpdate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_trash, container, false)
        constLayout = view.findViewById(R.id.cst)
        editText = view.findViewById(R.id.et_note_title)
        linearLayout = view.findViewById(R.id.linLytNote)
        fab = view.findViewById(R.id.fab_trash)
        btnDel = view.findViewById(R.id.btn_trash)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initParameters()
        setupColor()
        configureEditText()
        installNoteHandlers()
    }

    private fun initParameters() {
        shortsCommand = activity.getShortsCommand
        note = activity.getNote
    }

    private fun setupColor() {
        val color: Int = activity.getColor
        fab.setFabColor(color)
        btnDel.setButtonIconColor(color)
    }

    private fun configureEditText() {
        editText.isClickable = true
        editText.isCursorVisible = false
        editText.isFocusable = false
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, activity.getFontSize)
        editText.setText(note.title)
    }

    private fun installNoteHandlers() {
        fab.setOnSingleClickListener { restoreNoteFromTrash() }
        btnDel.setOnSingleClickListener { deleteNoteForever() }
        linearLayout.setOnClickListener { showSnackbar() }
        editText.setOnClickListener { showSnackbar() }
    }

    fun restoreNoteFromTrash() {
        //note.isDelete = false
        shortsCommand.update(note)
        //callback?.onDeleteFromTrash(note, false)
        activity.onBackPressed()
    }

    private fun deleteNoteForever() {
        shortsCommand.delete(note)
        //callback?.onDeleteFromTrash(note, true)
        activity.onBackPressed()
    }

    private fun showSnackbar() {
        SnackbarNoteResetUpdate(activity, this).show()
    }

    val getConstLayout: ConstraintLayout
        get() {
            return constLayout
        }

    val getFab: FloatingActionButton
        get() {
            return fab
        }

    interface CallbackNoteTrashUpdate {
        fun onNoteDeleteFromTrash(note: Note2, isDel: Boolean)
    }

    companion object {
        private var callback: CallbackNoteTrashUpdate? = null

        fun callbackNoteTrashListener(callback: CallbackNoteTrashUpdate) {
            this.callback = callback
        }

        fun newInstance(): NoteTrashFragmentUpdate {
            return NoteTrashFragmentUpdate()
        }
    }
}
