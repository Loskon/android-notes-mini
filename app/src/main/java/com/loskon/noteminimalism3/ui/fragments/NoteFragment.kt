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
import com.loskon.noteminimalism3.requests.storage.ResultAccessStorageInterface
import com.loskon.noteminimalism3.requests.storage.ResultStorageAccess
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.activities.NoteActivity
import com.loskon.noteminimalism3.ui.activities.ReceivingDataActivity
import com.loskon.noteminimalism3.ui.dialogs.NoteLinkDialog
import com.loskon.noteminimalism3.ui.recyclerview.AppMovementMethod
import com.loskon.noteminimalism3.ui.sheetdialogs.NoteAssistantSheetDialog
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
    private lateinit var storageAccess: ResultStorageAccess
    private lateinit var assistant: NoteAssistant

    private val commandCenter: CommandCenter = CommandCenter()

    private lateinit var constLayout: ConstraintLayout
    private lateinit var scrollView: ScrollView
    private lateinit var linLayout: LinearLayout
    private lateinit var editText: EditText
    private lateinit var fab: FloatingActionButton
    private lateinit var btnFav: MaterialButton
    private lateinit var btnDel: MaterialButton
    private lateinit var btnMore: MaterialButton

    private lateinit var note: Note
    private lateinit var backupDate: Date

    private var isFavorite: Boolean = false
    private var isNewNote: Boolean = true
    private var isDeleteNote: Boolean = false
    private var hasReceivingText: Boolean = false
    private var hasShowToast: Boolean = false
    private var hasTextEditingMode: Boolean = false

    private var supportedLinks: Int = 0
    private var color: Int = 0
    private var noteId: Long = 0L
    private var savedText: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as NoteActivity
        configureRequestPermissions()
    }

    private fun configureRequestPermissions() {
        storageAccess = ResultStorageAccess(activity, this, this)
        storageAccess.installingContracts()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installCallbacks()
        overrideBackPressed()
    }

    private fun installCallbacks() {
        if (!hasReceivingText) ReceivingDataActivity.registerReceivingDataCallback(this)
    }

    private fun overrideBackPressed() {
        activity.onBackPressedDispatcher.addCallback(this) {
            hasShowToast = true
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
        linLayout = view.findViewById(R.id.lin_layout_note)
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
        getCurrentNote()
        getRequiredVariableValues()
        changingValueVariablesForUpdateNote()
        checkingForIncludedLinks()
        configureEditText()
        configureShowKeyboard()
        changeIconFavoriteBtn()
        setupViewsListeners()
    }

    private fun establishViewsColor() {
        color = activity.getColor()
        fab.setFabColor(color)
        btnFav.setButtonIconColor(color)
        btnDel.setButtonIconColor(color)
        btnMore.setButtonIconColor(color)
    }

    private fun initializingObjects() {
        assistant = NoteAssistant(activity, this, storageAccess, editText, scrollView)
    }

    private fun getCurrentNote() {
        note = activity.getNote()
    }

    private fun getRequiredVariableValues() {
        noteId = note.id
        isFavorite = note.isFavorite
        hasReceivingText = activity.hasReceivingText()
    }

    private fun changingValueVariablesForUpdateNote() {
        if (noteId != 0L) {
            isNewNote = false
            if (!hasReceivingText) savedText = note.title
        }
    }

    private fun checkingForIncludedLinks() {
        if (noteId != 0L) supportedLinks = LinksManager.getActiveLinks(activity)
    }


    private fun configureEditText() {
        if (supportedLinks != 0) preparingToWorkWithLinks()
        editText.changeTextSize(activity.getNoteFontSize())
        editText.setText(note.title)
    }

    private fun preparingToWorkWithLinks() {
        editText.changeWorkWithLinks(supportedLinks)
        editText.setLinkTextColor(color)
        editText.setOnLinkClickListener()
    }

    private fun EditText.setOnLinkClickListener() {
        movementMethod = object : AppMovementMethod() {
            override fun onLinkClick(url: String) {
                removeFocusFromEditText()
                NoteLinkDialog(this@NoteFragment).show(url)
            }

            override fun onTextClick() {
                onEmptyAreaClick()
            }
        }
    }

    private fun removeFocusFromEditText() {
        linLayout.turnFocusOnYourself()
        editText.clearFocus()
    }

    private fun View.turnFocusOnYourself() {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
    }

    private fun onEmptyAreaClick() {
        WarningSnackbar.dismiss()
        if (supportedLinks != 0) activatingTextEditingMode()
        editText.setSelection(editText.text.toString().length)
        editText.showKeyboard(activity)
    }

    private fun activatingTextEditingMode() {
        if (!hasTextEditingMode) {
            hasTextEditingMode = true
            editText.changeWorkWithLinks(0)
            editText.setText(editText.text.toString())
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

    private fun changeIconFavoriteBtn() {
        val icon: Int = if (note.isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }

        btnFav.icon = activity.getShortDrawable(icon)
    }

    private fun setupViewsListeners() {
        fab.setOnSingleClickListener { onFabClick() }
        btnFav.setOnClickListener { onFavoriteBtnClick() }
        btnDel.setOnSingleClickListener { onDeleteBtnClick() }
        btnMore.setOnSingleClickListener { onMoreBtnClick() }
        editText.setOnClickListener { WarningSnackbar.dismiss() }
        linLayout.setOnLinLayoutClickListener()
    }

    private fun onFabClick() {
        completeWorkWithNote()
    }

    private fun completeWorkWithNote() {
        hasShowToast = true
        editText.hideKeyboard(activity)
        activity.onBackPressed()
    }

    private fun onFavoriteBtnClick() {
        WarningSnackbar.dismiss()
        toggleFavoriteStatus()
    }

    private fun toggleFavoriteStatus() {
        note.isFavorite = !note.isFavorite
        changeIconFavoriteBtn()
    }

    private fun onDeleteBtnClick() {
        WarningSnackbar.dismiss()
        beginDeletingNote()
    }

    private fun beginDeletingNote() {
        isDeleteNote = true
        editText.hideKeyboard(activity)
        choosingDeletionWay()
        activity.onBackPressed()
    }

    private fun choosingDeletionWay() {
        if (noteId == 0L) {
            commandCenter.delete(note)
        } else if (noteId != 0L && isNewNote) {
            deleteNoteForever()
        } else {
            sendNoteToTrash()
        }
    }

    private fun deleteNoteForever() {
        commandCenter.delete(note)
        callback?.onUpdateNote()
    }

    private fun sendNoteToTrash() {
        val hasFavStatus: Boolean = note.isFavorite
        note.isFavorite = false
        commandCenter.sendToTrash(note)
        callback?.onSendNoteToTrash(note, hasFavStatus)
    }

    private fun onMoreBtnClick() {
        WarningSnackbar.dismiss()
        showNoteAssistantSheetDialog()
    }

    private fun showNoteAssistantSheetDialog() {
        editText.hideKeyboard(activity)
        viewLifecycleOwner.lifecycleScope.launch {
            delay(300L)
            val stringDate: String = DateUtil.getStringDate(note.dateModification)
            NoteAssistantSheetDialog(activity, assistant).show(stringDate, noteId)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun LinearLayout.setOnLinLayoutClickListener() {
        setOnTouchListener { _, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) onEmptyAreaClick()
            false
        }
    }

    override fun onPause() {
        if (!isDeleteNote) checkingNoteForText()
        super.onPause()
    }

    private fun checkingNoteForText() {
        note.title = editText.text.toString()

        if (note.title.trim().isNotEmpty()) {
            choosingSavingWayNote()
        } else {
            deleteNoteForever()
        }
    }

    private fun choosingSavingWayNote() {
        val saveDate = Date()

        if (noteId == 0L) {
            performInsert(saveDate)
        } else {
            performUpdate(saveDate)
        }

        performAutoBackup()
    }

    private fun performInsert(saveDate: Date) {
        note.dateModification = saveDate
        note.dateCreation = saveDate

        note.id = commandCenter.insertWithIdReturn(note)
        noteId = note.id

        backupDate = saveDate
        callback?.onAddNote()
    }

    private fun performUpdate(saveDate: Date) {
        if (note.title.trim() != savedText.trim()) {
            note.dateModification = saveDate
            commandCenter.update(note)
            callback?.onUpdateNote()
        } else if (note.isFavorite != isFavorite) {
            commandCenter.update(note)
            callback?.onUpdateNote()
        }
    }

    private fun performAutoBackup() {
        val hasAutoBackup: Boolean = AppPreference.hasAutoBackup(activity)

        if (hasAutoBackup && isNewNote && noteId % 3 == 0L) {
            DataBaseAutoBackup.checkingStorageAccess(
                activity, backupDate,
                hasShowToast, storageAccess
            )
        }
    }

    override fun onRequestPermissionsStorageResult(isGranted: Boolean) {
        if (isGranted) {
            assistant.performSaveTextFile()
        } else {
            showSnackbar(WarningSnackbar.MSG_NO_PERMISSION)
        }
    }

    fun showSnackbar(messageType: String) {
        WarningSnackbar.show(constLayout, fab, messageType)
    }

    override fun onDetach() {
        super.onDetach()
        if (!hasReceivingText) ReceivingDataActivity.registerReceivingDataCallback(null)
    }

    //--- interface --------------------------------------------------------------------------------
    interface NoteCallback {
        fun onAddNote()
        fun onUpdateNote()
        fun onSendNoteToTrash(note: Note, hasFavStatus: Boolean)
    }

    companion object {
        private var callback: NoteCallback? = null

        fun registerNoteCallback(callback: NoteCallback) {
            this.callback = callback
        }

        fun newInstance(): NoteFragment {
            return NoteFragment()
        }
    }

    override fun onCloseRepeatedNote() {
        activity.finish()
    }
}

// Extension functions
private fun EditText.changeWorkWithLinks(supportedLinks: Int) {
    val hasNormalClickProcessing = true

    if (supportedLinks == 0) {
        autoLinkMask = 0
        isCursorVisible = hasNormalClickProcessing
        showSoftInputOnFocus = hasNormalClickProcessing
    } else {
        autoLinkMask = supportedLinks
        isCursorVisible = !hasNormalClickProcessing
        showSoftInputOnFocus = !hasNormalClickProcessing
    }
}