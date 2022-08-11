package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.requireDrawable
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setChangeableLayoutManager
import com.loskon.noteminimalism3.app.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceMenuItemClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setMenuItemVisibility
import com.loskon.noteminimalism3.app.base.extension.view.setShortMenuItemClickListener
import com.loskon.noteminimalism3.app.base.widget.recyclerview.AddAnimationItemAnimator
import com.loskon.noteminimalism3.databinding.FragmentNoteListBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)
    private val viewModel: NoteListViewModel by viewModel()

    private val notesAdapter = NoteListAdapter()
    private val swipeCallback = NoteListSwipeCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val sort = AppPreference.getSortingWay(requireContext())
            viewModel.getNotes(NoteListViewModel.CATEGORY_ALL_NOTES1, sort)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        configureRecyclerView()
        installObserver()
        setupViewsListeners()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.fabNoteList.setColorKtx(color)
        binding.bottomBarNoteList.setAllItemsColor(color)
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
        binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_linear_list_type, linearListType)
        binding.bottomBarNoteList.setMenuItemVisibility(R.id.action_grid_list_type, linearListType.not())
        binding.incNoteList.recyclerViewMain.setChangeableLayoutManager(linearListType)
        if (save) AppPreference.setLinearListType(requireContext(), linearListType)
    }

    private fun installObserver() {
        viewModel.getNoteListState.observe(viewLifecycleOwner) { notes ->
            binding.tvEmptyNoteList.isVisible = notes.isEmpty()
            notesAdapter.updateNoteList(notes)
        }
    }

    private fun setupViewsListeners() {
        notesAdapter.setOnItemClickListener { note ->

        }
        notesAdapter.setOnItemLongClickListener { note ->

        }
        swipeCallback.setOnItemSwipeListener { viewHolder ->

        }
        with(binding.bottomBarNoteList) {
            setDebounceNavigationClickListener {
                val action = NoteListFragmentDirections.actionOpenSettingsFragment()
                findNavController().navigate(action)
            }
            setShortMenuItemClickListener(R.id.action_linear_list_type) {
                val linearListType = AppPreference.getLinearListType(requireContext())
                configureListTypeViewsParameters(linearListType.not(), true)
            }
            setShortMenuItemClickListener(R.id.action_grid_list_type) {
                val linearListType = AppPreference.getLinearListType(requireContext())
                configureListTypeViewsParameters(linearListType.not(), true)
            }
            setDebounceMenuItemClickListener(R.id.action_search) {

            }
            setShortMenuItemClickListener(R.id.action_select_item) {

            }
            setDebounceMenuItemClickListener(R.id.action_unification) {

            }
            setShortMenuItemClickListener(R.id.action_favorite) {

            }
        }
    }

    private fun changeIconToggleViewMenuItem(hasLinearList: Boolean) {
        val drawableId = if (hasLinearList) {
            R.drawable.outline_dashboard_black_24
        } else {
            R.drawable.outline_view_agenda_black_24
        }

        replaceMenuIcon(R.id.action_linear_list_type, drawableId)
    }

    private fun replaceMenuIcon(menuItem: Int, drawableId: Int) {
        val color = AppPreference.getColor(requireContext())
        binding.bottomBarNoteList.menu.findItem(menuItem).icon = requireDrawable(drawableId)
        binding.bottomBarNoteList.setAllItemsColor(color)
    }

    /*    private fun updateQuicklyNoteList(scrollToTop: Boolean = true) {
            val notes = getNoteList()
            notesAdapter.updateNoteList(notes)
            binding.tvEmptyNoteList.setVisibilityKtx(notes.isEmpty())
            if (notes.isNotEmpty() && scrollToTop) binding.incNoteList.recyclerViewMain.scrollToTop()
        }

        private fun getNoteList(): List<Note> {
            return commandCenter.getNotes(null, DatabaseAdapter.CATEGORY_ALL_NOTES, 0)
        }*/
}