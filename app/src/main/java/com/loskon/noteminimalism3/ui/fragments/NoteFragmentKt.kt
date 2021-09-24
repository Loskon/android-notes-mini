package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.files.AutoBackup
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.other.TextNoteAssistant
import com.loskon.noteminimalism3.permissions.PermissionsInterface
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.ui.activities.NoteActivityKt
import com.loskon.noteminimalism3.ui.dialogs.DialogNoteLinks2
import com.loskon.noteminimalism3.ui.sheets.SheetCustomNoteKt
import com.loskon.noteminimalism3.ui.snackbars.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.SnackbarMessage
import com.loskon.noteminimalism3.ui.snackbars.SnackbarMessage.Companion.MSG_TEXT_NO_PERMISSION_NOTE
import com.loskon.noteminimalism3.utils.*
import com.loskon.noteminimalism3.viewmodel.NoteDetailViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class NoteFragmentKt : Fragment(),
    View.OnClickListener,
    PermissionsInterface {

    private lateinit var activity: NoteActivityKt
    private lateinit var viewModel: NoteDetailViewModel

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var linearNote: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnFav: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var btnMore: MaterialButton

    private lateinit var snackbarMessage: SnackbarMessage
    private lateinit var textAssistant: TextNoteAssistant

    private lateinit var note: Note2
    private var noteId: Long = 0L
    private var isFav: Boolean = false

    // Для работы с гиперссылками
    private var supportedLinks: Int = 0
    private var hasTextHyperlinks: Boolean = false
    private var isTextEditingMod: Boolean = false

    private val backupDate: Date = Date()
    private var isShowBackupToast: Boolean = false
    private var isNewNote: Boolean = false
    private var savedText: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as NoteActivityKt

        PermissionsStorageUpdate.installingVerification(activity, this)

        activity.onBackPressedDispatcher.addCallback(this) {
            isShowBackupToast = true
            if (isEnabled) {
                isEnabled = false
                activity.onBackPressed()
            }
        }
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

        setupNote()
    }

    private fun initVar() {
        viewModel = activity.getViewModel
        note = activity.getNote
        noteId = note.id
    }

    private fun setupColor() {
        val color: Int = MyColor.getMyColor(activity)
        fab.setFabColor(color)
        btnFav.setButtonIconColor(color)
        btnDel.setButtonIconColor(color)
        btnMore.setButtonIconColor(color)
    }

    private fun includedLinks() {
        supportedLinks = LinksUtil.getActiveLinks(activity)
        if (noteId != 0L && supportedLinks != 0) hasTextHyperlinks = true
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
        autoLinkMask = supportedLinks

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
        editText.handlerClickListener()
    }

    override fun onClick(v: View?) {
        BaseSnackbar.dismiss()

        when (v?.id) {
            R.id.fab_new_note2 -> {
                isShowBackupToast = true
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
                    val stringDate: String = DateUtils.getStringDate(note.dateModification)
                    SheetCustomNoteKt(activity, textAssistant).show(stringDate, noteId)
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

    private fun EditText.handlerClickListener() {
        setOnClickListener { BaseSnackbar.dismiss() }
    }

    private fun handlingClickOnEmptyArea() {
        BaseSnackbar.dismiss()
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
        snackbarMessage = SnackbarMessage(activity, constraintLayout, fab)
        textAssistant = TextNoteAssistant(activity, this)
    }

    private fun setupNote() {
        if (noteId == 0L) {
            isNewNote = true
        } else {
            savedText = note.title
        }
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

        if (noteId == 0L) {
            addNewNote(date)
        } else {
            updateOldNote(date)
        }

        autoBackup()
    }

    private fun addNewNote(date: Date) {
        note.dateModification = date
        note.dateCreation = date

        listener?.onNoteAdd2()

        viewModel.insertWithId(note)
        note.id = viewModel.insertedId
        noteId = note.id

        activity.showToast("add")
    }

    private fun updateOldNote(date: Date) {
        if (note.title.trim() != savedText.trim()) note.dateModification = date

        viewModel.update(note)
        activity.showToast("update")
    }

    private fun autoBackup() {
        if (noteId % 3 == 0L && isNewNote) {
            AutoBackup(activity).createBackupFile(backupDate, isShowBackupToast)
        }
    }

    val getEditText: EditText
        get() {
            return editText
        }

    val getNote: Note2
        get() {
            return note
        }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            textAssistant.mainMethodSaveTextFile()
        } else {
            showSnackbar(MSG_TEXT_NO_PERMISSION_NOTE, false)
        }
    }


    fun showSnackbar(typeMessage: String, isSuccess: Boolean) {
        snackbarMessage.show(typeMessage, isSuccess)
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