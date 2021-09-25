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
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList
import com.loskon.noteminimalism3.ui.activities.WidgetHelperMainKt
import com.loskon.noteminimalism3.ui.fragments.BottomSheetCategory
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.update.NoteListAdapterUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.SwipeCallbackMainUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarUndoUpdate
import com.loskon.noteminimalism3.utils.*
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
    private lateinit var bottomBar: BottomAppBar
    private lateinit var fab: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var tvNumberSelected: TextView
    private lateinit var searchView: SearchView
    private lateinit var coordLayout: CoordinatorLayout

    private val adapter: NoteListAdapterUpdate = NoteListAdapterUpdate()

    private var notesCategory: String = NoteViewModel.CATEGORY_ALL_NOTES
    private var isDeleteMode: Boolean = false
    private var isAllSel: Boolean = false

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
        cardView = findViewById(R.id.card_view_main)
        coordLayout = findViewById(R.id.coord_layout_main)
        recyclerView = findViewById(R.id.recycler_view_notes)
        bottomBar = findViewById(R.id.bottom_bar_main)
        fab = findViewById(R.id.fab_main)
        tvNumberSelected = findViewById(R.id.tv_number_selected)
        searchView = findViewById(R.id.search_view)

        //cardView.backgroundTintList = ColorStateList.valueOf(MyColor.getMyColor(this))
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
        widgetsHelper = WidgetHelperMainKt(this, fab, bottomBar)
        snackbarUndo = SnackbarUndoUpdate(this, shortsCommand)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            clickingFab()
        }

        bottomBar.setNavigationOnClickListener {
            clickingNavigationButton()
        }

        bottomBar.setOnMenuItemClickListener { item ->
            snackbarUndo.close()

            when (item.itemId) {
                R.id.action_switch_view -> {
                    //handlingSwitchClick()
                    true
                }
                R.id.action_select_item -> {
                    adapter.selectAllItems()
                    true
                }

                R.id.action_search -> {

                    true
                }

                R.id.action_unification -> {

                    true
                }
                else -> false
            }
        }
    }

    private fun clickingFab() {
        if (isDeleteMode) {
            deleteItems()
        } else {
            IntentUtil.openNote(this, Note2(), notesCategory)
        }
    }

    private fun deleteItems() {
        if (notesCategory == NoteViewModel.CATEGORY_TRASH) {
            adapter.removeItems(shortsCommand)
        } else {
            adapter.sendItemsToTrash(shortsCommand)
        }

        updateListNotes()
        onActivationDeleteMode(false)
    }

    private fun clickingNavigationButton() {
        snackbarUndo.close()

        if (isDeleteMode) {
            onActivationDeleteMode(false)
            adapter.itemChanged()
        } else {
            val bottomSheet = BottomSheetCategory.newInstance(notesCategory)
            if (supportFragmentManager.findFragmentByTag(BottomSheetCategory.TAG) == null) {
                bottomSheet.show(supportFragmentManager, BottomSheetCategory.TAG);
            }
        }
    }

    private fun selectAllItems(isSel: Boolean) {
        for (note in adapter.getListNote()) {
            //if (notesCategory != NoteViewModel.CATEGORY_TRASH) note.dateDelete = Date()
            //note.isChecked = isSel
            shortsCommand.update(note)
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

    // Callbacks
    override fun onClickingItem(note: Note2) {
        IntentUtil.openNote(this, note, notesCategory)
    }

    override fun onActivationDeleteMode(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
        deleteMode()
    }

    private fun deleteMode() {
        if (isDeleteMode) {
            cardView.setVisibleView(true)
            swipeCallback.blockSwiped(true)
            widgetsHelper.setVisibleSelect(true)
            widgetsHelper.setIconFab(WidgetHelperList.ICON_FAB_DELETE)
            widgetsHelper.setVisibleList(false)
            widgetsHelper.setNavigationIcon(false)

            //if (isSearchMode) widgetsHelper.changeBarVisible(true)

        } else {
            cardView.setVisibleView(false)
            swipeCallback.blockSwiped(false)
            widgetsHelper.setVisibleSelect(false)
            widgetsHelper.setIconFab(notesCategory, false)
            widgetsHelper.setVisibleList(true)
            widgetsHelper.setNavigationIcon(true)
            adapter.disableDeleteMode()
        }
    }

    override fun onSelectingItem(selectedItemCount: Int, hasAllSelected: Boolean) {
        tvNumberSelected.text = selectedItemCount.toString()
        widgetsHelper.setSelectIcon(hasAllSelected)
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
            return bottomBar
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