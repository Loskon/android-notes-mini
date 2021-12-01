package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DateBaseAutoBackup
import com.loskon.noteminimalism3.command.ShortsCommandNote
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.other.LinksManager
import com.loskon.noteminimalism3.other.TextNoteAssistant
import com.loskon.noteminimalism3.request.storage.ResultAccessStorage
import com.loskon.noteminimalism3.request.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.activities.ReceivingDataActivity
import com.loskon.noteminimalism3.ui.dialogs.DialogNoteLinks
import com.loskon.noteminimalism3.ui.recyclerview.CustomMovementMethod
import com.loskon.noteminimalism3.ui.sheets.SheetTextAssistantNote
import com.loskon.noteminimalism3.ui.snackbars.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Форма для работы с текстом заметки
 */

class NoteFragment : Fragment(),
    ReceivingDataActivity.CallbackReceivingData,
    ResultAccessStorageInterface {

    private lateinit var activity: NoteActivity
    private lateinit var shortsCommand: ShortsCommandNote
    private lateinit var snackbarManager: SnackbarManager
    private lateinit var textAssistant: TextNoteAssistant

    private lateinit var constLayout: ConstraintLayout
    private lateinit var scrollView: ScrollView
    private lateinit var linLayoutNote: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnFav: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var btnMore: MaterialButton

    private lateinit var note: Note
    private lateinit var backupDate: Date

    private var isFavorite: Boolean = false
    private var isShowBackupToast: Boolean = false
    private var isTextEditingMod: Boolean = false
    private var isNewNote: Boolean = false
    private var isSaveNewNote = true
    private var isDeleteNote = false
    private var hasReceivingText: Boolean = false
    private var supportedLinks: Int = 0
    private var color: Int = 0
    private var noteId: Long = 0L
    private var savedText: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as NoteActivity
        ResultAccessStorage.installingVerification(activity, this)
        overrideBackPressed()
    }

    private fun overrideBackPressed() {
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
        val view = inflater.inflate(R.layout.fragment_note, container, false)
        constLayout = view.findViewById(R.id.const_layout_note_trash)
        scrollView = view.findViewById(R.id.scroll_view_note)
        linLayoutNote = view.findViewById(R.id.lin_layout_note)
        editText = view.findViewById(R.id.edit_text_note)
        fab = view.findViewById(R.id.fab_note)
        btnFav = view.findViewById(R.id.btn_fav_note)
        btnDel = view.findViewById(R.id.btn_del_note)
        btnMore = view.findViewById(R.id.btn_more_note)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObjects()
        setupParameters()
        installCallbacks()
        establishColorViews()
        installingSaveSettings()
        includedLinks()
        configureEditText()
        configureShowKeyboard()
        configureOtherViews()
        installNoteHandlers()
    }

    private fun initObjects() {
        note = activity.getNote()
        shortsCommand = activity.getShortsCommand()
        snackbarManager = SnackbarManager(activity, constLayout, fab)
        textAssistant = TextNoteAssistant(activity, this)
    }

    private fun setupParameters() {
        noteId = note.id
        hasReceivingText = activity.hasReceivText
    }

    private fun installCallbacks() {
        if (!hasReceivingText) ReceivingDataActivity.listenerCallback(this)
    }

    private fun establishColorViews() {
        color = activity.getColor()
        fab.setFabColor(color)
        btnFav.setButtonIconColor(color)
        btnDel.setButtonIconColor(color)
        btnMore.setButtonIconColor(color)
    }

    private fun installingSaveSettings() {
        if (noteId == 0L) {
            isNewNote = true
        } else {
            if (!hasReceivingText) {
                savedText = note.title
            }
        }
    }

    private fun includedLinks() {
        if (noteId != 0L) {
            supportedLinks = LinksManager.getActiveLinks(activity)
            if (supportedLinks != 0) editText.configureMovementMethod()
        }
    }

    private fun EditText.configureMovementMethod() {
        autoLinkMask = supportedLinks
        isCursorVisible = false
        showSoftInputOnFocus = false
        setLinkTextColor(color)

        movementMethod = object : CustomMovementMethod() {
            override fun onClickingLink(url: String) {
                removeFocusFromEditText()
                DialogNoteLinks(activity, this@NoteFragment).show(url)
            }

            override fun onClickingText() {
                handlingClickOnEmptyArea()
            }
        }

        setOnLongClickListener {
            if (!isTextEditingMod) activationTextEditingMode()
            false
        }

        clearFocus()
    }

    private fun handlingClickOnEmptyArea() {
        BaseSnackbar.dismiss()
        if (supportedLinks != 0 && !isTextEditingMod) activationTextEditingMode()
        editText.apply {
            showKeyboard(activity)
            setSelection(editText.getLength())
        }
    }

    private fun activationTextEditingMode() {
        isTextEditingMod = true
        editText.apply {
            note.title = text.toString()
            autoLinkMask = 0
            showSoftInputOnFocus = true
            isCursorVisible = true
            setText(note.title)
        }
    }

    private fun configureEditText() {
        editText.setTextSizeShort(activity.getFontSize())
        editText.setText(note.title)

        if (noteId != 0L) {
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or
                        InputType.TYPE_TEXT_FLAG_MULTI_LINE
        }
    }

    private fun configureShowKeyboard() {
        if (noteId == 0L && !hasReceivingText) {
            editText.showKeyboard(activity)
        } else if (noteId != 0L && hasReceivingText) {
            editText.scrollBottom(scrollView)
            removeFocusFromEditText()
        } else {
            removeFocusFromEditText()
        }
    }

    private fun removeFocusFromEditText() {
        linLayoutNote.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }

        editText.clearFocus()
    }

    private fun configureOtherViews() {
        isFavorite = note.isFavorite
        toggleFavoriteStatus()
    }

    private fun toggleFavoriteStatus() {
        val icon: Int = if (isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }

        btnFav.icon = activity.getShortDrawable(icon)
    }

    private fun installNoteHandlers() {
        fab.setOnSingleClickListener { clickingFab() }
        btnFav.setOnClickListener { clickingFavoriteButton() }
        btnDel.setOnSingleClickListener { clickingFavoriteDelete() }
        btnMore.setOnSingleClickListener { clickingFavoriteMore() }
        linLayoutNote.handlerOutClick()
        editText.handlerClickListener()
    }

    private fun clickingFab() {
        BaseSnackbar.dismiss()

        isShowBackupToast = true
        editText.hideKeyboard(activity)
        activity.onBackPressed()
    }

    private fun clickingFavoriteButton() {
        BaseSnackbar.dismiss()

        isFavorite = !isFavorite
        toggleFavoriteStatus()
    }

    private fun clickingFavoriteDelete() {
        BaseSnackbar.dismiss()

        isDeleteNote = true

        if (noteId == 0L) {
            shortsCommand.delete(note)
            isSaveNewNote = false
        } else {
            note.dateDelete = Date()
            note.isDelete = true
            note.isFavorite = false
            shortsCommand.update(note)
            callback?.onNoteDelete(note, isFavorite)
        }

        activity.onBackPressed()
    }

    private fun clickingFavoriteMore() {
        BaseSnackbar.dismiss()

        editText.hideKeyboard(activity)
        lifecycleScope.launch {
            delay(300L)
            val stringDate: String = DateManager.getStringDate(note.dateModification)
            SheetTextAssistantNote(activity, textAssistant).show(stringDate, noteId)
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
        setOnClickListener {
            BaseSnackbar.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSaveNewNote && !isDeleteNote) selectingMethodSavingNote()
    }

    private fun selectingMethodSavingNote() {
        val newText: String = editText.text.toString()
        note.title = newText

        if (newText.trim().isNotEmpty()) {
            savingNote(newText)
        } else {
            deleteNote()
        }
    }

    private fun savingNote(newText: String) {
        val date = Date()

        if (noteId == 0L) {
            performInsert(date)
        } else {
            performUpdate(date, newText)
        }

        performAutoBackup()
    }

    private fun performInsert(date: Date) {
        note.isFavorite = isFavorite
        note.dateModification = date
        note.dateCreation = date

        note.id = shortsCommand.insertGetId(note)
        noteId = note.id

        backupDate = date

        callback?.onNoteAdd()
    }

    private fun performUpdate(date: Date, newText: String) {
        if (newText.trim() != savedText.trim()) {
            note.isFavorite = isFavorite
            note.dateModification = date
            shortsCommand.update(note)
            callback?.onNoteUpdate()
        } else if (isFavorite != note.isFavorite) {
            note.isFavorite = isFavorite
            shortsCommand.update(note)
            callback?.onNoteUpdate()
        }
    }

    private fun performAutoBackup() {
        val isAutoBackup: Boolean = PrefManager.isAutoBackup(activity)

        if (isAutoBackup && isNewNote && noteId % 3 == 0L) {
            DateBaseAutoBackup.createBackupFile(activity, backupDate, isShowBackupToast)
        }
    }

    private fun deleteNote() {
        shortsCommand.delete(note)
        callback?.onNoteUpdate()
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            textAssistant.performSaveTextFile()
        } else {
            showSnackbar(SnackbarManager.MSG_NO_PERMISSION)
        }
    }

    fun showSnackbar(typeMessage: String) {
        snackbarManager.show(typeMessage)
    }

    val getScrollView: ScrollView
        get() {
            return scrollView
        }

    val getEditText: EditText
        get() {
            return editText
        }

    val getNote: Note
        get() {
            return note
        }

    interface CallbackNote {
        fun onNoteAdd()
        fun onNoteUpdate()
        fun onNoteDelete(note: Note, isFavorite: Boolean)
    }

    companion object {
        private var callback: CallbackNote? = null

        fun listenerCallback(callback: CallbackNote) {
            this.callback = callback
        }

        fun newInstance(): NoteFragment {
            return NoteFragment()
        }
    }

    override fun onReceivingData() {
        activity.finish()
    }
}