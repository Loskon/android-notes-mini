package com.loskon.noteminimalism3.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.managers.setFabColor
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.ui.dialogs.NoteReceivingDataDialog
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.sharednotes.ReceivingNoteRecyclerAdapter
import com.loskon.noteminimalism3.utils.setVisibilityKtx

/**
 * Экран для выбора заметки из списка/создания новой для вставки полученного текста
 */

class ReceivingDataActivity :
    BaseActivity(),
    ReceivingNoteRecyclerAdapter.SharedNoteClickListener {

    private val commandCenter: CommandCenter = CommandCenter()
    private val adapter: ReceivingNoteRecyclerAdapter = ReceivingNoteRecyclerAdapter()

    private lateinit var tvEmpty: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomBar: BottomAppBar

    private var color: Int = 0
    private val category: String = CATEGORY_ALL_NOTES
    private var receivingText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiving_data)
        setupViewDeclaration()
        establishViewsColor()
        configureRecyclerAdapter()
        configureRecyclerView()
        updateSharedNotesList()
        setupViewsListeners()
        receivingTextData()
    }

    private fun setupViewDeclaration() {
        recyclerView = findViewById(R.id.recycler_view_note_reciving)
        tvEmpty = findViewById(R.id.tv_empty_list_receiving_data)
        fab = findViewById(R.id.fab_receiving_data)
        bottomBar = findViewById(R.id.bottom_bar_receiving_data)
    }

    private fun establishViewsColor() {
        color = AppPreference.getColor(this)
        bottomBar.setNavigationIconColor(color)
        fab.setFabColor(color)
    }

    private fun configureRecyclerAdapter() {
        adapter.setViewColor(color)
        //
        val titleFontSize: Int = AppPreference.getTitleFontSize(this)
        val dateFontSize: Int = AppPreference.getDateFontSize(this)
        adapter.setFontSizes(titleFontSize, dateFontSize)
        //
        val numberLines: Int = AppPreference.getNumberLines(this)
        adapter.setNumberLines(numberLines)
        // Callback
        adapter.registerSharedNoteClickListener(this)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = AppItemAnimator()
    }

    private fun updateSharedNotesList() {
        adapter.setSharedNoteList(notes)
        checkEmptyFilesList()
    }

    private val notes: List<Note>
        get() {
            val sortingWay: Int = AppPreference.getSortingWay(this)
            return commandCenter.getNotes(null, category, sortingWay)
        }

    private fun checkEmptyFilesList() {
        tvEmpty.setVisibilityKtx(adapter.itemCount == 0)
    }

    private fun setupViewsListeners() {
        fab.setDebounceClickListener { addNewNote() }
        bottomBar.setNavigationOnClickListener { finish() }
    }

    fun addNewNote() {
        openNote(getNewNote())
        closeUnnecessaryScreens()
    }

    private fun getNewNote(): Note = Note().apply { title = receivingText }

    private fun openNote(note: Note) = IntentManager.openNote(this, note, category, true)

    private fun closeUnnecessaryScreens() {
        callback?.onCloseRepeatedNote()
        finish()
    }

    private fun updateCreatedNote(note: Note) {
        openNote(getOldNote(note))
        closeUnnecessaryScreens()
    }

    private fun getOldNote(note: Note): Note {
        val title: String = note.title.plus("\n\n").plus(receivingText)
        note.title = title
        return note
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

    //--- ReceivingNoteRecyclerAdapter -------------------------------------------------------------
    override fun onSharedNoteClick(note: Note) {
        updateCreatedNote(note)
    }

    //--- interface --------------------------------------------------------------------------------
    interface ReceivingDataCallback {
        fun onCloseRepeatedNote()
    }

    companion object {
        private var callback: ReceivingDataCallback? = null

        fun registerReceivingDataCallback(callback: ReceivingDataCallback?) {
            this.callback = callback
        }
    }
}