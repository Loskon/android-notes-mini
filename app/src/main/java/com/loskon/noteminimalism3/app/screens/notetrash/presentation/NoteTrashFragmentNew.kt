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
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime

class NoteTrashFragmentNew : Fragment(R.layout.fragment_note_trash_new) {

    private val binding by viewBinding(FragmentNoteTrashNewBinding::bind)
    private val viewModel: NoteTrashViewModel by viewModel()
    private val args: NoteTrashFragmentNewArgs by navArgs()

    private var restoreSnackbar: NoteTrashRestoreSnackbar? = null

    private val getNote: Note get() = viewModel.getNoteState.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) viewModel.getNote(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restoreSnackbar = NoteTrashRestoreSnackbar(requireContext(), binding.root, binding.fabNoteTrash)

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
        viewModel.getNoteState.observe(viewLifecycleOwner) { note ->
            binding.editTextNote2.setText(note.title)
        }
    }

    private fun setupViewsListeners() {
        binding.fabNoteTrash.setDebounceClickListener {
            val note = getNote
            val updateDateTime = AppPreference.hasUpdateDateTime(requireContext())

            note.isDeleted = false
            if (updateDateTime) note.createdDate = LocalDateTime.now()
            viewModel.update(note)

            findNavController().popBackStack()
        }
        binding.btnDelNoteTrash.setDebounceClickListener {
            val note = getNote

            checkShowUndoSnackbar(note)
            viewModel.delete(note)
        }
        binding.linLayoutNote2.setOnClickListener {
            restoreSnackbar?.make(getNote)?.show()
        }
        binding.editTextNote2.setOnClickListener {
            restoreSnackbar?.make(getNote)?.show()
        }
        restoreSnackbar?.setRestoreClickListener {
            val note = getNote
            val updateDateTime = AppPreference.hasUpdateDateTime(requireContext())

            note.isDeleted = false
            if (updateDateTime) note.createdDate = LocalDateTime.now()
            viewModel.update(note)

            findNavController().popBackStack()
        }
    }

    private fun checkShowUndoSnackbar(note: Note) {
        val undoSnackbarShow = AppPreference.isBottomWidgetShow(requireContext())

        if (undoSnackbarShow) {
            val bundle = bundleOf(NoteListFragment.NOTE_TRASH_BUNDLE_KEY to note)
            setFragmentResult(NoteListFragment.NOTE_TRASH_REQUEST_KEY, bundle)
        }
    }
}