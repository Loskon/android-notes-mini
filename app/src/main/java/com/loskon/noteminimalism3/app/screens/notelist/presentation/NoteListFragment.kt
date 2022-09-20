package com.loskon.noteminimalism3.app.screens.notelist.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.bundle.getParcelableKtx
import com.loskon.noteminimalism3.base.extension.dialogfragment.show
import com.loskon.noteminimalism3.base.extension.flow.observe
import com.loskon.noteminimalism3.base.extension.fragment.getDrawable
import com.loskon.noteminimalism3.base.extension.fragment.setChildFragmentClickListener
import com.loskon.noteminimalism3.base.extension.fragment.setChildFragmentResultListener
import com.loskon.noteminimalism3.base.extension.fragment.setOnBackPressedListener
import com.loskon.noteminimalism3.base.extension.view.hide
import com.loskon.noteminimalism3.base.extension.view.scrollToTop
import com.loskon.noteminimalism3.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.base.extension.view.setChangeableLayoutManager
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.base.extension.view.setEnabledNestedView
import com.loskon.noteminimalism3.base.extension.view.setGoneVisibleKtx
import com.loskon.noteminimalism3.base.extension.view.setMenuIconWithColor
import com.loskon.noteminimalism3.base.extension.view.setMenuItemVisibility
import com.loskon.noteminimalism3.base.extension.view.setNavigationIconWithColor
import com.loskon.noteminimalism3.base.extension.view.setOnMenuItemClickListener
import com.loskon.noteminimalism3.base.extension.view.setShortQueryTextListener
import com.loskon.noteminimalism3.base.extension.view.show
import com.loskon.noteminimalism3.base.extension.view.showKeyboard
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.ConfirmSheetDialogFragment
import com.loskon.noteminimalism3.base.widget.snackbar.AppSnackbar
import com.loskon.noteminimalism3.base.widget.snackbar.BaseSnackbar
import com.loskon.noteminimalism3.databinding.FragmentNoteListBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.recyclerview.AppItemAnimator
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.LocalDateTime

@SuppressLint("NotifyDataSetChanged")
class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)
    private val viewModel: NoteListViewModel by viewModel()

    private val notesAdapter = NoteListAdapter()
    private val swipeCallback = NoteListSwipeCallback()

    private var undoSnackbar: NoteListUndoSnackbar? = null
    private var snackbar: BaseSnackbar? = null

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

        if (savedInstanceState == null) {
            val range = AppPreference.getRetentionRange(requireContext())
            viewModel.cleanTrash(range)
        }

        getNotes()

        setChildFragmentResultListener(NOTE_TRASH_REQUEST_KEY) { bundle ->
            val note = bundle.getParcelableKtx<Note>(NOTE_TRASH_BUNDLE_KEY)

            if (note != null) undoSnackbar?.show(note, note.isFavorite, category)
        }
        setChildFragmentClickListener(DELETE_FOREVER_REQUEST_KEY) {
            val checkedNotes = notesAdapter.getItems().filter { it.isChecked }

            viewModel.deleteNotes(checkedNotes)
            viewModel.toggleSelectionMode(false)
            viewModel.getNotes(scrollTop = false, quicklyListUpdate = true)
        }
        setChildFragmentClickListener(CLEAN_TRASH_REQUEST_KEY) {
            if (notesAdapter.itemCount != 0) {
                viewModel.cleanTrash()
                viewModel.getNotes(scrollTop = false, quicklyListUpdate = true)
            } else {
                showSnackbar(getString(R.string.sb_but_empty_trash), false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getNotes()
        establishViewsColor()
        initialObjects()
        configureRecyclerView()
        installObservers()
        setupViewsListeners()
    }

    private fun getNotes() {
        val sortWay = AppPreference.getSortingWay(requireContext())

        viewModel.setSortWay(sortWay)
        viewModel.getNotes(scrollTop = false, quicklyListUpdate = true)
    }

    private fun establishViewsColor() {
        color = AppPreference.getColor(requireContext())
        binding.fabNoteList.setBackgroundColorKtx(color)
        binding.bottomBarNoteList.setAllItemsColor(color)
        binding.incNoteList.cardViewMain.setBackgroundTintColorKtx(color)
    }

    private fun initialObjects() {
        undoSnackbar = NoteListUndoSnackbar(requireContext(), binding.root, binding.fabNoteList)
    }

    private fun configureRecyclerView() {
        val linearListType = AppPreference.getLinearListType(requireContext())

        with(binding.incNoteList.recyclerViewMain) {
            ItemTouchHelper(swipeCallback).attachToRecyclerView(this)
            configureListTypeViewsParameters(linearListType, false)
            itemAnimator = AppItemAnimator()
            adapter = notesAdapter
        }
    }

    private fun configureListTypeViewsParameters(linearListType: Boolean, save: Boolean) {
        val drawableId = getListTypeDrawableId(linearListType)

        binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_list_type, drawableId, color)
        binding.incNoteList.recyclerViewMain.setChangeableLayoutManager(linearListType)
        notesAdapter.setLinearList(linearListType)

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
        viewModel.getNoteListUiState.observe(viewLifecycleOwner) { uiState ->
            binding.tvEmptyNoteList.isVisible = uiState.notes.isEmpty()

            if (uiState.quicklyListUpdate || hasActiveSearchMode) {
                Timber.d("setQuicklyNoteList")
                notesAdapter.setQuicklyNoteList(uiState.notes)
            } else {
                Timber.d("setNoteList")
                notesAdapter.setNoteList(uiState.notes)
            }

            if (uiState.scrollTop) binding.incNoteList.recyclerViewMain.scrollToTop()
        }
        viewModel.getNoteListCategoryState.observe(viewLifecycleOwner) { category ->
            val drawableId = getFabDrawableId(category)
            binding.fabNoteList.setImageDrawable(getDrawable(drawableId))
        }
        viewModel.getNoteListSearchState.observe(viewLifecycleOwner) { searchMode ->
            if (searchMode) {
                with(binding) {
                    fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_search_off_black_24))
                    incNoteList.searchView.setGoneVisibleKtx(true)
                    incNoteList.searchView.showKeyboard()
                    bottomBarNoteList.hide(false)
                }
            } else {
                with(binding) {
                    fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_add_black_24))
                    incNoteList.searchView.setGoneVisibleKtx(false)
                    incNoteList.searchView.setQuery(null, false)
                    bottomBarNoteList.show(false)
                }
            }
        }
        viewModel.getNoteListSelectionState.observe(viewLifecycleOwner) { selectionMode ->
            if (selectionMode) {
                val drawableId = getFabTrashDrawableId()

                binding.fabNoteList.setImageDrawable(getDrawable(drawableId))
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
                val drawableId = getFabDrawableId(category)

                binding.fabNoteList.setImageDrawable(getDrawable(drawableId))
                binding.bottomBarNoteList.setNavigationIconWithColor(R.drawable.baseline_menu_black_24, color)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_search, true)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_list_type, true)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_select, false)
                binding.incNoteList.cardViewMain.setVisibilityKtx(false)
                swipeCallback.blockSwipe(false)

                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_unification, false)
                binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_favorite, false)

                if (hasActiveSearchMode) {
                    binding.fabNoteList.setImageDrawable(getDrawable(R.drawable.baseline_search_off_black_24))
                    binding.incNoteList.searchView.setEnabledNestedView(true)
                    binding.bottomBarNoteList.hide(false)
                }
            }
        }
    }

    private fun getFabDrawableId(category: String): Int {
        return when (category) {
            NoteListViewModel.CATEGORY_ALL_NOTES1 -> R.drawable.baseline_add_black_24
            NoteListViewModel.CATEGORY_FAVORITES1 -> R.drawable.baseline_star_black_24
            NoteListViewModel.CATEGORY_TRASH1 -> R.drawable.baseline_delete_black_24
            else -> R.drawable.baseline_add_black_24
        }
    }

    private fun getFabTrashDrawableId(): Int {
        return if (category == NoteListViewModel.CATEGORY_TRASH1) {
            R.drawable.baseline_delete_forever_black_24
        } else {
            R.drawable.baseline_delete_black_24
        }
    }

    private fun setupViewsListeners() {
        binding.incNoteList.searchView.setShortQueryTextListener { query -> viewModel.searchNotes(query) }
        notesAdapter.setOnItemClickListener { note, position -> handleNoteClick(note, position) }
        notesAdapter.setOnItemLongClickListener { note, position -> handleNoteLongClick(note, position) }
        swipeCallback.setOnItemSwipeListener { position -> handleNoteSwipe(position) }
        undoSnackbar?.setOnUndoClickListener { note, isFavorite -> handleNoteUndoClick(note, isFavorite) }
        binding.fabNoteList.setDebounceClickListener { handleFabClick() }
        binding.bottomBarNoteList.setDebounceNavigationClickListener { handleNavigationClick() }
        binding.bottomBarNoteList.setOnMenuItemClickListener(R.id.action_list_type) { handleListTypeClick() }
        binding.bottomBarNoteList.setDebounceMenuItemClickListener(R.id.action_search) { handleSearchClick() }
        binding.bottomBarNoteList.setOnMenuItemClickListener(R.id.action_select) { handleSelectClick() }
        binding.bottomBarNoteList.setDebounceMenuItemClickListener(R.id.action_unification) { showUnificationDialogFragment() }
        binding.bottomBarNoteList.setOnMenuItemClickListener(R.id.action_favorite) { handleFavoriteClick() }
    }

    private fun handleNoteClick(note: Note, position: Int) {
        if (hasActiveSelectionMode.not()) {
            val action = NoteListFragmentDirections.actionOpenNoteFragment(note.id)
            findNavController().navigate(action)
        } else {
            selectNote(note, position)
            updateFavoriteMenuIcon(note)
            changeViewsForSelectedNote()
        }
    }

    private fun selectNote(note: Note, position: Int) {
        note.isChecked = note.isChecked.not()
        notesAdapter.notifyItemChanged(position)
    }

    private fun updateFavoriteMenuIcon(note: Note) {
        if (category != NoteListViewModel.CATEGORY_TRASH1) {
            val selectedCount = (notesAdapter.getItems().count { it.isChecked })

            if (selectedCount == 1) {
                val drawableId = getFavoriteDrawableId(note.isFavorite)
                binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_favorite, drawableId, color)
            }
        }
    }

    private fun changeViewsForSelectedNote() {
        val selectedCount = (notesAdapter.getItems().count { it.isChecked })
        val drawableId = getSelectionDrawableId(selectedCount == notesAdapter.itemCount)

        with(binding) {
            incNoteList.tvCountItems.text = selectedCount.toString()
            bottomBarNoteList.setMenuIconWithColor(R.id.action_select, drawableId, color)

            if (category != NoteListViewModel.CATEGORY_TRASH1) {
                bottomBarNoteList.setMenuItemVisibility(R.id.action_unification, selectedCount >= 2)
                bottomBarNoteList.setMenuItemVisibility(R.id.action_favorite, selectedCount == 1)
            }
        }
    }

    private fun handleNoteLongClick(note: Note, position: Int) {
        if (hasActiveSelectionMode.not()) viewModel.toggleSelectionMode(true)

        selectNote(note, position)
        updateFavoriteMenuIcon(note)
        changeViewsForSelectedNote()
    }

    private fun handleNoteSwipe(position: Int) {
        val note = notesAdapter.getNote(position)
        val isFavorite = note.isFavorite

        if (category == NoteListViewModel.CATEGORY_TRASH1) {
            viewModel.deleteNote(note)
        } else {
            note.isDeleted = true
            note.isFavorite = false
            note.deletedDate = LocalDateTime.now()
            viewModel.updateNote(note)
        }

        viewModel.getNotes(scrollTop = false, quicklyListUpdate = false)
        undoSnackbar?.show(note, isFavorite, category)
    }

    private fun handleNoteUndoClick(note: Note, isFavorite: Boolean) {
        note.isDeleted = false
        note.isFavorite = isFavorite

        if (category == NoteListViewModel.CATEGORY_TRASH1) {
            viewModel.insertNote(note)
        } else {
            viewModel.updateNote(note)
        }

        viewModel.getNotes(scrollTop = false, quicklyListUpdate = false)
    }

    private fun handleFabClick() {
        if (hasActiveSelectionMode) {
            if (category == NoteListViewModel.CATEGORY_TRASH1) {
                showConfirmDeleteForever()
            } else {
                val checkedNotes = notesAdapter.getItems().filter { it.isChecked }
                val deletedNotes = checkedNotes.map { it.copy(isDeleted = true) }

                viewModel.updateNotes(deletedNotes)
                viewModel.toggleSelectionMode(false)
                viewModel.getNotes(scrollTop = false, quicklyListUpdate = true)
            }
        } else if (hasActiveSearchMode) {
            viewModel.toggleSearchMode(false)
        } else {
            if (category == NoteListViewModel.CATEGORY_TRASH1) {
                showConfirmCleanTrash()
            } else {
                val action = NoteListFragmentDirections.actionOpenNoteFragment(0)
                findNavController().navigate(action)
            }
        }
    }

    private fun handleNavigationClick() {
        dismissSnackbars()
        if (hasActiveSelectionMode.not()) {
            showCategorySheetDialogFragment()
        } else {
            viewModel.toggleSelectionMode(false)
            resetSelectedNotes()
        }
    }

    private fun handleListTypeClick() {
        dismissSnackbars()
        val linearListType = AppPreference.getLinearListType(requireContext()).not()
        configureListTypeViewsParameters(linearListType, true)
    }

    private fun handleSearchClick() {
        dismissSnackbars()
        viewModel.toggleSearchMode(true)
    }

    private fun handleSelectClick() {
        val selectedCount = (notesAdapter.getItems().count { it.isChecked })

        for (note in notesAdapter.getItems()) note.isChecked = (selectedCount == notesAdapter.itemCount).not()
        notesAdapter.notifyDataSetChanged()

        changeViewsForSelectedNote()
    }

    private fun handleFavoriteClick() {
        val note = notesAdapter.getItems().firstOrNull { it.isChecked }

        if (note != null) {
            note.isFavorite = note.isFavorite.not()
            notesAdapter.notifyDataSetChanged()

            val drawableId = getFavoriteDrawableId(note.isFavorite)
            binding.bottomBarNoteList.setMenuIconWithColor(R.id.action_favorite, drawableId, color)

            viewModel.updateNote(note)
        }
    }

    private fun getFavoriteDrawableId(isFavorite: Boolean): Int {
        return if (isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }
    }

    private fun getSelectionDrawableId(allSelected: Boolean): Int {
        return if (allSelected) {
            R.drawable.baseline_done_black_24
        } else {
            R.drawable.baseline_done_all_black_24
        }
    }

    private fun showCategorySheetDialogFragment() {
        NoteListCategorySheetDialogFragment.newInstance(category).apply {
            setOnCategorySelectListener { category ->
                dismissSnackbars()
                viewModel.setCategory(category)
                viewModel.getNotes(scrollTop = true, quicklyListUpdate = true)
            }
            setOnSettingsClickListener {
                val action = NoteListFragmentDirections.actionOpenSettingsFragment()
                findNavController().navigate(action)
            }
        }.show(childFragmentManager, NoteListCategorySheetDialogFragment.TAG)
    }

    private fun showUnificationDialogFragment() {
        NoteListUnificationSheetDialogFragment.newInstance().apply {
            setOnDeleteClickListener {
                unification(deleteCombinedNotes = true)
                viewModel.toggleSelectionMode(false)
                viewModel.getNotes(scrollTop = true, quicklyListUpdate = true)
                showSnackbar(getString(R.string.sb_combined_note_added), true)
            }
            setOnLeaveClickListener {
                unification(deleteCombinedNotes = false)
                viewModel.toggleSelectionMode(false)
                viewModel.getNotes(scrollTop = true, quicklyListUpdate = true)
                showSnackbar(getString(R.string.sb_combined_note_added), true)
            }
        }.show(childFragmentManager, NoteListUnificationSheetDialogFragment.TAG)
    }

    private fun unification(deleteCombinedNotes: Boolean) {
        val stringBuilder = StringBuilder()
        val selectedNotes = notesAdapter.getItems().filter { it.isChecked }

        var title = String()
        var isFavorite = false

        for (note in selectedNotes) {
            title = uniteTitlesNote(note, stringBuilder, selectedNotes)
            if (note.isFavorite) isFavorite = true
        }

        if (deleteCombinedNotes) viewModel.deleteNotes(selectedNotes)
        viewModel.insertNote(Note(title = title, isFavorite = isFavorite))
    }

    private fun uniteTitlesNote(note: Note, stringBuilder: StringBuilder, selectedNotes: List<Note>): String {
        val title = note.title.trim()

        if (note != selectedNotes[selectedNotes.lastIndex]) {
            stringBuilder.append(title).append("\n\n")
        } else {
            stringBuilder.append(title)
        }

        return stringBuilder.toString()
    }

    private fun showConfirmDeleteForever() {
        ConfirmSheetDialogFragment.newInstance(
            requestKey = DELETE_FOREVER_REQUEST_KEY,
            title = getString(R.string.dg_delete_forever_title),
            btnOkText = getString(R.string.yes),
            btnCancelText = getString(R.string.no)
        ).show(childFragmentManager)
    }

    private fun showConfirmCleanTrash() {
        ConfirmSheetDialogFragment.newInstance(
            requestKey = CLEAN_TRASH_REQUEST_KEY,
            title = getString(R.string.dg_trash_title),
            btnOkText = getString(R.string.yes),
            btnCancelText = getString(R.string.no)
        ).show(childFragmentManager)
    }

    fun showSnackbar(message: String, success: Boolean) {
        snackbar = AppSnackbar().make(binding.root, message, success, binding.fabNoteList)
        snackbar?.show()
    }

    private fun dismissSnackbars() {
        undoSnackbar?.dismiss()
        snackbar?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        dismissSnackbars()
    }

    companion object {
        const val NOTE_TRASH_REQUEST_KEY = "NOTE_TRASH_REQUEST_KEY"
        const val NOTE_TRASH_BUNDLE_KEY = "NOTE_TRASH_BUNDLE_KEY"
        const val CLEAN_TRASH_REQUEST_KEY = "CLEAN_TRASH_REQUEST_KEY"
        const val DELETE_FOREVER_REQUEST_KEY = "DELETE_FOREVER_REQUEST_KEY"
    }
}