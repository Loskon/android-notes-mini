package com.loskon.noteminimalism3.ui.activities.update

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.other.MyIntent
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import com.loskon.noteminimalism3.ui.activities.WidgetHelperMainKt
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.update.NoteListAdapterUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.SwipeCallbackMainUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarUndoUpdate
import com.loskon.noteminimalism3.utils.getRadiusLinLay
import com.loskon.noteminimalism3.utils.getStrokeLinLay
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.showToast
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand
import com.loskon.noteminimalism3.viewmodel.NoteViewModel

/**
 * Основное activity для работы со списком
 */

class MainActivityUpdate : AppCompatActivity(),
    NoteListAdapterUpdate.CallbackAdapter,
    NoteFragmentUpdate.CallbackNoteUpdate,
    SwipeCallbackMainUpdate.CallbackSwipeUpdate {

    companion object {
        private val TAG = "MyLogs_${MainActivityUpdate::class.java.simpleName}"
    }

    private lateinit var shortsCommand: AppShortsCommand
    private lateinit var widgetsHelper: WidgetHelperMainKt
    private lateinit var swipeCallback: SwipeCallbackMainUpdate
    private lateinit var snackbarUndo: SnackbarUndoUpdate

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomAppBar: BottomAppBar
    private lateinit var fab: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var tvNumber: TextView
    private lateinit var searchView: SearchView
    private lateinit var coordLayout: CoordinatorLayout

    private val adapter: NoteListAdapterUpdate = NoteListAdapterUpdate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        configureRecyclerAdapter()

        configureRecyclerView()

        initObjects()

        installHandlers()

        installCallbacks()

        updateListNotes()

        installSwiped()
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.coord_layout_main)
        recyclerView = findViewById(R.id.recycler_view_notes)
        bottomAppBar = findViewById(R.id.bottom_bar_main)
        fab = findViewById(R.id.fab_main)
        tvNumber = findViewById(R.id.tv_font_size_title)
        searchView = findViewById(R.id.search_view)
    }

    private fun configureRecyclerAdapter() {
        val radiusStrokeDp: Int = this.getRadiusLinLay()
        val boredStrokeDp: Int = this.getStrokeLinLay()
        adapter.setViewSizes(radiusStrokeDp, boredStrokeDp)

        val color: Int = MyColor.getMyColor(this)
        adapter.setViewColor(color)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun initObjects() {
        shortsCommand = AppShortsCommand()
        widgetsHelper = WidgetHelperMainKt(this, fab, bottomAppBar)
        snackbarUndo = SnackbarUndoUpdate(this, shortsCommand)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {

            //this.showToast(""+ adapter.selectedItemCount+ " : " + adapter.selectedItemCount2)

            val selected = adapter.getRemoveItems()

            for (item in selected) {
                item.isDelete = true
                shortsCommand.update(item)
            }

            adapter.clearSelection()

            updateListNotes()

            //MyIntent.addNewNoteUpdate(this, Note2(), NoteViewModel.CATEGORY_ALL_NOTES)
        }
    }

    private fun installCallbacks() {
        NoteListAdapterUpdate.callbackAdapterListener(this)
        NoteFragmentUpdate.callbackNoteListener(this)
        SwipeCallbackMainUpdate.callbackSwipeListener(this)
    }

    private fun updateListNotes() {
        val notes: List<Note2> =
            shortsCommand.getNotes("del_items = 0", NoteTable.COLUMN_DATE + " DESC")
        adapter.setListNote(notes)
    }

    private fun installSwiped() {
        swipeCallback = SwipeCallbackMainUpdate(adapter, shortsCommand)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }


    override fun onItemClick(note: Note2) {
        MyIntent.addNewNoteUpdate(this, note, NoteViewModel.CATEGORY_ALL_NOTES)
    }

    override fun onSelectedItem(note: Note2, size: Int) {
        this.showToast("" + size)
    }

    override fun onDeleteMode(isDelMode: Boolean) {

    }

    override fun onNumSelItem(isAll: Boolean, numSelItem: Int) {

    }

    override fun onX() {

    }

    val getCoordLayout: CoordinatorLayout
        get() {
            return coordLayout
        }

    val getFab: FloatingActionButton
        get() {
            return fab
        }

    val getBottomAppBar: BottomAppBar
        get() {
            return bottomAppBar
        }

    override fun onNoteDelete(note: Note2, isFav: Boolean) {

    }

    override fun onNoteAdd() {
        updateListNotes()
        recyclerView.scrollToPosition(0)
    }

    override fun onNoteUpdate() {
        updateListNotes()
    }

    override fun onNoteSwipe(note: Note2, isFav: Boolean, category: String) {
        snackbarUndo.show(note, isFav, category)
        updateListNotes()
    }

    fun UP() {
        updateListNotes()
    }
}