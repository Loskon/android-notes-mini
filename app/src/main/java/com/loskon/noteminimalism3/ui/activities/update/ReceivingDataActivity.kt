package com.loskon.noteminimalism3.ui.activities.update

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.other.AppFont
import com.loskon.noteminimalism3.sqlite.AppShortsCommand
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.ui.listview.NotesAdapter
import com.loskon.noteminimalism3.utils.IntentUtil
import com.loskon.noteminimalism3.utils.setNavigationIconColor
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

class ReceivingDataActivity : AppCompatActivity() {

    private val shortsCommand: AppShortsCommand = AppShortsCommand()
    private val adapter: NotesAdapter = NotesAdapter()

    private lateinit var tvEmpty: TextView
    private lateinit var listView: ListView
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomBar: BottomAppBar

    private var color: Int = 0
    private val notesCategory: String = CATEGORY_ALL_NOTES
    private var receivingText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        AppFont.setFont(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receiving_data)

        getArguments()
        initViews()
        establishColorViews()
        configureListView()
        configureListAdapter()
        updateNotesList()
        installHandlers()

/*        initViews()
        installCallbacks()
        configureRecyclerAdapter()
        configureRecyclerView()
        configureSearchView()
        initObjects()
        installSwiped()
        otherConfigurations()


        updateQuicklyNotesList()*/

    }


    private fun getArguments() {
        intent.getStringExtra(IntentUtil.PUT_EXTRA_RECEIVING_TEXT)?.let { receivingText = it }
    }

    private fun initViews() {
        listView = findViewById(R.id.listViewNotes)
        tvEmpty = findViewById(R.id.tv_empty_list_receiving_data)
        fab = findViewById(R.id.fabReceivingData2)
        bottomBar = findViewById(R.id.btmAppBarReceivingData)
    }

    private fun establishColorViews() {
        color = AppPref.getAppColor(this)
        bottomBar.setNavigationIconColor(color)
    }

    private fun configureListView() {
        listView.adapter = adapter
    }

    private fun configureListAdapter() {
        adapter.setViewColor(color)
    }

    private fun updateNotesList() {
        adapter.setFilesList(notes)
        checkEmptyFilesList()
    }

    private val notes: List<Note2>
        get() {
            val sortingWay: Int = AppPref.getSortingWay(this)
            return shortsCommand.getNotes(null, notesCategory, sortingWay)
        }

    private fun checkEmptyFilesList() {
        fab.setVisibleView(adapter.count == 0)
        tvEmpty.setVisibleView(adapter.count == 0)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            val note = Note2()
            note.title = receivingText
            IntentUtil.openNote(this, note, notesCategory)
        }

        bottomBar.setOnSingleClickListener {
            finish()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val note: Note2 = adapter.getItem(position)
            val newTitle = note.title.plus("\n\n").plus(receivingText)
            note.title = newTitle
            IntentUtil.openNoteFromDialog(this, note, notesCategory)
            finish()
        }
    }
}