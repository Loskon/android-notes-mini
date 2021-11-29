package com.loskon.noteminimalism3.ui.activities

import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
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
import com.loskon.noteminimalism3.backup.DateBaseCloudBackup
import com.loskon.noteminimalism3.command.ShortsCommand
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.other.FontManager
import com.loskon.noteminimalism3.other.ShortQueryTextListener
import com.loskon.noteminimalism3.sharedpref.PrefManager
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.dialogs.DialogDeleteForever
import com.loskon.noteminimalism3.ui.dialogs.DialogTrash
import com.loskon.noteminimalism3.ui.dialogs.DialogUnification
import com.loskon.noteminimalism3.ui.fragments.*
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenCardView
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenNumberLines
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenResetColor
import com.loskon.noteminimalism3.ui.recyclerview.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.notes.NotesListAdapter
import com.loskon.noteminimalism3.ui.recyclerview.notes.SwipeCallback
import com.loskon.noteminimalism3.ui.sheets.SheetListRestoreDateBase
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColorHex
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSort
import com.loskon.noteminimalism3.ui.snackbars.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.ui.snackbars.SnackbarUndo
import com.loskon.noteminimalism3.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Основное activity для работы со списком
 */

class ListActivity : BaseActivity(),
    NotesListAdapter.CallbackAdapter,
    SwipeCallback.CallbackSwipeUndo,
    BottomSheetCategory.CallbackCategory,
    NoteFragment.CallbackNote,
    NoteTrashFragment.CallbackNoteTrash,
    SheetPrefSelectColor.CallbackColorList,
    SheetPrefSelectColorHex.CallbackColorHexList,
    PrefScreenResetColor.CallbackColorResetList,
    PrefScreenCardView.CallbackFontsSizes,
    PrefScreenNumberLines.CallbackNumberLines,
    SettingsAppFragment.CallbackOneSizeCards,
    SheetListRestoreDateBase.CallbackRestoreNote,
    DateBaseCloudBackup.CallbackRestoreNoteCloud,
    BackupFragment.CallbackRestoreNoteAndroidR,
    SheetPrefSort.CallbackSort,
    FontsFragment.CallbackTypeFont {

    companion object {
        const val RECYCLER_STATE = "recycler_state"
    }

    private lateinit var shortsCommand: ShortsCommand
    private lateinit var widgetsListHelper: WidgetListHelper
    private lateinit var swipeCallback: SwipeCallback
    private lateinit var snackbarUndo: SnackbarUndo

    private lateinit var coordLayout: CoordinatorLayout
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyList: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var tvSelectedItemsCount: TextView
    private lateinit var bottomBar: BottomAppBar

    private val adapter: NotesListAdapter = NotesListAdapter()
    private var bundleState: Bundle? = null

    private var hasLinearList: Boolean = true
    private var isSelMode: Boolean = false
    private var isSearchMode: Boolean = false
    private var color: Int = 0
    private var sortingWay: Int = 0
    private var notesCategory: String = CATEGORY_ALL_NOTES
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        initViews()
        installCallbacks()
        configureRecyclerAdapter()
        configureRecyclerView()
        configureSearchView()
        initObjects()
        installSwipeCallback()
        otherConfigurations()
        establishColorViews()
        installHandlers()
        updateQuicklyNotesList()
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.coord_layout_list)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view_notes)
        tvEmptyList = findViewById(R.id.tv_empty_list)
        fab = findViewById(R.id.fab_list)
        cardView = findViewById(R.id.card_view_main)
        tvSelectedItemsCount = findViewById(R.id.tv_selected_items_count)
        bottomBar = findViewById(R.id.bottom_bar_list)
    }

    private fun installCallbacks() {
        // Для работы с заметками
        NotesListAdapter.listenerCallback(this)
        SwipeCallback.listenerCallback(this)
        BottomSheetCategory.listenerCallback(this)
        NoteFragment.listenerCallback(this)
        NoteTrashFragment.listenerCallback(this)
        // Для изменения настроек
        SheetPrefSelectColor.listenerCallBackColorList(this)
        SheetPrefSelectColorHex.listenerCallBackColorList(this)
        PrefScreenResetColor.listenerCallBackColorList(this)
        PrefScreenCardView.listenerCallback(this)
        PrefScreenNumberLines.listenerCallback(this)
        SettingsAppFragment.listenerCallbackSize(this)
        SheetListRestoreDateBase.listenerCallback(this)
        DateBaseCloudBackup.listenerCallback(this)
        BackupFragment.listenerCallback(this)
        SheetPrefSort.listenerCallback(this)
        FontsFragment.listenerCallback(this)
    }

    private fun configureRecyclerAdapter() {
        val radiusStrokeDp: Int = ValueUtil.getRadiusLinLay(this)
        val boredStrokeDp: Int = ValueUtil.getStrokeLinLay(this)
        adapter.setViewSizes(radiusStrokeDp, boredStrokeDp)

        color = PrefManager.getAppColor(this)
        adapter.setViewColor(color)

        val titleFontSize: Int = PrefManager.getFontSize(this)
        val dateFontSize: Int = PrefManager.getDateFontSize(this)
        adapter.setFontSizes(titleFontSize, dateFontSize)

        val numberLines: Int = PrefManager.getNumberLines(this)
        adapter.setNumberLines(numberLines)

        hasLinearList = PrefManager.getLinearList(this)
        adapter.setLinearList(hasLinearList)

        val hasOneSizeCards: Boolean = PrefManager.getOneSizeCards(this)
        adapter.setOneSizeCards(hasOneSizeCards)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun configureSearchView() {
        searchView.setOnQueryTextListener(object : ShortQueryTextListener() {
            override fun onShortQueryTextChange(query: String?) {
                handlingSearch(query)
                updateQuicklyNotesList()
            }
        })
    }

    private fun handlingSearch(newText: String?) {
        searchText = if (TextUtils.isEmpty(newText)) {
            null
        } else {
            newText
        }
    }

    private fun updateQuicklyNotesList() {
        adapter.setQuicklyNotesList(notes)
        tvEmptyList.setVisibleView(notes.isEmpty())
    }

    private val notes: List<Note>
        get() {
            return shortsCommand.getNotes(searchText, notesCategory, sortingWay)
        }

    private fun initObjects() {
        shortsCommand = ShortsCommand()
        widgetsListHelper = WidgetListHelper(this, fab, bottomBar)
        snackbarUndo = SnackbarUndo(this, shortsCommand)
    }

    private fun installSwipeCallback() {
        swipeCallback = SwipeCallback(adapter, shortsCommand)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    private fun otherConfigurations() {
        sortingWay = PrefManager.getSortingWay(this)
        widgetsListHelper.setTypeNotes(recyclerView, hasLinearList)
    }

    private fun establishColorViews() {
        widgetsListHelper.setColorViews(color)
        cardView.setBackgroundTintColor(color)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            clickingFab()
        }

        bottomBar.setNavigationOnClickListener {
            dismissSnackbars(true)
            clickingNavigationButton()
        }

        bottomBar.setOnMenuItemClickListener { item ->
            dismissSnackbars(true)

            when (item.itemId) {
                R.id.action_switch_view -> {
                    clickingMenuSwitch()
                    true
                }
                R.id.action_select_item -> {
                    adapter.selectAllNotes()
                    true
                }

                R.id.action_search -> {
                    activationSearchMode(true)
                    true
                }

                R.id.action_unification -> {
                    DialogUnification(this).show()
                    true
                }

                R.id.action_favorite -> {
                    adapter.changeFavoriteStatus(shortsCommand)
                    true
                }

                else -> false
            }
        }
    }

    private fun clickingFab() {
        if (isSelMode) {
            if (notesCategory == CATEGORY_TRASH) {
                DialogDeleteForever(this).show()
            } else {
                sendItemsToTrash()
            }
        } else {
            if (isSearchMode) {
                activationSearchMode(false)
            } else {
                if (notesCategory == CATEGORY_TRASH) {
                    DialogTrash(this).show(adapter.itemCount)
                } else {
                    IntentManager.openNote(this, Note(), notesCategory)
                }
            }
        }
    }

    private fun sendItemsToTrash() {
        adapter.sendItemsToTrash(shortsCommand)
        onActivatingSelectionMode(false)
        updateListNotes()
    }

    override fun onActivatingSelectionMode(isSelMode: Boolean) {
        this.isSelMode = isSelMode
        dismissSnackbars(isSelMode)
        selectionModeStatus()
    }

    private fun selectionModeStatus() {
        cardView.setVisibleView(isSelMode)
        swipeCallback.blockSwiped(isSelMode)
        widgetsListHelper.setVisibleSelect(isSelMode)

        if (isSelMode) {
            widgetsListHelper.setVisibleSearchAndSwitch(false)
            widgetsListHelper.setNavigationIcon(false)
            widgetsListHelper.setDeleteIconFab(notesCategory)

            if (isSearchMode) {
                widgetsListHelper.bottomBarVisible(true)
            }

        } else {
            widgetsListHelper.setVisibleUnification(false)
            widgetsListHelper.setVisibleFavorite(notesCategory, false)

            if (isSearchMode) {
                widgetsListHelper.setVisibleSearchAndSwitch(false)
                widgetsListHelper.hideNavigationIcon()
                widgetsListHelper.bottomBarVisible(false)
                widgetsListHelper.setIconFab(WidgetListHelper.ICON_FAB_SEARCH_CLOSE)
            } else {
                widgetsListHelper.setVisibleSearchAndSwitch(true)
                widgetsListHelper.setNavigationIcon(true)
                widgetsListHelper.setIconFabCategory(notesCategory)
            }

            adapter.disableSelectionMode()
        }
    }

    fun updateListNotes() {
        adapter.setNotesList(notes)
        tvEmptyList.setVisibleView(notes.isEmpty())
    }

    private fun dismissSnackbars(isDisSnackMessage: Boolean) {
        if (isDisSnackMessage) BaseSnackbar.dismiss()
        snackbarUndo.dismiss()
    }

    private fun clickingNavigationButton() {
        dismissSnackbars(true)

        if (isSelMode) {
            cancelSelectionMode()
        } else {
            openBottomSheetCategory()
        }
    }

    private fun cancelSelectionMode() {
        onActivatingSelectionMode(false)
        adapter.itemsChanged()
    }

    private fun openBottomSheetCategory() {
        val bottomSheet = BottomSheetCategory.newInstance(notesCategory)
        // Защита от двойного открытия
        if (supportFragmentManager.findFragmentByTag(BottomSheetCategory.TAG) == null) {
            bottomSheet.show(supportFragmentManager, BottomSheetCategory.TAG)
        }
    }

    private fun clickingMenuSwitch() {
        hasLinearList = !hasLinearList
        adapter.setLinearList(hasLinearList)
        widgetsListHelper.setTypeNotes(recyclerView, hasLinearList)
        PrefManager.setStateLinearList(this, hasLinearList)
    }

    private fun activationSearchMode(isSearchMode: Boolean) {
        this.isSearchMode = isSearchMode
        searchView.setQuery("", false)

        if (isSearchMode) {
            searchView.setVisibleView(true)
            widgetsListHelper.bottomBarVisible(false)
            val searchEditText: EditText = searchView.findViewById(R.id.search_src_text)
            searchEditText.showKeyboard(this)
            widgetsListHelper.setIconFab(WidgetListHelper.ICON_FAB_SEARCH_CLOSE)
        } else {
            searchView.setVisibleView(false)
            widgetsListHelper.bottomBarVisible(true)
            widgetsListHelper.setNavigationIcon(true)
            widgetsListHelper.setVisibleSearchAndSwitch(true)
            widgetsListHelper.setIconFabCategory(notesCategory)
        }
    }

    private fun updateQuicklyNotesListTop() {
        updateQuicklyNotesList()
        recyclerView.scrollToPosition(0)
    }

    // From recycler adapter
    override fun onClickingNote(note: Note) {
        IntentManager.openNote(this, note, notesCategory)
    }

    // From recycler adapter
    override fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean) {
        tvSelectedItemsCount.text = selectedItemsCount.toString()
        widgetsListHelper.changeIconMenuSelect(hasAllSelected)
        widgetsListHelper.changeVisibleUnification(notesCategory, selectedItemsCount >= 2)
        widgetsListHelper.setVisibleFavorite(notesCategory, selectedItemsCount in 1..1)
    }

    // From recycler adapter
    override fun onVisibleFavorite(note: Note) {
        widgetsListHelper.changeIconMenuFavorite(note.isFavorite)
    }

    // From note fragment
    override fun onNoteAdd() = updateQuicklyNotesListTop()

    // From note fragment
    override fun onNoteUpdate() = updateQuicklyNotesList()

    // From swipe
    override fun onSwipe(note: Note, isFavorite: Boolean) {
        updateListNotes()
        snackbarUndo.show(note, isFavorite)
    }

    // From note trash fragment and note fragment
    override fun onNoteDelete(note: Note, isFavorite: Boolean) {
        lifecycleScope.launch {
            delay(200L)
            updateListNotes()
            snackbarUndo.show(note, isFavorite)
        }
    }

    // From note trash fragment
    override fun onNoteReset(note: Note) {
        lifecycleScope.launch {
            delay(200L)
            updateListNotes()
            showSnackbar(SnackbarManager.MSG_NOTE_RESTORED, true)
        }
    }

    // From sheet category
    override fun onCallbackCategory(notesCategory: String) {
        this.notesCategory = notesCategory
        swipeCallback.setCategory(notesCategory)
        widgetsListHelper.setIconFabCategory(notesCategory)

        updateQuicklyNotesList()
        scrollUpWithProtection()
    }

    private fun scrollUpWithProtection() {
        // Защита от создания пустого пространства над списком
        // при использовании StaggeredGridLayoutManager
        if (notes.isNotEmpty()) {
            recyclerView.scrollToPosition(0)
        }
    }

    fun showSnackbar(message: String, isSuccess: Boolean) {
        SnackbarManager(this, coordLayout, fab).show(message, isSuccess)
    }

    // From Settings
    override fun onChangeColor(color: Int) {
        this.color = color
        adapter.setViewColor(color)
        updateQuicklyNotesListTop()
        establishColorViews()
    }

    override fun onChangeFontSizes(fontSizeTitle: Int, fontSizeDate: Int) {
        adapter.setFontSizes(fontSizeTitle, fontSizeDate)
        updateQuicklyNotesListTop()
    }

    override fun onChangeNumberLines(numberLines: Int) {
        adapter.setNumberLines(numberLines)
        updateQuicklyNotesListTop()
    }

    override fun onChangeStatusSizeCards(hasOneSizeCards: Boolean) {
        adapter.setOneSizeCards(hasOneSizeCards)
        updateQuicklyNotesListTop()
    }

    override fun onRestoreNotes() {
        notesCategory = CATEGORY_ALL_NOTES
        widgetsListHelper.setIconFabCategory(notesCategory)
        updateQuicklyNotesListTop()
    }

    override fun onChangeSortingWay(sortingWay: Int) {
        this.sortingWay = sortingWay
        updateQuicklyNotesListTop()
    }

    override fun onChangeTypeFont(typeFace: Typeface?) {
        FontManager.setFont(this)
        recyclerView.adapter = adapter
        typeFace?.let { tvEmptyList.typeface = it }
    }

    // Внешние методы
    fun cleanTrash() {
        shortsCommand.cleanTrash()
        updateQuicklyNotesList()
    }

    fun deleteItemsForever() {
        adapter.deleteItems(shortsCommand)
        onActivatingSelectionMode(false)
        updateListNotes()
    }

    fun performUnificationNotes() {
        adapter.unification(this, shortsCommand)
        onActivatingSelectionMode(false)
        updateQuicklyNotesListTop()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            when {
                isSelMode -> {
                    cancelSelectionMode()
                    false
                }
                isSearchMode -> {
                    activationSearchMode(false)
                    false
                }
                else -> {
                    finish()
                    true
                }
            }
        } else super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        resetRecyclerState()
    }

    private fun resetRecyclerState() {
        val state: Parcelable? = bundleState?.getParcelable(RECYCLER_STATE)
        recyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    override fun onPause() {
        super.onPause()
        dismissSnackbars(true)
        saveRecyclerStata()
    }

    private fun saveRecyclerStata() {
        val listState: Parcelable? = recyclerView.layoutManager?.onSaveInstanceState()
        bundleState?.putParcelable(RECYCLER_STATE, listState)
    }

    fun getNotesCategory(): String {
        return notesCategory
    }

    val getCoordLayout: CoordinatorLayout
        get() {
            return coordLayout
        }

    val getFab: FloatingActionButton
        get() {
            return fab
        }
}