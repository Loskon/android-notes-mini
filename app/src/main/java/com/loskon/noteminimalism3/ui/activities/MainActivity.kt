package com.loskon.noteminimalism3.ui.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.DataBaseCloudBackup
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.managers.FontManager
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.other.MainWidgetHelper
import com.loskon.noteminimalism3.other.QueryTextListener
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.dialogfragmntes.CategorySheetFragment
import com.loskon.noteminimalism3.ui.dialogs.DeleteForeverWarningDialog
import com.loskon.noteminimalism3.ui.dialogs.SendToTrashWarningDialog
import com.loskon.noteminimalism3.ui.dialogs.UnificationDialog
import com.loskon.noteminimalism3.ui.fragments.*
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenCardView
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenNumberLines
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.notes.NoteRecyclerAdapter
import com.loskon.noteminimalism3.ui.recyclerview.notes.SwipeCallback
import com.loskon.noteminimalism3.ui.sheetdialogs.FileListSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.SortWaySheetDialog
import com.loskon.noteminimalism3.ui.snackbars.UndoSnackbar
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.ValueUtil
import com.loskon.noteminimalism3.utils.onlyShow
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView

/**
 * Главный экран для работы со списком заметок
 */

class MainActivity : BaseActivity(),
    NoteRecyclerAdapter.NoteActionListener,
    SwipeCallback.NoteSwipeListener,
    CategorySheetFragment.CategorySheetCallback,
    NoteFragment.NoteCallback,
    NoteTrashFragment.NoteTrashCallback,
    SettingsAppFragment.MainColorCallback,
    PrefScreenCardView.FontsSizesCallback,
    PrefScreenNumberLines.NumberLinesCallback,
    SettingsAppFragment.OneSizeCardsCallback,
    FileListSheetDialog.RestoreNoteCallback,
    DataBaseCloudBackup.RestoreNoteCloudCallback,
    BackupFragment.RestoreNoteAndroidRCallback,
    SortWaySheetDialog.SortWayCallback,
    FontsFragment.TypeFontCallback {

    private val commandCenter: CommandCenter = CommandCenter()
    private val adapter: NoteRecyclerAdapter = NoteRecyclerAdapter()

    private lateinit var widgetHelper: MainWidgetHelper
    private lateinit var swipeCallback: SwipeCallback
    private lateinit var undoSnackbar: UndoSnackbar

    private lateinit var coordLayout: CoordinatorLayout
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var tvCountItems: TextView
    private lateinit var bottomBar: BottomAppBar

    private var hasLinearList: Boolean = true
    private var isSelectionMode: Boolean = false
    private var isSearchMode: Boolean = false
    private var color: Int = 0
    private var sortingWay: Int = 0
    private var category: String = CATEGORY_ALL_NOTES
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        installCallbacks()
        setupViewDeclaration()
        initializingObjects()
        getSomeSharedPreferences()
        establishViewsColor()
        configureRecyclerAdapter()
        configureRecyclerView()
        differentConfigurations()
        installSwipeCallback()
        configureSearchWithSearchView()
        setupViewsListeners()
        updateQuicklyNoteList(true)
    }

    private fun installCallbacks() {
        // Для работы с заметками
        NoteFragment.registerNoteCallback(this)
        NoteTrashFragment.registerNoteTrashCallback(this)
        // Для изменения настроек
        SettingsAppFragment.registerColorCallback(this)
        PrefScreenCardView.registerFontsSizesCallback(this)
        PrefScreenNumberLines.registerNumberLinesCallback(this)
        SettingsAppFragment.registerOneSizeCardsCallback(this)
        FileListSheetDialog.registerRestoreNoteCallback(this)
        DataBaseCloudBackup.registerRestoreNoteCloudCallback(this)
        BackupFragment.registerRestoreNoteAndroidRCallback(this)
        SortWaySheetDialog.registerSortWayCallback(this)
        FontsFragment.registerTypeFontCallback(this)
    }

    private fun setupViewDeclaration() {
        coordLayout = findViewById(R.id.coord_layout_main)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view_main)
        tvEmpty = findViewById(R.id.tv_empty_main)
        fab = findViewById(R.id.fab_main)
        cardView = findViewById(R.id.card_view_main)
        tvCountItems = findViewById(R.id.tv_count_items)
        bottomBar = findViewById(R.id.bottom_bar_main)
    }

    private fun initializingObjects() {
        widgetHelper = MainWidgetHelper(this, searchView, fab, cardView, tvCountItems, bottomBar)
        undoSnackbar = UndoSnackbar(this, commandCenter, coordLayout, fab)
    }

    private fun getSomeSharedPreferences() {
        color = PrefHelper.getAppColor(this)
        sortingWay = PrefHelper.getSortingWay(this)
        hasLinearList = PrefHelper.hasLinearList(this)
    }

    private fun establishViewsColor() {
        widgetHelper.setColorsViews(color)
    }

    private fun configureRecyclerAdapter() {
        adapter.setViewsColor(color)
        adapter.setLinearList(hasLinearList)
        //
        val radiusStrokeDp: Int = ValueUtil.getRadiusLinLay(this)
        val boredStrokeDp: Int = ValueUtil.getStrokeLinLay(this)
        adapter.setViewSizes(radiusStrokeDp, boredStrokeDp)
        //
        val titleFontSize: Int = PrefHelper.getTitleFontSize(this)
        val dateFontSize: Int = PrefHelper.getDateFontSize(this)
        adapter.setFontSizes(titleFontSize, dateFontSize)
        //
        val numberLines: Int = PrefHelper.getNumberLines(this)
        adapter.setNumberLines(numberLines)
        //
        val hasOneSizeCards: Boolean = PrefHelper.hasOneSizeCards(this)
        adapter.setOneSizeCards(hasOneSizeCards)
        // Callback
        adapter.registerNoteClickListener(this)
    }

    private fun configureRecyclerView() {
        recyclerView.changeLayoutManager(hasLinearList)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = AppItemAnimator()
    }

    private fun differentConfigurations() {
        widgetHelper.changeIconToggleViewMenuItem(hasLinearList)
    }

    private fun installSwipeCallback() {
        swipeCallback = SwipeCallback(adapter)
        swipeCallback.registerNoteSwipeListener(this)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    private fun configureSearchWithSearchView() {
        searchView.setOnQueryTextListener(object : QueryTextListener() {
            override fun queryTextChange(query: String?) {
                searchText = query
                updateQuicklyNoteList(true)
            }
        })
    }

    private fun updateQuicklyNoteList(scrollToTop: Boolean) {
        val list: List<Note> = noteList
        tvEmpty.setVisibleView(list.isEmpty())
        adapter.setQuicklyNoteList(list)
        recyclerView.scrollToTop(list, scrollToTop)
    }

    private val noteList: List<Note>
        get() {
            return commandCenter.getNotes(searchText, category, sortingWay)
        }

    private fun RecyclerView.scrollToTop(list: List<Note>, hasScrollToTop: Boolean) {
        if (list.isNotEmpty() && hasScrollToTop) scrollToPosition(0)
    }

    private fun setupViewsListeners() {
        fab.setOnSingleClickListener { onFabClick() }
        bottomBar.setNavigationOnClickListener { onNavigationBtnClick() }
        bottomBar.setOnMenuItemClickListener { onMenuItemsClick(it) }
    }

    private fun onFabClick() {
        if (isSelectionMode) {
            pressingInDeleteMode()
        } else {
            pressingInNormalMode()
        }
    }

    private fun pressingInDeleteMode() {
        if (category == CATEGORY_TRASH) {
            DeleteForeverWarningDialog(this).show()
        } else {
            sendSelectedNotesToTrash()
        }
    }

    private fun sendSelectedNotesToTrash() {
        adapter.sendSelectedItemsToTrash(commandCenter)
        disableSelectionMode()
        updateNoteList()
    }

    private fun disableSelectionMode() {
        isSelectionMode = false
        swipeCallback.blockSwiped(isSelectionMode)
        adapter.disableSelectionMode()
        changingViewsForSelectionMode()
    }

    fun updateNoteList() {
        val notes: List<Note> = noteList
        tvEmpty.setVisibleView(notes.isEmpty())
        adapter.setNoteList(notes)
    }

    private fun changingViewsForSelectionMode() {
        widgetHelper.changingViewsForSelectionMode(category, isSelectionMode, isSearchMode)
    }

    private fun pressingInNormalMode() {
        if (isSearchMode) {
            togglingSearchMode(false)
        } else {
            if (category == CATEGORY_TRASH) {
                SendToTrashWarningDialog(this).show(adapter.itemCount)
            } else {
                IntentManager.openNote(this, Note(), category)
            }
        }
    }

    private fun togglingSearchMode(hasActivation: Boolean) {
        this.isSearchMode = hasActivation
        widgetHelper.togglingSearchMode(category, hasActivation)
    }

    private fun onNavigationBtnClick() {
        dismissSnackbars()

        if (isSelectionMode) {
            cancelSelectionMode()
        } else {
            if (isSearchMode) {
                togglingSearchMode(false)
            } else {
                openBottomSheetCategory()
            }
        }
    }

    private fun dismissSnackbars() {
        WarningSnackbar.dismiss()
        undoSnackbar.dismiss()
    }

    private fun cancelSelectionMode() {
        disableSelectionMode()
        adapter.updateChangedList()
    }

    private fun openBottomSheetCategory() {
        val bottomSheet: DialogFragment = CategorySheetFragment.newInstance(category)
        bottomSheet.onlyShow(supportFragmentManager, CategorySheetFragment.TAG)
    }

    private fun onMenuItemsClick(item: MenuItem): Boolean {
        dismissSnackbars()

        when (item.itemId) {
            R.id.action_toggle_view -> menuItemSwitchClick()
            R.id.action_select_item -> adapter.selectAllNotes()
            R.id.action_search -> togglingSearchMode(true)
            R.id.action_unification -> UnificationDialog(this).show()
            R.id.action_favorite -> adapter.changeFavoriteStatus(commandCenter)
        }

        return true
    }

    private fun menuItemSwitchClick() {
        hasLinearList = !hasLinearList
        adapter.setLinearList(hasLinearList)
        recyclerView.changeLayoutManager(hasLinearList)
        widgetHelper.changeIconToggleViewMenuItem(hasLinearList)
        PrefHelper.setStateLinearList(this, hasLinearList)
    }

    //--- NoteRecyclerAdapter ----------------------------------------------------------------------
    override fun onNoteClick(note: Note) {
        IntentManager.openNote(this, note, category)
    }

    override fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean) {
        widgetHelper.selectingNote(category, selectedItemsCount, hasAllSelected)
    }

    override fun onChangeStatusOfFavorite(note: Note) {
        widgetHelper.changeIconFavoriteMenuItem(note.isFavorite)
    }

    override fun onActivatingSelectionMode() {
        isSelectionMode = true
        swipeCallback.blockSwiped(isSelectionMode)
        dismissSnackbars()
        changingViewsForSelectionMode()
    }

    override fun onShowSnackbar(messageType: String) {
        showSnackbar(messageType)
    }

    //--- NoteFragment -----------------------------------------------------------------------------
    override fun onAddNote() = updateQuicklyNoteList(true)

    override fun onUpdateNote() = updateQuicklyNoteList(false)

    override fun onSendNoteToTrash(note: Note, hasFavStatus: Boolean) {
        updateQuicklyNoteList(false)
        val hasShowUndoSnackbar: Boolean = PrefHelper.isBottomWidgetShow(this)
        if (hasShowUndoSnackbar) showSnackbarUndo(note, hasFavStatus)
    }

    //--- NoteTrashFragment ------------------------------------------------------------------------
    override fun onDeleteNote(note: Note, hasFavStatus: Boolean) {
        updateQuicklyNoteList(false)
        showSnackbarUndo(note, hasFavStatus)
    }

    override fun onRestoreNote(note: Note) {
        updateQuicklyNoteList(false)
        showSnackbar(WarningSnackbar.MSG_NOTE_RESTORED)
    }

    fun showSnackbar(messageType: String) = WarningSnackbar.show(coordLayout, fab, messageType)

    //--- SwipeCallback ----------------------------------------------------------------------------
    override fun onNoteSwipe(note: Note, hasFavStatus: Boolean) {
        commandCenter.selectDeleteOption(category, note)
        updateNoteList()
        showSnackbarUndo(note, hasFavStatus)
    }

    private fun showSnackbarUndo(note: Note, hasFavStatus: Boolean) {
        undoSnackbar.show(note, hasFavStatus, category)
    }

    //--- CategorySheetFragment --------------------------------------------------------------------
    override fun onChangeCategory(category: String) {
        this.category = category
        widgetHelper.changeFabIcon(category)
        updateQuicklyNoteList(true)
    }

    //--- SettingsActivity -------------------------------------------------------------------------
    override fun onChangeColor(color: Int) {
        this.color = color
        adapter.setViewsColor(color)
        updateQuicklyNoteList(true)
        establishViewsColor()
    }

    override fun onChangeFontSizes(fontSizeTitle: Int, fontSizeDate: Int) {
        adapter.setFontSizes(fontSizeTitle, fontSizeDate)
        updateQuicklyNoteList(true)
    }

    override fun onChangeNumberLines(numberLines: Int) {
        adapter.setNumberLines(numberLines)
        updateQuicklyNoteList(true)
    }

    override fun onChangeStatusSizeCards(hasOneSizeCards: Boolean) {
        adapter.setOneSizeCards(hasOneSizeCards)
        updateQuicklyNoteList(true)
    }

    override fun onRestoreNotes() {
        category = CATEGORY_ALL_NOTES
        widgetHelper.changeFabIcon(category)
        updateQuicklyNoteList(true)
    }

    override fun onChangeSortingWay(sortingWay: Int) {
        this.sortingWay = sortingWay
        updateQuicklyNoteList(true)
    }

    override fun onChangeTypeFont(typeFace: Typeface?) {
        FontManager.setFont(this)
        recyclerView.adapter = adapter
        typeFace?.let { tvEmpty.typeface = it }
    }

    //--- Внешние методы ---------------------------------------------------------------------------
    // SendToTrashWarningDialog
    fun cleanTrash() {
        commandCenter.cleanTrash()
        updateQuicklyNoteList(false)
    }

    // DeleteForeverWarningDialog
    fun deleteItemsForever() {
        adapter.deleteItems(commandCenter)
        disableSelectionMode()
        updateNoteList()
    }

    // UnificationDialog
    fun performUnificationNotes(delete: Boolean) {
        adapter.unification(commandCenter, delete)
        disableSelectionMode()
        updateQuicklyNoteList(true)
    }

    //--- Прочее -----------------------------------------------------------------------------------
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            when {
                isSelectionMode -> {
                    cancelSelectionMode()
                    false
                }
                isSearchMode -> {
                    togglingSearchMode(false)
                    false
                }
                else -> {
                    super.onKeyDown(keyCode, event)
                }
            }
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        dismissSnackbars()
        super.onPause()
    }
}

// Extension functions
private fun RecyclerView.changeLayoutManager(isLinear: Boolean) {
    layoutManager = if (isLinear) {
        LinearLayoutManager(context)
    } else {
        StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
    }
}
