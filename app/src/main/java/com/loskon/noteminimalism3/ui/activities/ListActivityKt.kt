package com.loskon.noteminimalism3.ui.activities

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.auxiliary.other.MyIntent
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.fragments.BottomSheetCategory
import com.loskon.noteminimalism3.ui.fragments.NoteFragmentKt
import com.loskon.noteminimalism3.ui.fragments.NoteTrashFragment
import com.loskon.noteminimalism3.ui.recyclerview.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.SwipeCallbackNote
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListAdapter
import com.loskon.noteminimalism3.ui.snackbars.SnackbarMessage
import com.loskon.noteminimalism3.ui.snackbars.SnackbarUndo
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.utils.showToast
import com.loskon.noteminimalism3.viewmodel.NoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Основное activity для работы со списком
 */

class ListActivityKt : AppCompatActivity(),
    NoteListAdapter.OnItemClickListener,
    SwipeCallbackNote.OnItemSwipeListener,
    NoteFragmentKt.OnNote2,
    BottomSheetCategory.OnNavViewListener,
    NoteTrashFragment.CallbackNoteTrash {

    companion object {
        private val TAG = "MyLogs_${ListActivityKt::class.java.simpleName}"
    }

    private lateinit var viewModel: NoteViewModel
    private val adapter: NoteListAdapter = NoteListAdapter()

    private lateinit var widgetsHelper: WidgetHelperKt
    private lateinit var snackbarUndo: SnackbarUndo
    private lateinit var swipeCallback: SwipeCallbackNote

    private lateinit var coordLayout: CoordinatorLayout
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var cardView: CardView
    private lateinit var tvNumber: TextView
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar

    private var notesCategory: String = NoteViewModel.CATEGORY_ALL_NOTES
    private var isTypeNotesOne = false
    private var isSearchMode = false
    private var isDeleteMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        setupObserver()
        initViews()
        installCallbacks()
        initObjects()
        configureRecyclerView()
        installSwiped()
        installHandlers()
    }

    private fun setupObserver() {
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        viewModel.getNotes.observe(this, { list ->
            list?.let {
                updateUI(list)
            }
        })
    }

    private fun initViews() {
        coordLayout = findViewById(R.id.coord_layout_new)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view_notes)
        tvEmpty = findViewById(R.id.text_empty_new)
        cardView = findViewById(R.id.card_view_new)
        tvNumber = findViewById(R.id.tv_number_new)
        fab = findViewById(R.id.fab_new)
        bottomAppBar = findViewById(R.id.btm_app_bar_new)

        cardView.backgroundTintList = ColorStateList.valueOf(MyColor.getMyColor(this))
    }

    private fun initObjects() {
        widgetsHelper = WidgetHelperKt(this)
        snackbarUndo = SnackbarUndo(this, viewModel)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setSettings(this)
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun installSwiped() {
        swipeCallback = SwipeCallbackNote(adapter, viewModel)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    private fun installCallbacks() {
        NoteListAdapter.setClickListener(this)
        SwipeCallbackNote.setSwipeListener(this)
        NoteFragmentKt.setNoteListener(this)
        BottomSheetCategory.setNavViewListener(this)
        NoteTrashFragment.setNoteListener(this)
    }

    private fun installHandlers() {
        fab.setOnSingleClickListener {
            handlingFabClick()
        }

        bottomAppBar.setNavigationOnClickListener {
            handlingNavBtnClick()
        }

        bottomAppBar.setOnMenuItemClickListener { item ->
            snackbarUndo.close()

            when (item.itemId) {
                R.id.action_switch_view -> {
                    handlingSwitchClick()
                    true
                }
                R.id.action_select_item -> {
                    adapter.selectAllItems()
                    selectAllItems(isAllSel)
                    true
                }

                R.id.action_search -> {
                    searchModeActivated()
                    true
                }

                R.id.action_unification -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun selectAllItems(isSel: Boolean) {
        for (note in adapter.getListNote()) {
            if (notesCategory != NoteViewModel.CATEGORY_TRASH) note.dateDelete = Date()
            note.isChecked = isSel
            viewModel.update(note)
        }
    }

    private fun handlingFabClick() {
        if (isDeleteMode) {
            onDeleteItemsClick()
        } else {
            onOtherClick()
        }
    }

    private fun onDeleteItemsClick() {
        if (notesCategory == NoteViewModel.CATEGORY_TRASH) {
            viewModel.deleteItemsAlways()
        } else {
            viewModel.deleteItems()
        }

        onDeleteMode(false)
    }

    private fun onOtherClick() {
        if (isSearchMode) {
            searchModeDisabled()
        } else {
            MyIntent.addNewNote2(this, Note2(), notesCategory)
        }
    }

    private fun searchModeActivated() {
        isSearchMode = true
        searchView.setVisibleView(true)

        widgetsHelper.changeBarVisible(false)
        widgetsHelper.setIconFab(WidgetHelperList.ICON_FAB_SEARCH_CLOSE)

        val searchText: EditText = searchView.findViewById(R.id.search_src_text)
        searchText.showKeyboard(this)
    }

    private fun searchModeDisabled() {
        isSearchMode = false
        searchView.setVisibleView(false)

        widgetsHelper.changeBarVisible(true)
        widgetsHelper.setIconFab(WidgetHelperList.ICON_FAB_ADD)

        searchView.setQuery("", false)
        recyclerView.scrollToPosition(0)
    }

    private fun handlingSwitchClick() {
        isTypeNotesOne = !isTypeNotesOne
        widgetsHelper.setTypeNotes(recyclerView, isTypeNotesOne)
        MySharedPref.setBoolean(this, MyPrefKey.KEY_TYPE_NOTES, isTypeNotesOne)
    }

    private fun handlingNavBtnClick() {
        snackbarUndo.close()

        if (isDeleteMode) {
            onDeleteMode(false)
        } else {
            val dialogBottomSheet = BottomSheetCategory.newInstance(notesCategory)
            dialogBottomSheet.show(supportFragmentManager, BottomSheetCategory.TAG)
        }
    }

    private fun updateUI(list: List<Note2>) {
        Log.d(TAG, "Notes size: ${list.size}")
        tvEmpty.setVisibleView(list.isEmpty())
        if (isTop) {
            scrollInTop(list)
        } else {
            adapter.setListNote(list)
        }
    }

    private fun scrollInTop(list: List<Note2>) {
        recyclerView.scrollToPosition(0)
        isTop = false
        adapter.setListNote2(list)
    }

    // For others methods
    private fun onClickMenuItem(category: String) {
        swipeCallback.setCategory(category)
        selectNotesCategory(category)
        recyclerView.scrollToPosition(0)
    }

    private fun selectNotesCategory(category: String) {
        notesCategory = category
        viewModel.categoryNotes(notesCategory)
    }


    // Callbacks
    override fun onItemClick(note: Note2) {
        MyIntent.addNewNote2(this, note, notesCategory)
    }

    override fun onSelectedItem(note: Note2) {
        note.isChecked = !note.isChecked
        viewModel.update(note)
    }

    override fun onNumSelItem(isAll: Boolean, numSelItem: Int) {
        isAllSel = isAll
        tvNumber.text = numSelItem.toString()
        widgetsHelper.setSelectIcon(isAll)
    }

    private var isAllSel: Boolean = false

    override fun onDeleteMode(isDelMode: Boolean) {
        isDeleteMode = isDelMode
        deleteMode()
    }

    override fun onX() {
        viewModel.disableCheckedStatus()
    }


    override fun onDeleteFromTrash(note: Note2, isDel: Boolean) {
        if (isDel) {
            lifecycleScope.launch {
                delay(500L)
                note.dateDelete = Date()
                snackbarUndo.show(note, false, notesCategory)
            }
        } else {
            this.showToast("hi")
        }

    }

    private fun deleteMode() {
        if (isDeleteMode) {
            cardView.setVisibleView(true)
            swipeCallback.setBlockSwiped(true)
            widgetsHelper.setVisibleSelect(true)
            widgetsHelper.setIconFab(WidgetHelperList.ICON_FAB_DELETE)
            widgetsHelper.setVisibleList(false)
            widgetsHelper.setNavigationIcon(false)

            if (isSearchMode) widgetsHelper.changeBarVisible(true)

        } else {
            cardView.setVisibleView(false)
            swipeCallback.setBlockSwiped(false)
            widgetsHelper.setVisibleSelect(false)
            widgetsHelper.setIconFab(notesCategory, isSearchMode)
            widgetsHelper.setVisibleList(true)
            widgetsHelper.setNavigationIcon(true)

            adapter.disableDeleteMode()
            viewModel.disableCheckedStatus()
        }
    }

    override fun onItemSwipe(note: Note2, isFav: Boolean, category: String) {
        snackbarUndo.show(note, isFav, category)
    }

    override fun onNoteDelete2(note: Note2, isFav: Boolean) {
        lifecycleScope.launch {
            delay(500L)
            note.dateDelete = Date()
            viewModel.update(note)
            snackbarUndo.show(note, isFav, notesCategory)
        }
    }

    override fun onNoteAdd2() {
        isTop = true
    }

    private var isTop: Boolean = false


    override fun onCallback(category: String) {
        Log.d(TAG, "Notes category: $category")
        onClickMenuItem(category)
    }


    private fun showSnackbar(message: String, isSuccess: Boolean) {
        SnackbarMessage(this, coordLayout, fab).show(message, isSuccess)
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
}