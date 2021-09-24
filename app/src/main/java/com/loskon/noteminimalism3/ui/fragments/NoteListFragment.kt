package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
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
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList.Companion.ICON_FAB_ADD
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList.Companion.ICON_FAB_DELETE
import com.loskon.noteminimalism3.ui.activities.WidgetHelperList.Companion.ICON_FAB_SEARCH_CLOSE
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
 * Форма списка
 */

class NoteListFragment :
    Fragment(),
    NoteListAdapter.OnItemClickListener,
    SwipeCallbackNote.OnItemSwipeListener,
    NoteFragment.OnNote {

    private lateinit var activity: ListActivity

    private lateinit var widgetsHelper: WidgetHelperList
    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar

    private lateinit var snackbarUndo: SnackbarUndo
    private lateinit var viewModel: NoteViewModel

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var cardView: CardView
    private lateinit var tvNumber: TextView

    private lateinit var adapter: NoteListAdapter
    private lateinit var swipeCallback: SwipeCallbackNote
    private var bundleState: Bundle? = null

    private var notesCategory: String = CATEGORY_ALL_NOTES
    private var isTypeNotesOne = false
    private var isSearchMode = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isTypeNotesOne = GetSharedPref.isTypeSingle(context)
        //viewModel = (context as ListActivity).getViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_notes, container, false)
        initViews(view)
        configuratorViews()
        configureRecyclerView()
        //setSwiped()

        return view
    }

    private fun initViews(view: View) {
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view_notes)
        tvEmpty = view.findViewById(R.id.tv_empty_list)
        cardView = view.findViewById(R.id.cardViewMain)
        tvNumber = view.findViewById(R.id.tv_font_size_title)
    }

    private fun configuratorViews() {
        //searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val searchText = newText.trim()

                if (TextUtils.isEmpty(searchText)) {
                    viewModel.searchNameChanged("")
                } else {
                    val searchTextQuery = "%$searchText%"
                    viewModel.searchNameChanged(searchTextQuery)
                }

                goTopRecyclerPosition()

                return true
            }
        })
    }

    private fun configureRecyclerView() {
        adapter = NoteListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = CustomItemAnimator()
    }

    private fun setSwiped() {
        swipeCallback = SwipeCallbackNote(adapter, viewModel)
        ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity() as ListActivity

        setupObserver()
        loadingEntireList()

        initWidgets()
        initObjects()
        installCallbacks()
        installHandlers()
    }

    private fun initWidgets() {
        widgetsHelper = activity.getWidgetsHelper
        fab = widgetsHelper.getFab
        bottomAppBar = widgetsHelper.getBottomAppBar

        //widgetsHelper.setVisibleSelect(false)
        widgetsHelper.setVisibleUnification(false)
    }

    private fun initObjects() {
       // snackbarUndo = SnackbarUndo(activity, widgetsHelper, viewModel)
    }

    private fun installCallbacks() {
        NoteListAdapter.setClickListener(this)
        SwipeCallbackNote.setSwipeListener(this)
        NoteFragment.setNoteListener(this)
    }

    private fun installHandlers() {
        fab.setOnClickListener {
            if (isDeleteMode) {

                if (notesCategory == CATEGORY_ALL_NOTES) {
                    viewModel.deleteItems()
                } else {
                    viewModel.deleteItemsAlways()
                }

                onDeleteMode(false)
            } else {
                if (isSearchMode) {
                    searchModeDisabled()
                } else {
                    activity.openNoteFragment(Note2())
                }
            }
        }

        bottomAppBar.setOnSingleClickListener {
            snackbarUndo.close()

            if (isDeleteMode) {
                onDeleteMode(false)
            } else {
                //val dialogBottomSheet = BottomSheetCategory.newInstance(this, notesCategory)
                // dialogBottomSheet.show(activity.supportFragmentManager, BottomSheetCategory.TAG)
            }
        }

        bottomAppBar.setOnMenuItemClickListener { item ->
            snackbarUndo.close()
            when (item.itemId) {
                R.id.action_switch_view -> {
                    isTypeNotesOne = !isTypeNotesOne
                    widgetsHelper.setTypeNotes(recyclerView, isTypeNotesOne)
                    MySharedPref.setBoolean(activity, MyPrefKey.KEY_TYPE_NOTES, isTypeNotesOne)
                    true
                }
                R.id.action_select_item -> {
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

    private fun searchModeActivated() {
        isSearchMode = true
        searchView.setVisibleView(true)

        val searchText: EditText = searchView.findViewById(R.id.search_src_text)
        searchText.showKeyboard(activity)

        widgetsHelper.changeBarVisible(false)
        widgetsHelper.setIconFab(ICON_FAB_SEARCH_CLOSE)
    }

    private fun searchModeDisabled() {
        isSearchMode = false
        searchView.setVisibleView(false)

        searchView.setQuery("", false)

        widgetsHelper.changeBarVisible(true)
        widgetsHelper.setIconFab(ICON_FAB_ADD)

        goTopRecyclerPosition()
        //viewModel.searchNameChanged("")
    }

    private val dataObserver = Observer<List<Note2>> { list ->
        list?.let {

            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                Log.d(TAG, list.size.toString() + " RESUMED")
                list?.let {
                    updateUI(list)
                }
            } else if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.STARTED) {
                Log.d(TAG, list.size.toString() + " STARTED")
                list?.let {
                    updateUI(list)
                }
            }

        }
    }


    private fun setupObserver() {
        Log.d(TAG, "Set observe")
        //viewModel.getNotesById.observe(viewLifecycleOwner, dataObserver)
    }

    private fun loadingEntireList() {
        viewModel.searchNameChanged("")
        selectNotesCategory(notesCategory)
    }

    private fun selectNotesCategory(category: String) {
        notesCategory = category
        viewModel.categoryNotes(notesCategory)
    }

    private fun updateUI(list: List<Note2>) {
        tvEmpty.setVisibleView(list.isEmpty())
        // context?.showToast("" + list.size)
        adapter.setListNote(list)
    }

    override fun onResume() {
        super.onResume()
        // resetRecyclerState()
        //if (isTop) goTopRecyclerPosition()
    }

    private fun resetRecyclerState() {
        val state: Parcelable? = bundleState?.getParcelable(RECYCLER_STATE_NOTES)
        recyclerView.layoutManager?.onRestoreInstanceState(state)
    }

    private fun goTopRecyclerPosition() {
        recyclerView.scrollToPosition(0)
    }

    override fun onPause() {
        super.onPause()
        //Log.d("NoteListFragment", "onPause")
        snackbarUndo.close()
        //  saveRecyclerStata()
    }

    private fun saveRecyclerStata() {
        val listState: Parcelable? = recyclerView.layoutManager?.onSaveInstanceState()
        bundleState?.putParcelable(RECYCLER_STATE_NOTES, listState)
    }

    // For others methods
    fun onClickMenuItem(category: String) {
        swipeCallback.setCategory(category)
        selectNotesCategory(category)
        goTopRecyclerPosition()
    }


    // Callbacks
    override fun onItemClick(note: Note2) {
        activity.openNoteFragment(note)
    }

    override fun onSelectedItem(note: Note2) {
        note.isChecked = !note.isChecked
       // tvNumber.text = numSelItem.toString()
        viewModel.update(note)
    }

    private var isDeleteMode: Boolean = false

    override fun onDeleteMode(isDelMode: Boolean) {
        isDeleteMode = isDelMode
        deleteMode()
    }

    private fun deleteMode() {
        if (isDeleteMode) {
            cardView.setVisibleView(true)
            swipeCallback.setBlockSwiped(true)

            widgetsHelper.setIconFab(ICON_FAB_DELETE)
            widgetsHelper.setVisibleList(false)
            // widgetsHelper.setNavigationIcon(R.drawable.baseline_close_black_24)

            if (isSearchMode) widgetsHelper.changeBarVisible(true)

        } else {
            cardView.setVisibleView(false)
            swipeCallback.setBlockSwiped(false)
            adapter.disableDeleteMode()

            widgetsHelper.setIconFab(notesCategory, isSearchMode)


            widgetsHelper.setVisibleList(true)
            //  widgetsHelper.setNavigationIcon(R.drawable.baseline_menu_black_24)

            viewModel.disableCheckedStatus()
        }
    }

    override fun onItemSwipe(note: Note2, isFav : Boolean, category: String) {
        snackbarUndo.show(note, isFav, category)
    }

    override fun onNoteDelete(note: Note2) {
        snackbarUndo.show(note,false, "")
    }

    private var isTop: Boolean = false

    override fun onNoteAdd(note: Note2) {
        viewModel.insert(note)
    }


    // Instance
    companion object {
        private val TAG = NoteListFragment::class.java.simpleName

        const val RECYCLER_STATE_NOTES = "recycler_state_notes"

        @JvmStatic
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }

    override fun onX() {

    }

    override fun onNumSelItem(isAll: Boolean, numSelItem: Int) {
        tvNumber.text = numSelItem.toString()
    }
}