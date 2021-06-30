package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.note.MyLinkify
import com.loskon.noteminimalism3.auxiliary.note.TextNoteAssistant
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.other.MyDate
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsStorageKt
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsInterface
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.NewNoteActivity
import com.loskon.noteminimalism3.ui.dialogs.DialogNoteLinks2
import com.loskon.noteminimalism3.ui.sheets.SheetCustomNoteKt
import com.loskon.noteminimalism3.ui.snackbars.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage
import com.loskon.noteminimalism3.ui.snackbars.SnackbarNoteMessage.Companion.MSG_TEXT_NO_PERMISSION_NOTE
import com.loskon.noteminimalism3.utils.*
import com.loskon.noteminimalism3.viewmodel.NoteDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class NoteFragmentKt : Fragment(),
    View.OnClickListener,
    PermissionsInterface{

    private lateinit var activity: NewNoteActivity
    private lateinit var viewModel: NoteDetailViewModel

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var linearNote: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnFav: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var btnMore: MaterialButton

    private lateinit var snackbarNoteMessage: SnackbarNoteMessage
    private lateinit var textAssistant: TextNoteAssistant

    private lateinit var note: Note2
    private var noteId: Long = 0L
    private var isFav: Boolean = false

    // Для работы с гиперссылками
    private var supportedLinkTypes: Int = 0
    private var hasTextHyperlinks: Boolean = false
    private var isTextEditingMod: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        PermissionsStorageKt.installingVerification(context as ComponentActivity, this, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.note, container, false)
        constraintLayout = view.findViewById(R.id.cstLayNote22)
        linearNote = view.findViewById(R.id.linLytNote)
        editText = view.findViewById(R.id.et_note_title)
        fab = view.findViewById(R.id.fab_new_note2)
        btnFav = view.findViewById(R.id.btnFavNote)
        btnDel = view.findViewById(R.id.btnDelNote)
        btnMore = view.findViewById(R.id.btnMoreNote)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVar()
        setupColor()
        includedLinks()

        configureViews()

        initObjects()
        installNoteHandlers()
    }

    private fun initVar() {
        activity = requireActivity() as NewNoteActivity
        viewModel = activity.getViewModel
        note = activity.getNote
        noteId = note.id
    }

    private fun setupColor() {
        val color: Int = MyColor.getMyColor(activity)
        fab.setFabColor(color)
        btnFav.setIconColor(color)
        btnDel.setIconColor(color)
        btnMore.setIconColor(color)
    }

    private fun includedLinks() {
        supportedLinkTypes = MyLinkify.getTypeLinks(activity)
        if (noteId != 0L && supportedLinkTypes != 0) hasTextHyperlinks = true
    }

    private fun configureViews() {
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, activity.getFontSize)
        if (hasTextHyperlinks) editText.configureEditTextMovementMethod()
        editText.setText(note.title)
        isFav = note.isFavorite
        installFavoriteStatus()
        if (noteId != 0L) removeFocusFromEddiText()
    }

    private fun installFavoriteStatus() {
        val icon: Int = if (isFav) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }

        btnFav.icon = activity.getShortDrawable(icon)
    }

    private fun removeFocusFromEddiText() {
        linearNote.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
        editText.clearFocus()
    }

    private fun EditText.configureEditTextMovementMethod() {
        autoLinkMask = supportedLinkTypes
        setLinkTextColor(MyColor.getMyColor(activity))
        movementMethod = object : CustomMovementMethod3() {
            override fun onLinkClick(url: String) {
                DialogNoteLinks2(activity, this@NoteFragmentKt).show(url)
            }

            override fun onNoLinkClick() {
                handlingClickOnEmptyArea()
            }
        }
        isCursorVisible = false
        showSoftInputOnFocus = false
        setOnLongClickListener {
            if (!isTextEditingMod) activationTextEditingMod()
            false
        }
    }

    private fun installNoteHandlers() {
        fab.setOnClickListener(this)
        btnFav.setOnClickListener(this)
        btnDel.setOnClickListener(this)
        btnMore.setOnClickListener(this)
        linearNote.handlerOutClick()
    }

    override fun onClick(v: View?) {
        BaseSnackbar.close()

        when (v?.id) {
            R.id.fab_new_note2 -> {
                editText.hideKeyboard(activity)
                activity.onBackPressed()
            }

            R.id.btnFavNote -> {
                isFav = !isFav
                installFavoriteStatus()
            }

            R.id.btnDelNote -> {
                note.isDelete = true
                note.dateDelete = Date()
                listener?.onNoteDelete2(note, isFav)
                activity.onBackPressed()
            }

            R.id.btnMoreNote -> {
                editText.hideKeyboard(activity)
                lifecycleScope.launch {
                    delay(300L)
                    SheetCustomNoteKt(
                        activity,
                        textAssistant
                    ).show(MyDate.getNowDate(note.dateModification), noteId)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun LinearLayout.handlerOutClick() {
        setOnTouchListener { _: View?, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) handlingClickOnEmptyArea()
            false
        }
    }

    private fun handlingClickOnEmptyArea() {
        BaseSnackbar.close()
        if (hasTextHyperlinks && !isTextEditingMod) activationTextEditingMod()
        editText.showKeyboard(activity)
        editText.setSelection(editText.getLength())
        Log.d(TAG, "Click")
    }

    private fun activationTextEditingMod() {
        isTextEditingMod = true
        editText.apply {
            autoLinkMask = 0
            showSoftInputOnFocus = true
            isCursorVisible = true
            setText(note.title)
        }
    }


    private fun initObjects() {
        snackbarNoteMessage = SnackbarNoteMessage(activity, constraintLayout, fab)
        textAssistant = TextNoteAssistant(activity, this)
    }

    override fun onPause() {
        super.onPause()
        note.title = editText.text.toString()

        if (note.title.trim().isNotEmpty()) {
            saveNote()
        } else {
            viewModel.delete(note)
        }
    }

    private fun saveNote() {
        val date = Date()
        note.isFavorite = isFav
        note.dateModification = date

        if (noteId == 0L) {
            note.dateCreation = date
            listener?.onNoteAdd2()
            viewModel.insertWithId(note)
            noteId = viewModel.insertedId
        } else {
            viewModel.update(note)
        }
    }

    val getEditText: EditText
        get() {
            return editText
        }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            textAssistant.goSaveTextFile()
        } else {
            showSnackbar(MSG_TEXT_NO_PERMISSION_NOTE, false)
        }
    }



    fun showSnackbar(typeMessage: String, isSuccess: Boolean) {
        snackbarNoteMessage.show(typeMessage, isSuccess)
    }

    //
    interface OnNote2 {
        fun onNoteDelete2(note: Note2, isFav: Boolean)
        fun onNoteAdd2()
    }

    companion object {
        private val TAG = "MyLogs_${NoteFragmentKt::class.java.simpleName}"

        private var listener: OnNote2? = null

        fun setNoteListener(listener: OnNote2) {
            this.listener = listener
        }

        @JvmStatic
        fun newInstance(): NoteFragmentKt {
            return NoteFragmentKt()
        }
    }

}