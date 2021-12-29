package com.loskon.noteminimalism3.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.managers.setFabColor
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.ui.dialogs.NoteReceivingDataDialog
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.share.NotesSelectedListAdapter
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Список заметок для выбора вставки текста
 */

class ReceivingDataActivity :
    AppBaseActivity(),
    NotesSelectedListAdapter.SendAdapterCallback {

    private val commandCenter: CommandCenter = CommandCenter()
    private val adapterSelected: NotesSelectedListAdapter = NotesSelectedListAdapter()

    private lateinit var tvEmpty: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomBar: BottomAppBar

    private var color: Int = 0
    private val notesCategory: String = CATEGORY_ALL_NOTES
    private var receivingText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiving_data)
        installCallbacks()
        setupViewDeclaration()
        establishColorViews()
        configureRecyclerAdapter()
        configureRecyclerView()
        updateNotesList()
        installHandlers()
        receivingTextData()
    }

    private fun installCallbacks() {
        NotesSelectedListAdapter.registerCallbackSendAdapter(this)
    }

    private fun setupViewDeclaration() {
        recyclerView = findViewById(R.id.recycler_view_note_reciving)
        tvEmpty = findViewById(R.id.tv_empty_list_receiving_data)
        fab = findViewById(R.id.fab_receiving_data)
        bottomBar = findViewById(R.id.bottom_bar_receiving_data)
    }

    private fun establishColorViews() {
        color = PrefHelper.getAppColor(this)
        bottomBar.setNavigationIconColor(color)
        fab.setFabColor(color)
    }

    private fun configureRecyclerAdapter() {
        adapterSelected.setViewColor(color)

        val titleFontSize: Int = PrefHelper.getTitleFontSize(this)
        val dateFontSize: Int = PrefHelper.getDateFontSize(this)
        adapterSelected.setFontSizes(titleFontSize, dateFontSize)

        val numberLines: Int = PrefHelper.getNumberLines(this)
        adapterSelected.setNumberLines(numberLines)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterSelected
        recyclerView.itemAnimator = AppItemAnimator()
    }

    private fun updateNotesList() {
        adapterSelected.setFilesList(notes)
        checkEmptyFilesList()
    }

    private val notes: List<Note>
        get() {
            val sortingWay: Int = PrefHelper.getSortingWay(this)
            return commandCenter.getNotes(null, notesCategory, sortingWay)
        }

    private fun checkEmptyFilesList() {
        tvEmpty.setVisibleView(adapterSelected.itemCount == 0)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            addNewNote()
        }

        bottomBar.setOnSingleClickListener {
            finish()
        }
    }

    fun addNewNote() {
        val note = Note()
        note.title = receivingText
        callback?.onReceivingData()
        IntentManager.openNoteFromDialog(this, note, notesCategory)
        finish()
    }

    private fun updateCreatedNote(note: Note) {
        val title: String = note.title.plus("\n\n").plus(receivingText)
        note.title = title
        callback?.onReceivingData()
        IntentManager.openNoteFromDialog(this, note, notesCategory)
        finish()
    }

    private fun receivingTextData() {
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    receivingText = it
                    NoteReceivingDataDialog(this).show()
                }
            }
        }
    }

    override fun onClickingNote(note: Note) {
        updateCreatedNote(note)
    }

    interface ReceivingDataCallback {
        fun onReceivingData()
    }

    companion object {
        private var callback: ReceivingDataCallback? = null

        fun registerCallbackReceivingData(callback: ReceivingDataCallback) {
            this.callback = callback
        }
    }
}