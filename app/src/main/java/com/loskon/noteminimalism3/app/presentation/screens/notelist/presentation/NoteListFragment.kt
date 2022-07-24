package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.view.setAllItemsColor
import com.loskon.noteminimalism3.app.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setLayoutManagerKtx
import com.loskon.noteminimalism3.app.base.widget.recyclerview.AddAnimationItemAnimator
import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.NoteListViewModel.Companion.CATEGORY_ALL_NOTES1
import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.adapter.NoteListAdapter
import com.loskon.noteminimalism3.databinding.FragmentNoteListBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)
    private val viewModel: NoteListViewModel by viewModel()

    private val notesAdapter: NoteListAdapter = NoteListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val sort = AppPreference.getSortingWay(context)
        viewModel.getNotes(CATEGORY_ALL_NOTES1, sort)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        configureRecyclerView()
        setupViewsListeners()
        installObserver()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.fabNoteList.setColorKtx(color)
        binding.bottomBarNoteList.setAllItemsColor(color)
    }

    private fun configureRecyclerView() {
        with(binding.incNoteList.recyclerViewMain) {
            itemAnimator = AddAnimationItemAnimator()
            setLayoutManagerKtx(true)
            adapter = notesAdapter
        }
    }

    private fun setupViewsListeners() {
        binding.bottomBarNoteList.setDebounceNavigationClickListener {
            val action = NoteListFragmentDirections.actionOpenSettingsFragment()
            findNavController().navigate(action)
        }
    }

    private fun installObserver() {
        viewModel.getNoteListState.observe(viewLifecycleOwner) { notes ->
            binding.tvEmptyNoteList.isVisible = notes.isEmpty()
            notesAdapter.updateNoteList(notes)
        }
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