package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.activities.ListActivity
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList
import com.loskon.noteminimalism3.ui.recyclerview.update.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.SwipeCallbackNote
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListAdapter
import com.loskon.noteminimalism3.ui.snackbars.SnackbarUndo
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewmodel.NoteViewModel
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_ALL_NOTES

/**
 * Форма списка заметок
 */

private val TAG = "MyLogs_${NoteListFragment3::class.java.simpleName}"

class NoteListFragment3 : Fragment(),
    NoteListAdapter.OnItemClickListener,
    SwipeCallbackNote.OnItemSwipeListener,
    NoteFragment.OnNote,
    BottomSheetCategory.CallbackCategory {

    private lateinit var activity: ListActivity
    private lateinit var viewModel: NoteViewModel

    private lateinit var widgetsHelper: WidgetHelperList
    private lateinit var snackbarUndo: SnackbarUndo

    // Activity views
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar

    // Fragment views
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var cardView: CardView
    private lateinit var tvNumber: TextView

    private val adapter: NoteListAdapter = NoteListAdapter()
    private lateinit var swipeCallback: SwipeCallbackNote

    private var notesCategory: String = CATEGORY_ALL_NOTES
    private var isTypeNotesOne = false
    private var isSearchMode = false
    private var isDeleteMode: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as ListActivity
        //adapter.setSettings(context)

        loadSharedPreferences()
        initWidgets()
    }

    private fun loadSharedPreferences() {
        isTypeNotesOne = GetSharedPref.getLinearList(activity)
    }

    private fun initWidgets() {
        widgetsHelper = activity.getWidgetsHelper
        fab = widgetsHelper.getFab
        bottomAppBar = widgetsHelper.getBottomAppBar
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_list_notes, container, false)
        initFragmentViews(view)
        configureRecyclerView()
        return view
    }

    private fun initFragmentViews(view: View) {
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view_notes)
        tvEmpty = view.findViewById(R.id.tv_empty_list)
        cardView = view.findViewById(R.id.card_view_main)
        tvNumber = view.findViewById(R.id.tv_selected_items_count)
    }

    private fun configureRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupWidgets()
        setupSearchView()
        initObjects()
        installSwiped()
        installCallbacks()
        installHandlers()
    }

    private fun setupObserver() {
        viewModel = activity.getViewModel

        viewModel.getNotes.observe(viewLifecycleOwner, { list ->
            list?.let {
                updateUI(list)
            }
        })
    }

    fun setupWidgets() {
        widgetsHelper.setVisibleWidgets(true)
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //handlingSearch(newText)
                return true
            }
        })
    }

    private fun handlingSearch(newText: String) {
        val searchText = newText.trim()

        if (TextUtils.isEmpty(searchText)) {
            viewModel.searchNameChanged("")
        } else {
            viewModel.searchNameChanged("%$searchText%")
        }

        recyclerView.scrollToPosition(0)
    }

    private fun initObjects() {
     //   snackbarUndo = SnackbarUndo(activity, widgetsHelper, viewModel)
    }

    private fun installSwiped() {
        swipeCallback = SwipeCallbackNote(adapter, viewModel)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    private fun installCallbacks() {
        NoteListAdapter.setClickListener(this)
        SwipeCallbackNote.setSwipeListener(this)
        NoteFragment.setNoteListener(this)
        BottomSheetCategory.listenerCallback(this)
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
/*                    if (isAllSel) {
                        viewModel.activateCheckedStatus()
                    } else {
                        viewModel.disableCheckedStatus()
                    }*/
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

    private fun handlingFabClick() {
        if (isDeleteMode) {
            onDeleteItemsClick()
        } else {
            onOtherClick()
        }
    }

    private fun onDeleteItemsClick() {
        if (notesCategory == CATEGORY_ALL_NOTES) {
            viewModel.deleteItems()
        } else {
            viewModel.deleteItemsAlways()
        }

        onDeleteMode(false)
    }

    private fun onOtherClick() {
        if (isSearchMode) {
            searchModeDisabled()
        } else {
            activity.openNoteFragment(Note2())
        }
    }

    private fun handlingSwitchClick() {
        isTypeNotesOne = !isTypeNotesOne
        widgetsHelper.setTypeNotes(recyclerView, isTypeNotesOne)
        MySharedPref.setBoolean(activity, MyPrefKey.KEY_TYPE_NOTES, isTypeNotesOne)
    }

    private fun searchModeActivated() {
        isSearchMode = true
        searchView.setVisibleView(true)

        widgetsHelper.changeBarVisible(false)
        widgetsHelper.setIconFab(WidgetHelperList.ICON_FAB_SEARCH_CLOSE)

        val searchText: EditText = searchView.findViewById(R.id.search_src_text)
        searchText.showKeyboard(activity)
    }

    private fun searchModeDisabled() {
        isSearchMode = false
        searchView.setVisibleView(false)

        widgetsHelper.changeBarVisible(true)
        widgetsHelper.setIconFab(WidgetHelperList.ICON_FAB_ADD)

        searchView.setQuery("", false)
        recyclerView.scrollToPosition(0)
        //viewModel.searchNameChanged("")
    }

    private fun handlingNavBtnClick() {
        snackbarUndo.close()

        if (isDeleteMode) {
            onDeleteMode(false)
        } else {
            val dialogBottomSheet = BottomSheetCategory.newInstance(notesCategory)
            dialogBottomSheet.show(childFragmentManager, BottomSheetCategory.TAG)
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
        activity.openNoteFragment(note)
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

    override fun onItemSwipe(note: Note2,isFav: Boolean, category: String) {
        //snackbarUndo.show(note, category)
    }

    override fun onNoteDelete(note: Note2) {
        TODO("Not yet implemented")
    }

    override fun onNoteAdd(note: Note2) {
        isTop = true
    }

    private var isTop: Boolean = false


    override fun onCallbackCategory(category: String) {
        Log.d(TAG, "Notes category: $category")
        onClickMenuItem(category)
    }


    // instance
    companion object {
        fun newInstance(): NoteListFragment3 {
            return NoteListFragment3()
        }
    }

}