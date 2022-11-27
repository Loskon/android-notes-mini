package com.loskon.noteminimalism3.app.screens.notetrash.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.notelist.presentation.NoteListFragment
import com.loskon.noteminimalism3.base.extension.flow.observe
import com.loskon.noteminimalism3.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.databinding.FragmentNoteTrashNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class NoteTrashFragmentNew : Fragment(R.layout.fragment_note_trash_new) {

    private val binding by viewBinding(FragmentNoteTrashNewBinding::bind)
    private val viewModel by viewModel<NoteTrashViewModel>()
    private val args by navArgs<NoteTrashFragmentNewArgs>()

    private val restoreSnackbar = NoteTrashRestoreSnackbarNew()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) viewModel.getNote(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        setNoteFontSize()
        installObserver()
        setupViewsListeners()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.fabNoteTrash.setBackgroundColorKtx(color)
    }

    private fun setNoteFontSize() {
        val fontSize = AppPreference.getNoteFontSize(requireContext())
        binding.editTextNote2.setTextSizeKtx(fontSize)
    }

    private fun installObserver() {
        viewModel.noteStateFlow.observe(viewLifecycleOwner) { note ->
            binding.editTextNote2.setText(note.title)
        }
    }

    private fun setupViewsListeners() {
        binding.fabNoteTrash.setDebounceClickListener {
            restoreNote()
            findNavController().popBackStack()
        }
        binding.btnDelNoteTrash.setDebounceClickListener {
            deleteNote()
            findNavController().popBackStack()
        }
        binding.linLayoutNote2.setOnClickListener {
            showRestoreSnackbar()
        }
        binding.editTextNote2.setOnClickListener {
            showRestoreSnackbar()
        }
        restoreSnackbar.setRestoreClickListener {
            restoreNote()
            findNavController().popBackStack()
        }
    }

    private fun restoreNote() {
        val note = viewModel.noteStateFlow.value
        val updateDateTime = AppPreference.hasUpdateDateTime(requireContext())

        if (updateDateTime) {
            note.createdDate = LocalDateTime.now()
            note.modifiedDate = LocalDateTime.now()
        }
        note.isDeleted = false
        viewModel.update(note)
    }

    private fun deleteNote() {
        val note = viewModel.noteStateFlow.value
        val undoSnackbarShow = AppPreference.isBottomWidgetShow(requireContext())

        if (undoSnackbarShow) {
            val bundle = bundleOf(NoteListFragment.NOTE_TRASH_BUNDLE_KEY to note)
            setFragmentResult(NoteListFragment.NOTE_TRASH_REQUEST_KEY, bundle)
        }

        viewModel.delete(note)
    }

    private fun showRestoreSnackbar() {
        val note = viewModel.noteStateFlow.value
        restoreSnackbar.make(binding.root, binding.fabNoteTrash, note).show()
    }
}