package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.getDrawable
import com.loskon.noteminimalism3.app.base.extension.fragment.requireDrawable
import com.loskon.noteminimalism3.app.base.extension.fragment.setOnBackPressedListener
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
import com.loskon.noteminimalism3.app.base.extension.view.setMenuIconWithColor
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

@SuppressLint("NotifyDataSetChanged")
class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)
    private val viewModel: NoteListViewModel by viewModel()

    private val notesAdapter = NoteListAdapter()
    private val swipeCallback = NoteListSwipeCallback()

    private var color: Int = 0

    private val category: String get() = viewModel.getNoteListCategoryState.value
    private val hasActiveSelectionMode: Boolean get() = viewModel.getNoteListSelectionState.value
    private val hasActiveSearchMode: Boolean get() = viewModel.getNoteListSearchState.value

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setOnBackPressedListener {
            if (hasActiveSelectionMode) {
                viewModel.toggleSelectionMode(false)
                resetSelectedNotes()
            } else if (hasActiveSearchMode) {
                viewModel.toggleSearchMode(false)
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun resetSelectedNotes() {
        val selectedItemCount = notesAdapter.getItems().count { it.isChecked }

        if (selectedItemCount != 0) {
            for (note in notesAdapter.getItems()) note.isChecked = false
            notesAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sortWay = AppPreference.getSortingWay(requireContext())
        viewModel.setSortWay(sortWay)

        if (savedInstanceState == null) {
            viewModel.cleanTrash()
            viewModel.getNotes()
        }
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
        binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_list_type, drawableId, color)
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
            val drawableId = when (category) {
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

                if (category == NoteListViewModel.CATEGORY_TRASH1) {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_delete_forever_black_24))
                } else {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_delete_black_24))
                }

                binding.bottomBarNoteList.setNavigationIconWithColor(R.drawable.baseline_close_black_24, color)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_search, false)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_list_type, false)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_select, true)
                binding.incNoteList.cardViewMain.setVisibilityKtx(true)
                swipeCallback.blockSwipe(true)

                if (hasActiveSearchMode) {
                    binding.incNoteList.searchView.clearFocus()
                    binding.incNoteList.searchView.setEnabledNestedView(false)
                    binding.bottomBarNoteList.show(false)
                }

            } else {

                if (category == NoteListViewModel.CATEGORY_ALL_NOTES1) {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_add_black_24))
                } else if (category == NoteListViewModel.CATEGORY_FAVORITES1) {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_star_black_24))
                } else {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_delete_black_24))
                }

                binding.bottomBarNoteList.setNavigationIconWithColor(R.drawable.baseline_menu_black_24, color)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_search, true)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_list_type, true)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_select, false)
                binding.incNoteList.cardViewMain.setVisibilityKtx(false)
                swipeCallback.blockSwipe(false)

                if (hasActiveSearchMode) {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_search_off_black_24))
                    binding.incNoteList.searchView.setEnabledNestedView(true)
                    binding.bottomBarNoteList.hide(false)
                }

                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_unification, false)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_favorite, false)
            }
        }
    }

    private fun setupViewsListeners() {
        binding.incNoteList.searchView.setShortQueryTextListener { query ->
            viewModel.searchNotes(query)
        }
        notesAdapter.setOnItemClickListener { note, position ->
            if (hasActiveSelectionMode.not()) {
                // TODO
            } else {
                note.isChecked = note.isChecked.not()
                notesAdapter.notifyItemChanged(position)

                val selectedCount = (notesAdapter.getItems().count { it.isChecked })
                binding.incNoteList.tvCountItems.text = selectedCount.toString()
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_unification, selectedCount >= 2)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_favorite, selectedCount == 1)
                if (selectedCount == 1) binding.bottomBarNoteList.setMenuIconWithColor(
                    R.id.action_favorite,
                    requireDrawable(getFavoriteDrawableId(note.isFavorite)),
                    color
                )
                binding.bottomBarNoteList.setMenuIconWithColor(
                    R.id.action_select,
                    requireDrawable(getSelectionDrawableId(selectedCount == notesAdapter.itemCount)),
                    color
                )
            }
        }
        notesAdapter.setOnItemLongClickListener { note, position ->
            if (hasActiveSelectionMode.not()) viewModel.toggleSelectionMode(true)

            note.isChecked = note.isChecked.not()
            notesAdapter.notifyItemChanged(position)

            val selectedCount = (notesAdapter.getItems().count { it.isChecked })
            binding.incNoteList.tvCountItems.text = selectedCount.toString()
            binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_unification, selectedCount >= 2)
            binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_favorite, selectedCount == 1)
            if (selectedCount == 1) binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_favorite, getFavoriteDrawableId(note.isFavorite), color)
            binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_select, getSelectionDrawableId(selectedCount == notesAdapter.itemCount), color)
        }
        swipeCallback.setOnItemSwipeListener { viewHolder ->
            val note = notesAdapter.getNote(viewHolder.absoluteAdapterPosition).also { it.isDeleted = true }

            viewModel.updateNote(note)
            viewModel.getNotes()
        }
        binding.fabNoteList.setDebounceClickListener {
            if (hasActiveSelectionMode) {
                if (category == NoteListViewModel.CATEGORY_TRASH1) {
                    val checkedNotes = notesAdapter.getItems().filter { it.isChecked }

                    viewModel.deleteNotes(checkedNotes)
                    viewModel.toggleSelectionMode(false)
                    viewModel.getNotes()
                } else {
                    val checkedNotes = notesAdapter.getItems().filter { it.isChecked }
                    val deletedNotes = checkedNotes.map { it.copy(isDeleted = true) }

                    viewModel.updateNotes(deletedNotes)
                    viewModel.toggleSelectionMode(false)
                    viewModel.getNotes()
                }
            } else if (hasActiveSearchMode) {
                viewModel.toggleSearchMode(false)
            } else {
                // TODO
            }
        }
        with(binding.bottomBarNoteList) {
            setDebounceNavigationClickListener {
                if (hasActiveSelectionMode.not()) {
                    showCategorySheetDialogFragment()
                } else {
                    viewModel.toggleSelectionMode(false)
                    resetSelectedNotes()
                }
            }
            setShortMenuItemClickListener(R.id.action_list_type) {
                val linearListType = AppPreference.getLinearListType(requireContext()).not()
                configureListTypeViewsParameters(linearListType, true)
            }
            setDebounceMenuItemClickListener(R.id.action_search) {
                viewModel.toggleSearchMode(true)
            }
            setShortMenuItemClickListener(R.id.action_select) {

            }
            setDebounceMenuItemClickListener(R.id.action_unification) {

            }
            setShortMenuItemClickListener(R.id.action_favorite) {
                val note = notesAdapter.getItems().first { it.isChecked }.also { it.isFavorite = it.isFavorite.not() }
                val drawableId = getFavoriteDrawableId(note.isFavorite)
                binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_favorite, drawableId, color)
                viewModel.updateNote(note)
                notesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getFavoriteDrawableId(b: Boolean): Int {
        return if (b) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }
    }

    private fun getSelectionDrawableId(b: Boolean): Int {
        return if (b) {
            R.drawable.baseline_done_black_24
        } else {
            R.drawable.baseline_done_all_black_24
        }
    }

    private fun showCategorySheetDialogFragment() {
        CategorySheetDialogFragment.newInstance(category).apply {
            setOnCategorySelectListener { category ->
                viewModel.setCategory(category)
                viewModel.getNotes()
            }
            setOnSettingsClickListener {
                val action = NoteListFragmentDirections.actionOpenSettingsFragment()
                findNavController().navigate(action)
            }
        }.show(childFragmentManager, CategorySheetDialogFragment.TAG)
    }
}