package com.loskon.noteminimalism3.app.screens.notetrash.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.flow.observe
import com.loskon.noteminimalism3.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.databinding.FragmentNoteTrashNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteTrashFragmentNew : Fragment(R.layout.fragment_note_trash_new) {

    private val binding by viewBinding(FragmentNoteTrashNewBinding::bind)
    private val viewModel: NoteTrashViewModel by viewModel()
    private val args: NoteTrashFragmentNewArgs by navArgs()

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
        viewModel.getNoteState.observe(viewLifecycleOwner) { note ->
            binding.editTextNote2.setText(note.title)
        }
    }

    private fun setupViewsListeners() {
        binding.fabNoteTrash.setDebounceClickListener { }
        binding.btnDelNoteTrash.setDebounceClickListener { }
        binding.linLayoutNote2.setOnClickListener { }
        binding.editTextNote2.setOnClickListener { }
    }
}