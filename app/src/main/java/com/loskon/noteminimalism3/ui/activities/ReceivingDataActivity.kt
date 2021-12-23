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
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.ui.dialogs.DialogNoteReceivingData
import com.loskon.noteminimalism3.ui.recyclerview.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.share.NotesSelectedListAdapter
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Список заметок для выбора вставки текста
 */

class ReceivingDataActivity :
    BaseActivities(),
    NotesSelectedListAdapter.CallbackSendAdapter {

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

        NotesSelectedListAdapter.listenerCallback(this)

        initViews()
        establishColorViews()
        configureListView()
        configureListAdapter()
        updateNotesList()
        installHandlers()
        receivingTextData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recycler_view_note_reciving)
        tvEmpty = findViewById(R.id.tv_empty_list_receiving_data)
        fab = findViewById(R.id.fabReceivingData)
        bottomBar = findViewById(R.id.btmAppBarReceivingData)
    }

    private fun establishColorViews() {
        color = PrefManager.getAppColor(this)
        bottomBar.setNavigationIconColor(color)
        fab.setFabColor(color)
    }

    private fun configureListView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterSelected
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun configureListAdapter() {
        adapterSelected.setViewColor(color)

        val titleFontSize: Int = PrefManager.getTitleFontSize(this)
        val dateFontSize: Int = PrefManager.getDateFontSize(this)
        adapterSelected.setFontSizes(titleFontSize, dateFontSize)

        val numberLines: Int = PrefManager.getNumberLines(this)
        adapterSelected.setNumberLines(numberLines)
    }

    private fun updateNotesList() {
        adapterSelected.setFilesList(notes)
        checkEmptyFilesList()
    }

    private val notes: List<Note>
        get() {
            val sortingWay: Int = PrefManager.getSortingWay(this)
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
                    DialogNoteReceivingData(this).show()
                }
            }
        }
    }

    override fun onClickingNote(note: Note) {
        updateCreatedNote(note)
    }

    interface CallbackReceivingData {
        fun onReceivingData()
    }

    companion object {
        private var callback: CallbackReceivingData? = null

        fun listenerCallback(callback: CallbackReceivingData) {
            this.callback = callback
        }
    }
}