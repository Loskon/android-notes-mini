package com.loskon.noteminimalism3.ui.activities.update

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.EditText
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
import com.loskon.noteminimalism3.backup.update.DateBaseCloudBackup
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.other.AppFont
import com.loskon.noteminimalism3.other.ShortQueryTextListener
import com.loskon.noteminimalism3.sqlite.AppShortsCommand
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.dialogs.update.DialogDeleteForever
import com.loskon.noteminimalism3.ui.dialogs.update.DialogNoteReceivingData
import com.loskon.noteminimalism3.ui.dialogs.update.DialogTrashUpdate
import com.loskon.noteminimalism3.ui.fragments.BottomSheetCategory
import com.loskon.noteminimalism3.ui.fragments.update.FontsFragment
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.fragments.update.NoteTrashFragmentUpdate
import com.loskon.noteminimalism3.ui.fragments.update.SettingsAppFragmentUpdate
import com.loskon.noteminimalism3.ui.prefscreen.update.PrefScreenCardView
import com.loskon.noteminimalism3.ui.prefscreen.update.PrefScreenNumberLines
import com.loskon.noteminimalism3.ui.recyclerview.update.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.update.NoteListAdapterUpdate
import com.loskon.noteminimalism3.ui.recyclerview.update.SwipeCallbackMainUpdate
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSort
import com.loskon.noteminimalism3.ui.sheets.SheetRestoreDateBase
import com.loskon.noteminimalism3.ui.snackbars.update.BaseSnackbar
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarUndoUpdate
import com.loskon.noteminimalism3.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Основное activity для работы со списком
 */

class MainActivityUpdate : AppCompatActivity(),
    NoteListAdapterUpdate.CallbackAdapter,
    SwipeCallbackMainUpdate.CallbackSwipeUpdate,
    BottomSheetCategory.CallbackCategory,
    NoteFragmentUpdate.CallbackNoteUpdate,
    NoteTrashFragmentUpdate.CallbackNoteTrashUpdate,
    SheetPrefSelectColor.CallbackColorMain,
    PrefScreenCardView.CallbackFontSizeUpdate,
    PrefScreenNumberLines.CallbackNumberLinesUpdate,
    SettingsAppFragmentUpdate.CallbackOneSizeCardsUpdate,
    SheetRestoreDateBase.CallbackRestoreNote,
    DateBaseCloudBackup.CallbackRestoreNoteCloud,
    SheetPrefSort.CallbackSort,
    FontsFragment.CallbackChangeTypeFont {

    companion object {
        //private val TAG = "MyLogs_${MainActivityUpdate::class.java.simpleName}"
        const val RECYCLER_STATE_EXERCISE = "recycler_state"
    }

    private lateinit var shortsCommand: AppShortsCommand
    private lateinit var widgetsHelper: WidgetHelperUpdate
    private lateinit var swipeCallback: SwipeCallbackMainUpdate
    private lateinit var snackbarUndo: SnackbarUndoUpdate

    private lateinit var coordLayout: CoordinatorLayout
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyList: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var cardView: CardView
    private lateinit var tvSelectedItemsCount: TextView
    private lateinit var bottomBar: BottomAppBar

    private val adapter: NoteListAdapterUpdate = NoteListAdapterUpdate()
    private var bundleState: Bundle? = null

    private var hasLinearList: Boolean = true
    private var isSelMode: Boolean = false
    private var isSearchMode: Boolean = false
    private var color: Int = 0
    private var sortingWay: Int = 0
    private var notesCategory: String = CATEGORY_ALL_NOTES
    private var searchText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setAppFonts()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        installCallbacks()
        configureRecyclerAdapter()
        configureRecyclerView()
        configureSearchView()
        initObjects()
        installSwiped()
        otherConfigurations()
        establishColorViews()
        installHandlers()
        updateQuicklyNotesList()
        receivingTextData()
    }

    private fun setAppFonts() {
        AppFont.setFont(this)
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.coord_layout_main)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view_notes)
        tvEmptyList = findViewById(R.id.tv_empty_list_up)
        fab = findViewById(R.id.fab_main)
        cardView = findViewById(R.id.card_view_main)
        tvSelectedItemsCount = findViewById(R.id.tv_selected_items_count)
        bottomBar = findViewById(R.id.bottom_bar_main)
    }

    private fun installCallbacks() {
        // Для работы с заметками
        NoteListAdapterUpdate.listenerCallback(this)
        SwipeCallbackMainUpdate.listenerCallback(this)
        BottomSheetCategory.listenerCallback(this)
        NoteFragmentUpdate.listenerCallback(this)
        NoteTrashFragmentUpdate.listenerCallback(this)
        // Для изменения настроек
        SheetPrefSelectColor.listenerCallBack(this)
        PrefScreenCardView.listenerCallback(this)
        PrefScreenNumberLines.listenerCallback(this)
        SettingsAppFragmentUpdate.listenerCallbackSize(this)

        SheetRestoreDateBase.listenerCallback(this)
        DateBaseCloudBackup.listenerCallback(this)
        SheetPrefSort.listenerCallback(this)
        FontsFragment.listenerCallback(this)
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

    private val notes: List<Note2>
        get() {
            return shortsCommand.getNotes(searchText, notesCategory, sortingWay)
        }

    private fun initObjects() {
        shortsCommand = AppShortsCommand()
        widgetsHelper = WidgetHelperUpdate(this, fab, bottomBar)
        snackbarUndo = SnackbarUndoUpdate(this, shortsCommand)
    }

    private fun installSwiped() {
        swipeCallback = SwipeCallbackMainUpdate(adapter, shortsCommand)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    private fun otherConfigurations() {
        sortingWay = AppPref.getSortingWay(this)
        widgetsHelper.setTypeNotes(recyclerView, hasLinearList)
    }

    private fun establishColorViews() {
        widgetsHelper.setColorViews(color)
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
                    unificationNotes()
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
                    DialogTrashUpdate(this).show(adapter.itemCount)
                } else {
                    IntentUtil.openNote(this, Note2(), notesCategory)
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
        widgetsHelper.setVisibleSelect(isSelMode)

        if (isSelMode) {
            widgetsHelper.setVisibleSearchAndSwitch(false)
            widgetsHelper.setNavigationIcon(false)
            widgetsHelper.setDeleteIconFab(notesCategory)

            if (isSearchMode) {
                widgetsHelper.bottomBarVisible(true)
            }

        } else {
            widgetsHelper.setVisibleUnification(false)

            if (isSearchMode) {
                widgetsHelper.setVisibleSearchAndSwitch(false)
                widgetsHelper.hideNavigationIcon()
                widgetsHelper.bottomBarVisible(false)
                widgetsHelper.setIconFab(WidgetHelperUpdate.ICON_FAB_SEARCH_CLOSE)
            } else {
                widgetsHelper.setVisibleSearchAndSwitch(true)
                widgetsHelper.setNavigationIcon(true)
                widgetsHelper.setIconFabCategory(notesCategory)
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
        widgetsHelper.setTypeNotes(recyclerView, hasLinearList)
        saveStateLinearList(hasLinearList)
    }

    private fun saveStateLinearList(hasLinearList: Boolean) {
        AppPref.setStateLinearList(this, hasLinearList)
    }

    private fun activationSearchMode(isSearchMode: Boolean) {
        this.isSearchMode = isSearchMode
        searchView.setQuery("", false)

        if (isSearchMode) {
            searchView.setVisibleView(true)
            widgetsHelper.bottomBarVisible(false)
            val searchEditText: EditText = searchView.findViewById(R.id.search_src_text)
            searchEditText.showKeyboard(this)
            widgetsHelper.setIconFab(WidgetHelperUpdate.ICON_FAB_SEARCH_CLOSE)
        } else {
            searchView.setVisibleView(false)
            widgetsHelper.bottomBarVisible(true)
            widgetsHelper.setNavigationIcon(true)
            widgetsHelper.setVisibleSearchAndSwitch(true)
            widgetsHelper.setIconFabCategory(notesCategory)
        }
    }

    private fun unificationNotes() {
        adapter.unificationItems(this, shortsCommand)
        onActivatingSelectionMode(false)
        updateQuicklyNotesListTop()
    }

    private fun updateQuicklyNotesListTop() {
        updateQuicklyNotesList()
        recyclerView.scrollToPosition(0)
    }

    private fun receivingTextData() {
        if (intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == intent.type) {
                intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                    DialogNoteReceivingData(this).show(it)
                }
            }
        }
    }

    // From recycler adapter
    override fun onClickingNote(note: Note2) {
        IntentUtil.openNote(this, note, notesCategory)
    }

    // From recycler adapter
    override fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean) {
        tvSelectedItemsCount.text = selectedItemsCount.toString()
        widgetsHelper.setSelectIcon(hasAllSelected)
        widgetsHelper.changeVisibleUnification(notesCategory, selectedItemsCount in 2..3)
    }

    // From note fragment
    override fun onNoteAdd() = updateQuicklyNotesListTop()

    // From note fragment
    override fun onNoteUpdate() = updateListNotes()

    // From swipe
    override fun onSwipe(note: Note2, isFavorite: Boolean) {
        updateListNotes()
        snackbarUndo.show(note, isFavorite)
    }

    // From note trash fragment and note fragment
    override fun onNoteDelete(note: Note2, isFavorite: Boolean) {
        lifecycleScope.launch {
            delay(200L)
            updateListNotes()
            snackbarUndo.show(note, isFavorite)
        }
    }

    // From note trash fragment
    override fun onNoteReset(note: Note2) {
        lifecycleScope.launch {
            delay(200L)
            updateListNotes()
            showSnackbar(SnackbarApp.MSG_NOTE_RESTORED, true)
        }
    }

    // From sheet category
    override fun onCallbackCategory(notesCategory: String) {
        this.notesCategory = notesCategory
        swipeCallback.setCategory(notesCategory)
        widgetsHelper.setIconFabCategory(notesCategory)

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
        SnackbarApp(this, coordLayout, fab).show(message, isSuccess)
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
        updateQuicklyNotesListTop()
    }

    override fun onChangeSortingWay(sortingWay: Int) {
        this.sortingWay = sortingWay
        updateQuicklyNotesListTop()
    }

    override fun onChangeTypeFont() {
        setAppFonts()
        recyclerView.adapter = adapter
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
        val state: Parcelable? = bundleState?.getParcelable(RECYCLER_STATE_EXERCISE)
        recyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    override fun onPause() {
        super.onPause()
        dismissSnackbars(true)
        saveRecyclerStata()
    }

    private fun saveRecyclerStata() {
        val listState: Parcelable? = recyclerView.layoutManager?.onSaveInstanceState()
        bundleState?.putParcelable(RECYCLER_STATE_EXERCISE, listState)
    }

    fun getColor(): Int {
        return color
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