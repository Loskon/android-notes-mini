package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.scrollToTop
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setLayoutManagerKtx
import com.loskon.noteminimalism3.app.base.widget.recyclerview.AddAnimationItemAnimator
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.databinding.FragmentNoteListBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter
import com.loskon.noteminimalism3.ui.recyclerview.notes.NoteRecyclerAdapter
import com.loskon.noteminimalism3.utils.setVisibilityKtx
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteListFragment : Fragment(R.layout.fragment_note_list) {

    private val binding by viewBinding(FragmentNoteListBinding::bind)

    private val commandCenter: CommandCenter = CommandCenter()
    private val notesAdapter: NoteRecyclerAdapter = NoteRecyclerAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        setupViewsListener()
        updateQuicklyNoteList()
    }

    private fun configureRecyclerView() {
        with(binding.incNoteList.recyclerViewMain) {
            itemAnimator = AddAnimationItemAnimator()
            setLayoutManagerKtx(true)
            adapter = notesAdapter
        }
    }

    private fun setupViewsListener() {
        binding.bottomBarNoteList.setDebounceNavigationClickListener {
            val action = NoteListFragmentDirections.actionOpenSettingsFragment()
            findNavController().navigate(action)
        }
    }

    private fun updateQuicklyNoteList(scrollToTop: Boolean = true) {
        val notes = getNoteList()
        notesAdapter.setQuicklyNoteList(notes)
        binding.tvEmptyNoteList.setVisibilityKtx(notes.isEmpty())
        if (notes.isNotEmpty() && scrollToTop) binding.incNoteList.recyclerViewMain.scrollToTop()
    }

    private fun getNoteList(): List<Note> {
        return commandCenter.getNotes(null, DataBaseAdapter.CATEGORY_ALL_NOTES, 0)
    }
}