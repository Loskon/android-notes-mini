package com.loskon.noteminimalism3.ui.activities

import android.graphics.Typeface
import android.os.Bundle
import android.view.KeyEvent
import android.widget.EditText
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
import com.loskon.noteminimalism3.managers.setBackgroundTintColor
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.other.ListActivityHelper
import com.loskon.noteminimalism3.other.QueryTextListener
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.ui.dialogs.DeleteForeverWarningDialog
import com.loskon.noteminimalism3.ui.dialogs.SendToTrashWarningDialog
import com.loskon.noteminimalism3.ui.dialogs.UnificationDialog
import com.loskon.noteminimalism3.ui.fragments.*
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenCardView
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenNumberLines
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenResetColor
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.notes.NoteListAdapter
import com.loskon.noteminimalism3.ui.recyclerview.notes.SwipeCallback
import com.loskon.noteminimalism3.ui.sheets.ListRestoreSheetDialog
import com.loskon.noteminimalism3.ui.sheets.SelectColorHexSheetDialog
import com.loskon.noteminimalism3.ui.sheets.SelectColorPickerSheetDialog
import com.loskon.noteminimalism3.ui.sheets.SortWaySheetDialog
import com.loskon.noteminimalism3.ui.snackbars.BaseWarningSnackbars
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import com.loskon.noteminimalism3.ui.snackbars.SnackbarUndo
import com.loskon.noteminimalism3.utils.*

/**
 * Основное activity для работы со списком
 */

class MainActivity : AppBaseActivity(),
    NoteListAdapter.NoteListAdapterCallback,
    SwipeCallback.NoteSwipeCallback,
    CategorySheetFragment.CallbackCategory,
    NoteFragment.NoteCallback,
    NoteTrashFragment.NoteTrashCallback,
    SelectColorPickerSheetDialog.ColorListCallback,
    SelectColorHexSheetDialog.ColorHexListCallback,
    PrefScreenResetColor.ColorResetListCallback,
    PrefScreenCardView.FontsSizesCallback,
    PrefScreenNumberLines.NumberLinesCallback,
    SettingsAppFragment.OneSizeCardsCallback,
    ListRestoreSheetDialog.RestoreNoteCallback,
    DataBaseCloudBackup.RestoreNoteCloudCallback,
    BackupFragment.RestoreNoteAndroidRCallback,
    SortWaySheetDialog.SortWayCallback,
    FontsFragment.TypeFontCallback {

    private val commandCenter: CommandCenter = CommandCenter()
    private val adapter: NoteListAdapter = NoteListAdapter()

    private lateinit var helper: ListActivityHelper
    private lateinit var swipeCallback: SwipeCallback
    private lateinit var snackbarUndo: SnackbarUndo

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
        establishColorViews()
        configureRecyclerAdapter()
        configureRecyclerView()
        differentConfigurations()
        installSwipeCallback()
        configureSearchView()
        installHandlersForViews()
        updateQuicklyNotesList()
    }

    private fun installCallbacks() {
        // Для работы с заметками
        NoteListAdapter.registerCallbackNoteListAdapter(this)
        SwipeCallback.registerCallbackNoteSwipe(this)
        NoteFragment.registerCallbackNote(this)
        NoteTrashFragment.registerCallbackNoteTrash(this)
        // Для изменения настроек
        SelectColorPickerSheetDialog.registerCallbackColorList(this)
        SelectColorHexSheetDialog.registerCallbackColorList(this)
        PrefScreenResetColor.registerCallbackColorList(this)
        PrefScreenCardView.registerCallbackFontSizes(this)
        PrefScreenNumberLines.registerCallbackNumberLines(this)
        SettingsAppFragment.registerCallbackOneSizeCards(this)
        ListRestoreSheetDialog.registerCallbackRestoreNote(this)
        DataBaseCloudBackup.registerCallbackRestoreNoteCloud(this)
        BackupFragment.registerCallbackRestoreNoteAndroidR(this)
        SortWaySheetDialog.registerCallbackSortWay(this)
        FontsFragment.registerCallbackTypeFont(this)
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
        helper = ListActivityHelper(this, fab, bottomBar)
        snackbarUndo = SnackbarUndo(this, commandCenter, coordLayout, fab)
    }

    private fun getSomeSharedPreferences() {
        color = PrefHelper.getAppColor(this)
        sortingWay = PrefHelper.getSortingWay(this)
        hasLinearList = PrefHelper.hasLinearList(this)
    }

    private fun establishColorViews() {
        helper.setColorViews(color)
        cardView.setBackgroundTintColor(color)
    }

    private fun configureRecyclerAdapter() {
        adapter.setViewColor(color)
        adapter.setLinearList(hasLinearList)

        val radiusStrokeDp: Int = ValueUtil.getRadiusLinLay(this)
        val boredStrokeDp: Int = ValueUtil.getStrokeLinLay(this)
        adapter.setViewSizes(radiusStrokeDp, boredStrokeDp)

        val titleFontSize: Int = PrefHelper.getTitleFontSize(this)
        val dateFontSize: Int = PrefHelper.getDateFontSize(this)
        adapter.setFontSizes(titleFontSize, dateFontSize)

        val numberLines: Int = PrefHelper.getNumberLines(this)
        adapter.setNumberLines(numberLines)

        val hasOneSizeCards: Boolean = PrefHelper.hasOneSizeCards(this)
        adapter.setOneSizeCards(hasOneSizeCards)
    }

    private fun configureRecyclerView() {
        recyclerView.changeLayoutManager(hasLinearList)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = AppItemAnimator()
    }

    private fun RecyclerView.changeLayoutManager(hasLinearList: Boolean) {
        layoutManager = if (hasLinearList) {
            LinearLayoutManager(context)
        } else {
            StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
        }
    }

    private fun differentConfigurations() {
        helper.changeMenuItemForLinearList(hasLinearList)
    }

    private fun installSwipeCallback() {
        swipeCallback = SwipeCallback(adapter, commandCenter)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    private fun configureSearchView() {
        searchView.setOnQueryTextListener(object : QueryTextListener() {
            override fun queryTextChange(query: String?) {
                searchText = query
                updateQuicklyNotesList()
            }
        })
    }

    private fun updateQuicklyNotesList() {
        val notes: List<Note> = noteList
        adapter.setQuicklyNotesList(notes)
        tvEmpty.setVisibleView(notes.isEmpty())
    }

    private val noteList: List<Note>
        get() {
            return commandCenter.getNotes(searchText, category, sortingWay)
        }

    private fun installHandlersForViews() {
        fab.setOnSingleClickListener { clickingFab() }

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
                    transitionSearchMode(true)
                    true
                }

                R.id.action_unification -> {
                    UnificationDialog(this).show()
                    true
                }

                R.id.action_favorite -> {
                    adapter.changeFavoriteStatus(this, commandCenter)
                    true
                }

                else -> false
            }
        }
    }

    private fun clickingFab() {
        if (isSelectionMode) {
            pressingInDeleteMode()
        } else {
            pressingInNormally()
        }
    }

    private fun pressingInDeleteMode() {
        if (category == CATEGORY_TRASH) {
            DeleteForeverWarningDialog(this).show()
        } else {
            sendItemsToTrash()
        }
    }

    private fun pressingInNormally() {
        if (isSearchMode) {
            transitionSearchMode(false)
        } else {
            if (category == CATEGORY_TRASH) {
                SendToTrashWarningDialog(this).show(adapter.itemCount)
            } else {
                IntentManager.openNote(this, Note(), category)
            }
        }
    }

    private fun sendItemsToTrash() {
        adapter.sendItemsToTrash(commandCenter)
        onActivatingSelectionMode(false)
        updateListNotes()
    }

    override fun onActivatingSelectionMode(isSelMode: Boolean) {
        this.isSelectionMode = isSelMode
        dismissSnackbars(isSelMode)
        selectionModeStatus()
    }

    private fun selectionModeStatus() {
        cardView.setVisibleView(isSelectionMode)
        swipeCallback.blockSwiped(isSelectionMode)
        helper.setVisibleSelect(isSelectionMode)

        if (isSelectionMode) {
            helper.setVisibleSearchAndSwitch(false)
            helper.setNavigationIcon(false)
            helper.setDeleteIconFab(category)

            if (isSearchMode) {
                helper.bottomBarVisible(true)
            }

        } else {
            helper.setVisibleUnification(false)
            helper.setVisibleFavorite(category, false)

            if (isSearchMode) {
                helper.setVisibleSearchAndSwitch(false)
                helper.hideNavigationIcon()
                helper.bottomBarVisible(false)
                helper.setIconFab(ListActivityHelper.ICON_FAB_SEARCH_CLOSE)
            } else {
                helper.setVisibleSearchAndSwitch(true)
                helper.setNavigationIcon(true)
                helper.setIconFabCategory(category)
            }

            adapter.disableSelectionMode()
        }
    }

    fun updateListNotes() {
        val notes: List<Note> = noteList
        adapter.setNotesList(notes)
        tvEmpty.setVisibleView(notes.isEmpty())
    }

    private fun dismissSnackbars(isDisSnackMessage: Boolean) {
        if (isDisSnackMessage) BaseWarningSnackbars.dismiss()
        snackbarUndo.dismiss()
    }

    private fun clickingNavigationButton() {
        dismissSnackbars(true)

        if (isSelectionMode) {
            cancelSelectionMode()
        } else {
            openBottomSheetCategory()
        }
    }

    private fun cancelSelectionMode() {
        onActivatingSelectionMode(false)
        adapter.updateChangedList()
    }

    private fun openBottomSheetCategory() {
        val bottomSheet: DialogFragment = CategorySheetFragment.newInstance(category)
        bottomSheet.onlyShow(supportFragmentManager, CategorySheetFragment.TAG)
    }

    private fun clickingMenuSwitch() {
        hasLinearList = !hasLinearList
        adapter.setLinearList(hasLinearList)
        helper.changeMenuItemForLinearList(hasLinearList)
        recyclerView.changeLayoutManager(hasLinearList)
        PrefHelper.setStateLinearList(this, hasLinearList)
    }

    private fun transitionSearchMode(isSearchMode: Boolean) {
        this.isSearchMode = isSearchMode
        searchView.setQuery("", false)

        if (isSearchMode) {
            searchView.setVisibleView(true)
            helper.bottomBarVisible(false)
            val searchEditText: EditText = searchView.findViewById(R.id.search_src_text)
            searchEditText.showKeyboard(this)
            helper.setIconFab(ListActivityHelper.ICON_FAB_SEARCH_CLOSE)
        } else {
            searchView.setVisibleView(false)
            helper.bottomBarVisible(true)
            helper.setNavigationIcon(true)
            helper.setVisibleSearchAndSwitch(true)
            helper.setIconFabCategory(category)
        }
    }

    private fun updateQuicklyNotesListTop() {
        updateQuicklyNotesList()
        recyclerView.scrollToPosition(0)
    }

    // From recycler adapter
    override fun onClickingNote(note: Note) {
        IntentManager.openNote(this, note, category)
    }

    // From recycler adapter
    override fun onSelectingNote(selectedItemsCount: Int, hasAllSelected: Boolean) {
        tvCountItems.text = selectedItemsCount.toString()
        helper.changeIconMenuSelect(hasAllSelected)
        helper.changeVisibleUnification(category, selectedItemsCount >= 2)
        helper.setVisibleFavorite(category, selectedItemsCount in 1..1)
    }

    // From recycler adapter
    override fun onVisibleFavorite(note: Note) {
        helper.changeMenuIconFavorite(note.isFavorite)
    }

    // From note fragment
    override fun onNoteAdd() = updateQuicklyNotesListTop()

    // From note fragment
    override fun onNoteUpdate() = updateQuicklyNotesList()

    // From note fragment
    override fun onSendToTrash(note: Note, hasFavStatus: Boolean, isBottomWidgetShow: Boolean) {
        updateQuicklyNotesList()
        if (isBottomWidgetShow) showSnackbarUndo(note, hasFavStatus)
    }

    // From note trash fragment
    override fun onNoteDelete(note: Note, hasFavStatus: Boolean) {
        updateQuicklyNotesList()
        showSnackbarUndo(note, hasFavStatus)
    }

    // From swipe
    override fun onSwipeDelete(note: Note, hasFavStatus: Boolean) {
        updateListNotes()
        showSnackbarUndo(note, hasFavStatus)
    }

    private fun showSnackbarUndo(note: Note, hasFavStatus: Boolean) {
        snackbarUndo.show(note, hasFavStatus, category)
    }

    // From note trash fragment
    override fun onNoteReset(note: Note) {
        updateQuicklyNotesList()
        showSnackbar(SnackbarControl.MSG_NOTE_RESTORED)
    }

    // From sheet category
    override fun onCallbackCategory(category: String) {
        this.category = category
        swipeCallback.setCategory(category)
        helper.setIconFabCategory(category)
        updateQuicklyNotesList()
        scrollUpWithProtection()
    }

    private fun scrollUpWithProtection() {
        // Защита от создания пустого пространства над списком
        // при использовании StaggeredGridLayoutManager
        if (noteList.isNotEmpty()) {
            recyclerView.scrollToPosition(0)
        }
    }

    fun showSnackbar(message: String) {
        SnackbarControl(coordLayout, fab).show(message)
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
        category = CATEGORY_ALL_NOTES
        helper.setIconFabCategory(category)
        updateQuicklyNotesListTop()
    }

    override fun onChangeSortingWay(sortingWay: Int) {
        this.sortingWay = sortingWay
        updateQuicklyNotesListTop()
    }

    override fun onChangeTypeFont(typeFace: Typeface?) {
        FontManager.setFont(this)
        recyclerView.adapter = adapter
        typeFace?.let { tvEmpty.typeface = it }
    }

    // Внешние методы
    fun cleanTrash() {
        commandCenter.cleanTrash()
        updateQuicklyNotesList()
    }

    fun deleteItemsForever() {
        adapter.deleteItems(commandCenter)
        onActivatingSelectionMode(false)
        updateListNotes()
    }

    fun performUnificationNotes() {
        adapter.unification(this, commandCenter)
        onActivatingSelectionMode(false)
        updateQuicklyNotesListTop()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            when {
                isSelectionMode -> {
                    cancelSelectionMode()
                    false
                }
                isSearchMode -> {
                    transitionSearchMode(false)
                    false
                }
                else -> {
                    finish()
                    true
                }
            }
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        dismissSnackbars(true)
    }
}