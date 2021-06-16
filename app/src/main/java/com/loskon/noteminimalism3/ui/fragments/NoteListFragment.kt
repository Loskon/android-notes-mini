package com.loskon.noteminimalism3.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.loskon.noteminimalism3.ui.recyclerview.CustomItemAnimator
import com.loskon.noteminimalism3.ui.recyclerview.SwipeCallbackNote
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListAdapter
import com.loskon.noteminimalism3.ui.snackbars.SnackbarUndo
import com.loskon.noteminimalism3.utils.setOnSingleClickListener
import com.loskon.noteminimalism3.utils.setVisibleView
import com.loskon.noteminimalism3.utils.showToast
import com.loskon.noteminimalism3.viewmodel.NoteViewModel
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Category.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Category.CATEGORY_SEARCH

/**
 * Форма списка
 */

const val RECYCLER_STATE_NOTES = "recycler_state_notes"

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
    private val viewModel: NoteViewModel by viewModels()

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    private lateinit var adapter: NoteListAdapter
    private lateinit var swipeCallback: SwipeCallbackNote
    private var bundleState: Bundle? = null

    private var notesCategory: String = CATEGORY_ALL_NOTES
    private var isTypeNotesOne = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isTypeNotesOne = GetSharedPref.isTypeSingle(context)
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
        setSwiped()
        setupObserver()
        return view
    }

    private fun initViews(view: View) {
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view_notes)
        tvEmpty = view.findViewById(R.id.tv_empty_list)
    }

    private fun configuratorViews() {
        searchView.onActionViewExpanded()
        searchView.visibility = View.GONE


        // Установка обработчика поиска
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (TextUtils.isEmpty(newText.trim())) {
                    selectNotesCategory(CATEGORY_ALL_NOTES)
                    //adapter.filter?.filter("")
                } else {
                    selectNotesCategory(CATEGORY_SEARCH)
                    viewModel.searchNameChanged(newText.trim())
                    //adapter.filter?.filter(newText.trim())
                }
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

        initWidgets()
        initObjects()
        installCallbacks()
        installHandlers()
    }

    private fun initWidgets() {
        widgetsHelper = activity.getWidgetsHelper
        fab = widgetsHelper.getFab
        bottomAppBar = widgetsHelper.getBottomAppBar

        widgetsHelper.setBottomBarVisible(true)
        widgetsHelper.setVisibleSelect(false)
        widgetsHelper.setVisibleUnification(false)
        widgetsHelper.setIconFab(true)
    }

    private fun initObjects() {
        snackbarUndo = SnackbarUndo(activity, widgetsHelper, viewModel)
    }

    private fun installCallbacks() {
        NoteListAdapter.setClickListener(this)
        SwipeCallbackNote.setSwipeListener(this)
        NoteFragment.setNoteListener(this)
    }

    private fun installHandlers() {
        fab.setOnClickListener {
            activity.openItem(Note2())
        }

        bottomAppBar.setOnSingleClickListener {
            snackbarUndo.close()
            val dialogBottomSheet = BottomSheetCategory.newInstance(this, notesCategory)
            dialogBottomSheet.show(activity.supportFragmentManager, BottomSheetCategory.TAG)
        }

        bottomAppBar.setOnMenuItemClickListener { item ->
            snackbarUndo.close()
            when (item.itemId) {
                R.id.action_switch_view -> {
                    widgetsHelper.setTypeNotes(recyclerView, !isTypeNotesOne)
                    MySharedPref.setBoolean(activity, MyPrefKey.KEY_TYPE_NOTES, !isTypeNotesOne)
                    true
                }
                R.id.action_select_item -> {
                    true
                }

                R.id.action_search -> {
                    searchView.visibility = View.VISIBLE
                    true
                }

                R.id.action_unification -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun setupObserver() {
        viewModel.searchList.observe(viewLifecycleOwner, { list ->
            list?.let {
                updateUI(list)
                activity.showToast(R.string.unknown_error)
            }
        })

        viewModel.searchNameChanged("")
        selectNotesCategory(notesCategory)
    }

    private fun selectNotesCategory(category: String) {
        notesCategory = category
        viewModel.categoryNotes(notesCategory)
    }

    private fun updateUI(list: List<Note2>) {
        tvEmpty.setVisibleView(list.isEmpty())
        adapter.setListProfiles(list)
    }

    override fun onResume() {
        super.onResume()
        resetRecyclerState()
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
        snackbarUndo.close()
        saveRecyclerStata()
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
        activity.openItem(note)
    }

    override fun onLongItemClick(note: Note2) {

    }

    override fun onItemSwipe(note: Note2, category: String) {
        snackbarUndo.show(note, category)
    }

    override fun onDeletedNote(note: Note2) {
        snackbarUndo.show(note, "")
    }


    // Instance
    companion object {
        @JvmStatic
        fun newInstance(): NoteListFragment {
            return NoteListFragment()
        }
    }


}