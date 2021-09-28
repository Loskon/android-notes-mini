package com.loskon.noteminimalism3.ui.activities.update

import android.os.Bundle
import android.os.Parcelable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList
import com.loskon.noteminimalism3.ui.activities.WidgetHelperUpdate
import com.loskon.noteminimalism3.ui.fragments.BottomSheetCategory
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.fragments.update.NoteTrashFragmentUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.update.NoteListAdapterUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.SwipeCallbackMainUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarUndoUpdate
import com.loskon.noteminimalism3.utils.*
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Основное activity для работы со списком
 */

class MainActivityUpdate : AppCompatActivity(),
    NoteListAdapterUpdate.CallbackAdapter,
    NoteFragmentUpdate.CallbackNoteUpdate,
    SwipeCallbackMainUpdate.CallbackSwipeUpdate,
    BottomSheetCategory.CallbackCategory,
    NoteTrashFragmentUpdate.CallbackNoteTrashUpdate {

    companion object {
        private val TAG = "MyLogs_${MainActivityUpdate::class.java.simpleName}"
        const val RECYCLER_STATE_EXERCISE = "recycler_state"
    }

    private lateinit var shortsCommand: AppShortsCommand
    private lateinit var widgetsHelper: WidgetHelperUpdate
    private lateinit var swipeCallback: SwipeCallbackMainUpdate
    private lateinit var snackbarUndo: SnackbarUndoUpdate

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var bottomBar: BottomAppBar
    private lateinit var fab: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var tvNumberSelected: TextView
    private lateinit var searchView: SearchView
    private lateinit var coordLayout: CoordinatorLayout

    private val adapter: NoteListAdapterUpdate = NoteListAdapterUpdate()
    private var bundleState: Bundle? = null

    private var hasLinearList: Boolean = true
    private var isDeleteMode: Boolean = false
    private var color: Int = 0
    private var sortingWay: Int = 0
    private var notesCategory: String = CATEGORY_ALL_NOTES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        configureRecyclerAdapter()

        configureRecyclerView()

        initObjects()

        configureViews()

        installHandlers()

        installCallbacks()

        updateQuicklyNotesList()

        installSwiped()
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.coord_layout_main)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view_notes)
        tvEmpty = findViewById(R.id.tv_empty_list_up)
        fab = findViewById(R.id.fab_main)
        cardView = findViewById(R.id.card_view_main)
        tvNumberSelected = findViewById(R.id.tv_number_selected)
        bottomBar = findViewById(R.id.bottom_bar_main)
    }

    private fun configureRecyclerAdapter() {
        val radiusStrokeDp: Int = this.getRadiusLinLay()
        val boredStrokeDp: Int = this.getStrokeLinLay()
        adapter.setViewSizes(radiusStrokeDp, boredStrokeDp)

        color = AppPref.getAppColor(this)
        adapter.setViewColor(color)

        val titleFontSize: Int = AppPref.getFontSize(this)
        val dateFontSize: Int = AppPref.getDateFontSize(this)
        adapter.setFontSizes(titleFontSize, dateFontSize)

        val numberLines: Int = AppPref.getNumberLines(this)
        adapter.setNumberLines(numberLines)

        hasLinearList = AppPref.getLinearList(this)
        adapter.setLinearList(hasLinearList)

        val hasOneSizeCards: Boolean = AppPref.getOneSizeCards(this)
        adapter.setOneSizeCards(hasOneSizeCards)

        sortingWay = AppPref.getSort(this)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun initObjects() {
        shortsCommand = AppShortsCommand()
        widgetsHelper = WidgetHelperUpdate(this, fab, bottomBar)
        snackbarUndo = SnackbarUndoUpdate(this, shortsCommand)
    }

    private fun configureViews() {
        widgetsHelper.setTypeNotes(recyclerView, hasLinearList)
        cardView.setBackgroundTintColor(color)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            clickingFab()
        }

        bottomBar.setNavigationOnClickListener {
            snackbarUndo.close()
            clickingNavigationButton()
        }

        bottomBar.setOnMenuItemClickListener { item ->
            snackbarUndo.close()

            when (item.itemId) {
                R.id.action_switch_view -> {
                    clickingMenuSwitch()
                    saveStateLinearList()
                    true
                }
                R.id.action_select_item -> {
                    adapter.selectAllNotes()
                    true
                }

                R.id.action_search -> {

                    true
                }

                R.id.action_unification -> {
                    adapter.unificationItems(shortsCommand)
                    onActivationDeleteMode(false)
                    updateListNotes()
                    lifecycleScope.launch {
                        delay(200L)
                        recyclerView.smoothScrollToPosition(0)
                    }

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
        if (notesCategory == CATEGORY_TRASH) {
            adapter.removeItems(shortsCommand)
        } else {
            adapter.sendItemsToTrash(shortsCommand)
        }

        onActivationDeleteMode(false)
        updateListNotes()
    }

    private fun clickingNavigationButton() {
        snackbarUndo.close()

        if (isDeleteMode) {
            onActivationDeleteMode(false)
            adapter.itemsChanged()
        } else {
            val bottomSheet = BottomSheetCategory.newInstance(notesCategory)
            if (supportFragmentManager.findFragmentByTag(BottomSheetCategory.TAG) == null) {
                bottomSheet.show(supportFragmentManager, BottomSheetCategory.TAG);
            }
        }
    }

    private fun clickingMenuSwitch() {
        hasLinearList = !hasLinearList
        adapter.setLinearList(hasLinearList)
        widgetsHelper.setTypeNotes(recyclerView, hasLinearList)
    }

    private fun saveStateLinearList() {
        AppPref.setStateLinearList(this, hasLinearList)
    }

    private fun installCallbacks() {
        NoteListAdapterUpdate.callbackAdapterListener(this)
        NoteFragmentUpdate.callbackNoteListener(this)
        SwipeCallbackMainUpdate.callbackSwipeListener(this)
        BottomSheetCategory.callbackCategoryListener(this)
        NoteTrashFragmentUpdate.callbackNoteTrashListener(this)
    }

    fun updateListNotes() {
        val notes: List<Note2> = shortsCommand.getNotes(notesCategory, sortingWay)
        adapter.setNotesList(notes)
        tvEmpty.setVisibleView(notes.isEmpty())
    }

    private fun updateQuicklyNotesList() {
        val notes: List<Note2> = shortsCommand.getNotes(notesCategory, sortingWay)
        adapter.setQuicklyNotesList(notes)
        tvEmpty.setVisibleView(notes.isEmpty())
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
        snackbarUndo.close()
        deleteModeStatus()
    }

    private fun deleteModeStatus() {
        widgetsHelper.startAnimateFab()

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
            widgetsHelper.setIconFabCategory(notesCategory)
            //widgetsHelper.setIconFab(notesCategory, false)
            widgetsHelper.setVisibleList(true)
            widgetsHelper.setNavigationIcon(true)

            widgetsHelper.setVisibleUnification(notesCategory, false)
            adapter.disableDeleteMode()
        }
    }

    override fun onSelectingItem(selectedItemCount: Int, hasAllSelected: Boolean) {
        tvNumberSelected.text = selectedItemCount.toString()
        widgetsHelper.setSelectIcon(hasAllSelected)
        widgetsHelper.setVisibleUnification(notesCategory, selectedItemCount in 2..5)
    }

    override fun onResume() {
        super.onResume()
        resetRecyclerState()
    }

    private fun resetRecyclerState() {
        val state: Parcelable? = bundleState?.getParcelable(RECYCLER_STATE_EXERCISE)
        recyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerStata()
    }

    private fun saveRecyclerStata() {
        val listState: Parcelable? = recyclerView.layoutManager?.onSaveInstanceState()
        bundleState?.putParcelable(RECYCLER_STATE_EXERCISE, listState)
    }

    override fun onNoteSendToTrash(note: Note2, isFavorite: Boolean) {
        lifecycleScope.launch {
            delay(200L)
            snackbarUndo.show(note, isFavorite, notesCategory)
            updateListNotes()
        }
    }

    override fun onNoteAdd() {
        updateQuicklyNotesList()
        recyclerView.scrollToPosition(0)
    }

    override fun onNoteUpdate() {
        updateListNotes()
    }

    override fun onSwipeUpdate(note: Note2, isFavorite: Boolean) {
        updateListNotes()
        snackbarUndo.show(note, isFavorite, notesCategory)
    }

    override fun onCallbackCategory(notesCategory: String) {
        // !!!!БАГ с StaggeredGridLayoutManager связан с recyclerview.scroll
        this.notesCategory = notesCategory
        swipeCallback.setCategory(notesCategory)
        widgetsHelper.setIconFabCategory(notesCategory)
        onNoteAdd()
    }

    override fun onNoteDelete(note: Note2) {
        lifecycleScope.launch {
            delay(200L)
            snackbarUndo.show(note, false, notesCategory)
            updateListNotes()
        }
    }

    override fun onNoteReset(note: Note2) {
        lifecycleScope.launch {
            delay(200L)
            showToast("reset")
            updateListNotes()
        }
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
}