package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import com.loskon.noteminimalism3.backup.DataBaseAutoBackup
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.managers.LinksManager
import com.loskon.noteminimalism3.managers.setButtonIconColor
import com.loskon.noteminimalism3.managers.setFabColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.other.NoteAssistant
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorage
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.activities.ReceivingDataActivity
import com.loskon.noteminimalism3.ui.dialogs.NoteLinksDialog
import com.loskon.noteminimalism3.ui.recyclerview.AppMovementMethod
import com.loskon.noteminimalism3.ui.sheets.NoteAssistantSheetDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningBaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Экран для работы с заметкой
 */

open class NoteFragment : Fragment(),
    ReceivingDataActivity.ReceivingDataCallback,
    ResultAccessStorageInterface {

    private lateinit var activity: NoteActivity
    private lateinit var assistant: NoteAssistant

    private val commandCenter: CommandCenter = CommandCenter()

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
    private var isNewNote: Boolean = true
    private var isSaveNewNote: Boolean = true
    private var isDeleteNote: Boolean = false

    private var isShowBackupToast: Boolean = false
    private var hasShowUndoSnackbar: Boolean = true

    private var hasTextEditingMode: Boolean = false
    private var hasReceivingText: Boolean = false

    private var supportedLinks: Int = 0
    private var color: Int = 0
    private var noteId: Long = 0L
    private var savedText: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as NoteActivity
        ResultAccessStorage.installing(activity, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hasShowUndoSnackbar = PrefHelper.isBottomWidgetShow(activity)
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
        constLayout = view.findViewById(R.id.const_layout_note)
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
        establishViewsColor()
        initializingObjects()
        getNecessaryParameters()
        installCallbacks()
        configureParametersForSavingNote()
        checkingForIncludedLinks()
        configureEditText()
        configureShowKeyboard()
        configureOtherViews()
        installNoteHandlers()
    }

    private fun establishViewsColor() {
        color = activity.getColor()
        fab.setFabColor(color)
        btnFav.setButtonIconColor(color)
        btnDel.setButtonIconColor(color)
        btnMore.setButtonIconColor(color)
    }

    private fun initializingObjects() {
        note = activity.getNote()
        assistant = NoteAssistant(activity, this, editText, scrollView)
    }

    private fun getNecessaryParameters() {
        noteId = note.id
        hasReceivingText = activity.hasRecText
    }

    private fun installCallbacks() {
        if (!hasReceivingText) ReceivingDataActivity.registerCallbackReceivingData(this)
    }

    private fun configureParametersForSavingNote() {
        if (noteId != 0L) {
            isNewNote = false
            if (!hasReceivingText) savedText = note.title
        }
    }

    private fun checkingForIncludedLinks() {
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

        movementMethod = object : AppMovementMethod() {
            override fun onClickingLink(url: String) {
                removeFocusFromEditText()
                NoteLinksDialog(activity, this@NoteFragment).show(url)
            }

            override fun onClickingText() {
                handlingClickOnEmptyArea()
            }
        }

        setOnLongClickListener {
            if (!hasTextEditingMode) activationTextEditingMode()
            false
        }

        clearFocus()
    }

    private fun handlingClickOnEmptyArea() {
        WarningBaseSnackbar.dismiss()
        if (supportedLinks != 0 && !hasTextEditingMode) activationTextEditingMode()
        editText.showKeyboard(activity)
        editText.setSelection(editText.getLength())
    }

    private fun activationTextEditingMode() {
        hasTextEditingMode = true

        editText.disableLinks()
        editText.setText(editText.text.toString())
    }

    private fun configureEditText() {
        editText.changeTextSize(activity.getNoteFontSize())
        editText.setText(note.title)
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
        linLayoutNote.turnFocusOnYourself()
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
        fab.setOnSingleClickListener { onFabClick() }
        btnFav.setOnClickListener { onFavoriteBtnClick() }
        btnDel.setOnSingleClickListener { onDeleteBtnClick() }
        btnMore.setOnSingleClickListener { onMoreBtnClick() }
        linLayoutNote.handlerOutClick()
        editText.handlerClickListener()
    }

    private fun onFabClick() {
        WarningBaseSnackbar.dismiss()

        isShowBackupToast = true
        editText.hideKeyboard(activity)
        activity.onBackPressed()
    }

    private fun onFavoriteBtnClick() {
        WarningBaseSnackbar.dismiss()

        isFavorite = !isFavorite
        toggleFavoriteStatus()
    }

    private fun onDeleteBtnClick() {
        WarningBaseSnackbar.dismiss()

        isDeleteNote = true

        if (noteId == 0L) {
            commandCenter.delete(note)
            isSaveNewNote = false
        } else {
            commandCenter.sendToTrash(note)
            callback?.onSendToTrash(note, isFavorite, hasShowUndoSnackbar)
        }

        activity.onBackPressed()
    }

    private fun onMoreBtnClick() {
        WarningBaseSnackbar.dismiss()

        editText.hideKeyboard(activity)
        lifecycleScope.launch {
            delay(300L)
            val stringDate: String = DateUtil.getStringDate(note.dateModification)
            NoteAssistantSheetDialog(activity, assistant).show(stringDate, noteId)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun LinearLayout.handlerOutClick() {
        setOnTouchListener { _, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) handlingClickOnEmptyArea()
            false
        }
    }

    private fun EditText.handlerClickListener() {
        setOnClickListener {
            WarningBaseSnackbar.dismiss()
        }
    }

    override fun onPause() {
        if (isSaveNewNote && !isDeleteNote) selectingMethodSavingNote()
        super.onPause()
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

        note.id = commandCenter.insertWithIdReturn(note)
        noteId = note.id

        backupDate = date
        callback?.onNoteAdd()
    }

    private fun performUpdate(date: Date, newText: String) {
        if (newText.trim() != savedText.trim()) {
            note.isFavorite = isFavorite
            note.dateModification = date
            commandCenter.update(note)
            callback?.onNoteUpdate()
        } else if (isFavorite != note.isFavorite) {
            note.isFavorite = isFavorite
            commandCenter.update(note)
            callback?.onNoteUpdate()
        }
    }

    private fun performAutoBackup() {
        val isAutoBackup: Boolean = PrefHelper.hasAutoBackup(activity)

        if (isAutoBackup && isNewNote && noteId % 3 == 0L) {
            DataBaseAutoBackup.startCreatingBackup(activity, backupDate, isShowBackupToast)
        }
    }

    private fun deleteNote() {
        commandCenter.delete(note)
        callback?.onNoteUpdate()
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            assistant.performSaveTextFile()
        } else {
            showSnackbar(WarningSnackbar.MSG_NO_PERMISSION)
        }
    }

    fun showSnackbar(messageType: String) = WarningSnackbar.show(constLayout, fab, messageType)

    interface NoteCallback {
        fun onNoteAdd()
        fun onNoteUpdate()
        fun onSendToTrash(note: Note, hasFavStatus: Boolean, isBottomWidgetShow: Boolean)
    }

    companion object {
        private var callback: NoteCallback? = null

        fun registerCallbackNote(callback: NoteCallback) {
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

// Extension functions
private fun EditText.getLength(): Int {
    return text.toString().length
}

private fun EditText.disableLinks() {
    autoLinkMask = 0
    showSoftInputOnFocus = true
    isCursorVisible = true
}

private fun View.turnFocusOnYourself() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
}