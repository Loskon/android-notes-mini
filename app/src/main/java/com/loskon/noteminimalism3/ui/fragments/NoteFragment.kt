package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewmodel.NoteViewModel

/**
 *
 */

class NoteFragment : Fragment(), View.OnClickListener {

    private lateinit var activity: ListActivity

    private lateinit var widgetsHelper: WidgetHelperList
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar

    private lateinit var viewModel: NoteViewModel

    // fragment
    private lateinit var linearNote: LinearLayout
    private lateinit var editText: EditText
    private lateinit var btnFav: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var btnMore: MaterialButton

    private var note: Note2 = Note2()

    private var fontSizeNote: Float = 0f

    private var id: Long = 0
    private var isFavorite: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getSharedPreferences(context)
        getNoteArguments()
    }

    private fun getSharedPreferences(context: Context) {
        fontSizeNote = GetSharedPref.getFontSizeNote(context).toFloat()
    }

    private fun getNoteArguments() {
        arguments?.getParcelable<Note2>(ARG_NOTE)?.let {
            note = it
            id = note.id
            isFavorite = note.isFavorite
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        initView(view)
        installNoteHandlers()
        favoriteStatus()
        settingRequiredValues()
        applyingStartSettings()
        return view
    }

    private fun initView(view: View) {
        editText = view.findViewById(R.id.et_note_title)
        linearNote = view.findViewById(R.id.linLytNote)
        btnFav = view.findViewById(R.id.btnFavNote)
        btnDel = view.findViewById(R.id.btnDelNote)
        btnMore = view.findViewById(R.id.btnMoreNote)
    }

    private fun installNoteHandlers() {
        btnFav.setOnClickListener(this)
        btnDel.setOnClickListener(this)
        btnMore.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnFavNote -> {
                isFavorite = !isFavorite
                favoriteStatus()
            }

            R.id.btnDelNote -> {
                note.isDelete = true
                //viewModel.update(note)
                activity.onBackPressed()
                listener?.onNoteDelete(note)
            }

            R.id.btnMoreNote -> {

            }

        }
    }

    private fun favoriteStatus() {
        val icon: Int = if (isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }

        btnFav.icon = context?.getShortDrawable(icon)
    }

    private fun settingRequiredValues() {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNote)
    }

    private fun applyingStartSettings() {
        if (id == 0L) {
            createNewNote()
        } else {
            existingOldNote()
        }

        handlerOutClick()
    }

    private fun existingOldNote() {
        editText.setText(note.title)
    }

    private fun createNewNote() {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handlerOutClick() {
        linearNote.setOnTouchListener { _, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                eventOutClick()
            }
            false
        }
    }

    private fun eventOutClick() {
        showSoftKeyboard()
        editText.setSelection(editText.text.toString().trim().length)
    }

    private fun showSoftKeyboard() {
        editText.showKeyboard(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as ListActivity
        viewModel = activity.getViewModel

        initWidgets()
        installHandlers()
    }

    private fun initWidgets() {
        widgetsHelper = activity.getWidgetsHelper
        fab = widgetsHelper.getFab
        bottomAppBar = widgetsHelper.getBottomAppBar

        widgetsHelper.setVisibleWidgets(false)

        showSoftKeyboard()
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            note.title = editText.text.toString()
            note.isFavorite = isFavorite

            if (id == 0L) {
                listener?.onNoteAdd(note)
                viewModel.insert(note)
            } else {
                //viewModel.update(note)
            }


            activity.supportFragmentManager.popBackStack()
        }

        bottomAppBar.setOnSingleClickListener {
            activity.supportFragmentManager.popBackStack()
        }
    }



    interface OnNote {
        fun onNoteDelete(note: Note2)
        fun onNoteAdd(note: Note2)
    }

    companion object {
        private var listener: OnNote? = null

        fun setNoteListener(listener: OnNote) {
            this.listener = listener
        }

        private const val ARG_NOTE = "arg_note"

        @JvmStatic
        fun newInstance(note: Note2) = NoteFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_NOTE, note)
            }
        }
    }
}