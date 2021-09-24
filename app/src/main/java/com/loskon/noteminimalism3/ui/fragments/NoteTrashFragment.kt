package com.loskon.noteminimalism3.ui.fragments

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
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.NoteActivityKt
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteReset
import com.loskon.noteminimalism3.utils.setFabColor
import com.loskon.noteminimalism3.utils.setButtonIconColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.viewmodel.NoteDetailViewModel

/**
 *
 */

class NoteTrashFragment : Fragment() {

    private lateinit var activityKt: NoteActivityKt
    private lateinit var viewModel: NoteDetailViewModel

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var linearLayout: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnDel: MaterialButton

    private lateinit var note: Note2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note_trash, container, false)
        constraintLayout = view.findViewById(R.id.cst)
        editText = view.findViewById(R.id.et_note_title)
        linearLayout = view.findViewById(R.id.linLytNote)
        fab = view.findViewById(R.id.fab_trash)
        btnDel = view.findViewById(R.id.btn_trash)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVar()
        setupColor()
        configureEditText()
        installNoteHandlers()
    }

    private fun initVar() {
        activityKt = requireActivity() as NoteActivityKt
        viewModel = activityKt.getViewModel
        note = activityKt.getNote
    }

    private fun setupColor() {
        val color: Int = MyColor.getMyColor(activityKt)
        fab.setFabColor(color)
        btnDel.setButtonIconColor(color)
    }

    private fun configureEditText() {
        editText.isClickable = true
        editText.isCursorVisible = false
        editText.isFocusable = false
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, activityKt.getFontSize)
        editText.setText(note.title)
    }

    private fun installNoteHandlers() {
        fab.setOnSingleClickListener { restoreNoteFromTrash() }
        btnDel.setOnSingleClickListener { deleteNoteForever() }
        linearLayout.setOnClickListener { showSnackbar() }
        editText.setOnClickListener { showSnackbar() }
    }

    fun restoreNoteFromTrash() {
        note.isDelete = false
        viewModel.update(note)
        callback?.onDeleteFromTrash(note, false)
        activityKt.onBackPressed()
    }

    private fun deleteNoteForever() {
        //viewModel.delete(note)
        callback?.onDeleteFromTrash(note, true)
        activityKt.onBackPressed()
    }

    private fun showSnackbar() {
        SnackbarNoteReset(activityKt, this).show()
    }


    // getters
    val getConstLayout: ConstraintLayout
        get() {
            return constraintLayout
        }

    val getFab: FloatingActionButton
        get() {
            return fab
        }


    // interface
    interface CallbackNoteTrash {
        fun onDeleteFromTrash(note: Note2, isDel: Boolean)
    }


    // callback and instance
    companion object {
        private var callback: CallbackNoteTrash? = null

        fun setNoteListener(callback: CallbackNoteTrash) {
            this.callback = callback
        }

        fun newInstance(): NoteTrashFragment {
            return NoteTrashFragment()
        }
    }
}
