package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.getDrawable
import com.loskon.noteminimalism3.app.base.extension.fragment.requireDrawable
import com.loskon.noteminimalism3.app.base.extension.fragment.setOnBackClickListener
import com.loskon.noteminimalism3.app.base.extension.view.hide
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setChangeableLayoutManager
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setEnabledNestedView
import com.loskon.noteminimalism3.app.base.extension.view.setGoneVisibleKtx
import com.loskon.noteminimalism3.app.base.extension.view.setMenuIcon
import com.loskon.noteminimalism3.app.base.extension.view.setMenuIconColor
import com.loskon.noteminimalism3.app.base.extension.view.setMenuItemVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setNavigationIconWithColor
import com.loskon.noteminimalism3.app.base.extension.view.setShortMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortQueryTextListener
import com.loskon.noteminimalism3.app.base.extension.view.show
import com.loskon.noteminimalism3.app.base.extension.view.showKeyboard
import com.loskon.noteminimalism3.app.base.widget.recyclerview.AddAnimationItemAnimator
import com.loskon.noteminimalism3.app.presentation.screens.CategorySheetDialogFragment
import com.loskon.noteminimalism3.databinding.FragmentNoteListBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)
    private val viewModel: NoteListViewModel by viewModel()

    private val notesAdapter = NoteListAdapter()
    private val swipeCallback = NoteListSwipeCallback()

    private var color: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            performRequestNotes()
        }
        setOnBackClickListener {
            if (viewModel.getNoteListSelectionState.value) {
                viewModel.toggleSelectionMode(false)
                /*                for (note in notesAdapter.getItems()) {
                                    if (note.isChecked) note.isChecked = false
                                }
                                notesAdapter.notifyDataSetChanged()*/
            } else if (viewModel.getNoteListSearchState.value) {
                viewModel.toggleSearchMode(false)
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun performRequestNotes() {
        val sort = AppPreference.getSortingWay(requireContext())
        val category = viewModel.getNoteListCategoryState.value
        viewModel.getNotes(category, sort)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        configureRecyclerView()
        installObservers()
        setupViewsListeners()
    }

    private fun establishViewsColor() {
        color = AppPreference.getColor(requireContext())
        binding.fabNoteList.setBackgroundColorKtx(color)
        binding.bottomBarNoteList.setAllItemsColor(color)
        binding.incNoteList.cardViewMain.setBackgroundTintColorKtx(color)
    }

    private fun configureRecyclerView() {
        val linearListType = AppPreference.getLinearListType(requireContext())

        with(binding.incNoteList.recyclerViewMain) {
            ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
            configureListTypeViewsParameters(linearListType, false)
            itemAnimator = AddAnimationItemAnimator()
            adapter = notesAdapter
        }
    }

    private fun configureListTypeViewsParameters(linearListType: Boolean, save: Boolean) {
        val drawableId = getListTypeDrawableId(linearListType)
        binding.bottomBarNoteList.setMenuIcon(R.id.action_list_type, requireDrawable(drawableId))
        binding.bottomBarNoteList.setMenuIconColor(R.id.action_list_type, color)
        binding.incNoteList.recyclerViewMain.setChangeableLayoutManager(linearListType)
        if (save) AppPreference.setLinearListType(requireContext(), linearListType)
    }

    private fun getListTypeDrawableId(linearListType: Boolean): Int {
        return if (linearListType) {
            R.drawable.outline_dashboard_black_24
        } else {
            R.drawable.outline_view_agenda_black_24
        }
    }

    private fun installObservers() {
        viewModel.getNoteListState.observe(viewLifecycleOwner) { notes ->
            binding.tvEmptyNoteList.isVisible = notes.isEmpty()
            notesAdapter.updateNoteList(notes)
        }
        viewModel.getNoteListCategoryState.observe(viewLifecycleOwner) { category ->
            val drawableId: Int = when (category) {
                NoteListViewModel.CATEGORY_ALL_NOTES1 -> R.drawable.baseline_add_black_24
                NoteListViewModel.CATEGORY_FAVORITES1 -> R.drawable.baseline_star_black_24
                NoteListViewModel.CATEGORY_TRASH1 -> R.drawable.baseline_delete_black_24
                else -> R.drawable.baseline_add_black_24
            }

            binding.fabNoteList.setImageDrawable(getDrawable(drawableId))
        }
        viewModel.getNoteListSearchState.observe(viewLifecycleOwner) { searchMode ->
            if (searchMode) {
                binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_search_off_black_24))
                binding.incNoteList.searchView.setGoneVisibleKtx(true)
                binding.incNoteList.searchView.showKeyboard()
                binding.bottomBarNoteList.hide(false)
            } else {
                binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_add_black_24))
                binding.incNoteList.searchView.setQuery(null, false)
                binding.incNoteList.searchView.setGoneVisibleKtx(false)
                binding.bottomBarNoteList.show(false)
            }
        }
        viewModel.getNoteListSelectionState.observe(viewLifecycleOwner) { selectionMode ->
            if (selectionMode) {
                binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_delete_black_24))
                binding.bottomBarNoteList.setNavigationIconWithColor(R.drawable.baseline_close_black_24, color)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_search, false)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_list_type, false)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_select_item, true)
                binding.incNoteList.cardViewMain.setVisibilityKtx(true)
                swipeCallback.blockSwipe(true)

                if (viewModel.getNoteListSearchState.value) {
                    binding.incNoteList.searchView.clearFocus()
                    binding.incNoteList.searchView.setEnabledNestedView(false)
                    binding.bottomBarNoteList.show(false)
                }
            } else {
                binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_add_black_24))
                binding.bottomBarNoteList.setNavigationIconWithColor(R.drawable.baseline_menu_black_24, color)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_search, true)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_list_type, true)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_select_item, false)
                binding.incNoteList.cardViewMain.setVisibilityKtx(false)
                swipeCallback.blockSwipe(false)

                if (viewModel.getNoteListSearchState.value) {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_search_off_black_24))
                    binding.incNoteList.searchView.setEnabledNestedView(true)
                    binding.bottomBarNoteList.hide(false)
                }
            }
        }
    }

    private fun setupViewsListeners() {
        binding.incNoteList.searchView.setShortQueryTextListener { queryText ->
            viewModel.searchNotes(queryText)
        }
        notesAdapter.setOnItemClickListener { note, position ->
            if (viewModel.getNoteListSelectionState.value.not()) {

            } else {

                note.isChecked = note.isChecked.not()
                notesAdapter.notifyItemChanged(position)
                binding.incNoteList.tvCountItems.text = (notesAdapter.getItems().count { it.isChecked }).toString()
            }
        }
        notesAdapter.setOnItemLongClickListener { note, position ->
            if (viewModel.getNoteListSelectionState.value.not()) viewModel.toggleSelectionMode(true)

            note.isChecked = note.isChecked.not()
            notesAdapter.notifyItemChanged(position)
            binding.incNoteList.tvCountItems.text = (notesAdapter.getItems().count { it.isChecked }).toString()
        }
        swipeCallback.setOnItemSwipeListener { viewHolder ->

        }
        binding.fabNoteList.setDebounceClickListener {
            if (viewModel.getNoteListSelectionState.value) {
                for (note in notesAdapter.getItems()) {
                    if (note.isChecked) {
                        viewModel.deleteItem(note)
                    }
                }
                viewModel.toggleSelectionMode(false)
                performRequestNotes()
            } else if (viewModel.getNoteListSearchState.value) {
                viewModel.toggleSearchMode(false)
            } else {
                // TODO
            }
        }
        with(binding.bottomBarNoteList) {
            setDebounceNavigationClickListener {
                if (viewModel.getNoteListSelectionState.value.not()) {
                    showCategorySheetDialogFragment()
                } else {
                    viewModel.toggleSelectionMode(false)
                }
            }
            setShortMenuItemClickListener(R.id.action_list_type) {
                val linearListType = AppPreference.getLinearListType(requireContext()).not()
                configureListTypeViewsParameters(linearListType, true)
            }
            setDebounceMenuItemClickListener(R.id.action_search) {
                viewModel.toggleSearchMode(true)
            }
            setShortMenuItemClickListener(R.id.action_select_item) {

            }
            setDebounceMenuItemClickListener(R.id.action_unification) {

            }
            setShortMenuItemClickListener(R.id.action_favorite) {

            }
        }
    }

    private fun showCategorySheetDialogFragment() {
        val currentCategory = viewModel.getNoteListCategoryState.value

        CategorySheetDialogFragment.newInstance(currentCategory).apply {
            setOnCategoryClickListener { category ->
                viewModel.setCategory(category)
                performRequestNotes()
                // binding.incNoteList.recyclerViewMain.scrollToTop()
            }
        }.show(childFragmentManager, CategorySheetDialogFragment.TAG)
    }
}