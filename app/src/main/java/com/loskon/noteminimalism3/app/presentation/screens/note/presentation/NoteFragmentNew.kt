package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setEndSelection
import com.loskon.noteminimalism3.app.base.extension.view.setFocus
import com.loskon.noteminimalism3.app.base.extension.view.setIconColor
import com.loskon.noteminimalism3.databinding.FragmentNoteNewBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.LocalDateTime

class NoteFragmentNew : Fragment(R.layout.fragment_note_new) {

    private val binding by viewBinding(FragmentNoteNewBinding::bind)
    private val viewModel: NoteViewModel by viewModel()
    private val args: NoteFragmentNewArgs by navArgs()

    private val savedNote: Note get() = viewModel.getNoteState.value

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.id != 0L) viewModel.getNote(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        installObserver()

        if (savedNote.id == 0L) {
            binding.editTextNote2.showKeyboard()
        } else {
            binding.linLayoutNote2.setFocus()
        }

        changeIconFavBtn(savedNote.isFavorite)
        isFavorite = savedNote.isFavorite

        setupViewsListeners()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.fabNote2.setBackgroundColorKtx(color)
        binding.btnFavNote2.setIconColor(color)
        binding.btnDelNote2.setIconColor(color)
        binding.btnMoreNote2.setIconColor(color)
    }

    private fun installObserver() {
        viewModel.getNoteState.observe(viewLifecycleOwner) { note ->
            Timber.d("HI")
            binding.editTextNote2.setText(note.title)
            binding.editTextNote2.setEndSelection()
        }
    }

    private fun setupViewsListeners() {
        binding.fabNote2.setDebounceClickListener { requireActivity().onBackPressed() }
        binding.btnFavNote2.setOnClickListener { handleFavoriteClick() }
        binding.btnDelNote2.setDebounceClickListener { handleDeleteClick() }
        binding.btnMoreNote2.setDebounceClickListener { }
    }

    private fun handleFavoriteClick() {
        val note = savedNote

        changeIconFavBtn(note.isFavorite.not())
        changeNoteFavStatus(note)
    }

    private fun handleDeleteClick() {
        val note = savedNote

        if (note.id == 0L) {
            viewModel.delete(note)
        } else {
            note.isFavorite = false
            note.isDeleted = true
            viewModel.update(note)
        }

        requireActivity().onBackPressed()
    }

    private fun changeIconFavBtn(isFavorite: Boolean) {
        val icon = getIconResource(isFavorite)
        binding.btnFavNote2.setIconResource(icon)
    }

    private fun getIconResource(isFavorite: Boolean): Int {
        return if (isFavorite) {
            R.drawable.baseline_star_black_24
        } else {
            R.drawable.baseline_star_border_black_24
        }
    }

    private fun changeNoteFavStatus(note: Note) {
        note.isFavorite = note.isFavorite.not()
        viewModel.setNote(note)
    }

    override fun onPause() {
        super.onPause()

        val title = binding.editTextNote2.text.toString()

        if (title.trim().isNotEmpty()) {
            if (savedNote.id == 0L) {
                val note = savedNote

                note.title = title
                note.modifiedDate = LocalDateTime.now()
                note.createdDate = LocalDateTime.now()

                note.id = viewModel.insertGetId(note)
                viewModel.setNote(note)
            } else {
                val note = savedNote

                if (note.title.trim() != title.trim()) {
                    note.modifiedDate = LocalDateTime.now()
                    viewModel.update(note)
                } else if (note.isFavorite != isFavorite) {
                    viewModel.update(note)
                }
            }
        } else {
            if (savedNote.id != 0L) viewModel.delete(savedNote)
        }
    }
}