package com.loskon.noteminimalism3.ui.fragments.update

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.AutoBackup
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.other.CustomMovementMethodUpdate
import com.loskon.noteminimalism3.other.TextNoteAssistantUpdate
import com.loskon.noteminimalism3.permissions.PermissionsInterface
import com.loskon.noteminimalism3.permissions.PermissionsStorageUpdate
import com.loskon.noteminimalism3.sqlite.AppShortsCommand
import com.loskon.noteminimalism3.ui.activities.update.NoteActivityUpdate
import com.loskon.noteminimalism3.ui.dialogs.DialogNoteLinksUpdate
import com.loskon.noteminimalism3.ui.sheets.update.SheetNoteUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Работа с заметкой
 */

class NoteFragmentUpdate : Fragment(),
    View.OnClickListener,
    PermissionsInterface {

    private lateinit var activity: NoteActivityUpdate
    private lateinit var shortsCommand: AppShortsCommand
    private lateinit var snackbarApp: SnackbarApp
    private lateinit var textAssistant: TextNoteAssistantUpdate

    private lateinit var constLayout: ConstraintLayout
    private lateinit var linLayoutNote: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnFav: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var btnMore: MaterialButton

    private lateinit var note: Note2
    private lateinit var backupDate: Date

    private var isFavorite: Boolean = false
    private var isShowBackupToast: Boolean = false
    private var isTextEditingMod: Boolean = false
    private var supportedLinks: Int = 0
    private var isNewNote: Boolean = false
    private var color: Int = 0
    private var noteId: Long = 0L
    private var savedText: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as NoteActivityUpdate
        PermissionsStorageUpdate.installingVerification(activity, this)
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
        val view = inflater.inflate(R.layout.note_update, container, false)
        constLayout = view.findViewById(R.id.const_layout_note_up)
        linLayoutNote = view.findViewById(R.id.linear_layout_note_up)
        editText = view.findViewById(R.id.edit_text_note_up)
        fab = view.findViewById(R.id.fab_note_up)
        btnFav = view.findViewById(R.id.btn_fav_note_up)
        btnDel = view.findViewById(R.id.btn_del_note_up)
        btnMore = view.findViewById(R.id.btn_more_note_up)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObjects()
        setupParameters()
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
        snackbarApp = SnackbarApp(activity, constLayout, fab)
        textAssistant = TextNoteAssistantUpdate(activity, this)
    }

    private fun setupParameters() {
        noteId = note.id
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
            savedText = note.title
        }
    }

    private fun includedLinks() {
        if (noteId != 0L) {
            supportedLinks = LinksUtil.getActiveLinks(activity)
            if (supportedLinks != 0) editText.configureMovementMethod()
        }
    }

    private fun EditText.configureMovementMethod() {
        autoLinkMask = supportedLinks
        isCursorVisible = false
        showSoftInputOnFocus = false
        setLinkTextColor(color)

        movementMethod = object : CustomMovementMethodUpdate() {
            override fun onClickingLink(url: String) {
                DialogNoteLinksUpdate(activity, this@NoteFragmentUpdate).show(url)
            }

            override fun onClickingText() {
                handlingClickOnEmptyArea()
            }
        }

        setOnLongClickListener {
            if (!isTextEditingMod) activationTextEditingMode()
            false
        }
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
        editText.setFontSize(activity.getFontSize())
        if (noteId != 0L) editText.setText(note.title)
    }

    private fun configureShowKeyboard() {
        if (noteId == 0L) {
            editText.showKeyboard(activity)
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
        fab.setOnClickListener(this)
        btnFav.setOnClickListener(this)
        btnDel.setOnClickListener(this)
        btnMore.setOnClickListener(this)
        linLayoutNote.handlerOutClick()
        editText.handlerClickListener()
    }

    override fun onClick(v: View?) {
        BaseSnackbar.dismiss()

        when (v?.id) {
            R.id.fab_note_up -> clickingFab()

            R.id.btn_fav_note_up -> clickingFavoriteButton()

            R.id.btn_del_note_up -> clickingFavoriteDelete()

            R.id.btn_more_note_up -> clickingFavoriteMore()
        }
    }

    private fun clickingFab() {
        isShowBackupToast = true
        editText.hideKeyboard(activity)
        activity.onBackPressed()
    }

    private fun clickingFavoriteButton() {
        isFavorite = !isFavorite
        toggleFavoriteStatus()
    }

    private fun clickingFavoriteDelete() {
        note.dateDelete = Date()
        note.isDelete = true
        note.isFavorite = false
        shortsCommand.update(note)
        callback?.onNoteDelete(note, isFavorite)
        activity.onBackPressed()
    }

    private fun clickingFavoriteMore() {
        editText.hideKeyboard(activity)
        lifecycleScope.launch {
            delay(300L)
            val stringDate: String = DateUtils.getStringDate(note.dateModification)
            SheetNoteUpdate(activity, textAssistant).show(stringDate, noteId)
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
        selectingMethodSavingNote()
    }

    private fun selectingMethodSavingNote() {
        val newText: String = editText.text.toString()
        note.title = newText

        if (newText.trim().isNotEmpty()) {
            savingNote(newText)
        } else {
            removeNote()
        }
    }

    private fun savingNote(newText: String) {
        val date = Date()

        if (noteId == 0L) {
            insertNote(date)
        } else {
            updateNote(date, newText)
        }

        callAutoBackup()
    }

    private fun insertNote(date: Date) {
        note.isFavorite = isFavorite
        note.dateModification = date
        note.dateCreation = date

        note.id = shortsCommand.insertGetId(note)
        noteId = note.id

        backupDate = date

        callback?.onNoteAdd()
    }

    private fun updateNote(date: Date, newText: String) {
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

    private fun callAutoBackup() {
        if (isNewNote && noteId % 3 == 0L) {
            AutoBackup(activity).createBackupFile(backupDate, isShowBackupToast)
        }
    }

    private fun removeNote() {
        shortsCommand.delete(note)
        callback?.onNoteUpdate()
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
            showSnackbar(SnackbarApp.MSG_NO_PERMISSION, false)
        }
    }

    fun showSnackbar(typeMessage: String, isSuccess: Boolean) {
        snackbarApp.show(typeMessage, isSuccess)
    }

    interface CallbackNoteUpdate {
        fun onNoteAdd()
        fun onNoteUpdate()
        fun onNoteDelete(note: Note2, isFavorite: Boolean)
    }

    companion object {
        //private val TAG = "MyLogs_${NoteFragmentUpdate::class.java.simpleName}"

        private var callback: CallbackNoteUpdate? = null

        fun listenerCallback(callback: CallbackNoteUpdate) {
            this.callback = callback
        }

        fun newInstance(): NoteFragmentUpdate {
            return NoteFragmentUpdate()
        }
    }
}