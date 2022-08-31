package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.clipboardmanager.ClipboardHelper
import com.loskon.noteminimalism3.app.base.contracts.StorageContract
import com.loskon.noteminimalism3.app.base.datetime.formatedString
import com.loskon.noteminimalism3.app.base.extension.corutine.launchDelay
import com.loskon.noteminimalism3.app.base.extension.flow.observe
import com.loskon.noteminimalism3.app.base.extension.fragment.getColor
import com.loskon.noteminimalism3.app.base.extension.view.scrollBottom
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setEndSelection
import com.loskon.noteminimalism3.app.base.extension.view.setFocus
import com.loskon.noteminimalism3.app.base.extension.view.setIconColor
import com.loskon.noteminimalism3.app.base.extension.view.setOnDownClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.app.base.linkmovementmethod.AppLinkMovementMethod
import com.loskon.noteminimalism3.app.base.widget.snackbar.AppSnackbar
import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.NoteListFragment
import com.loskon.noteminimalism3.databinding.FragmentNoteNewBinding
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.managers.LinksManager
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.hideKeyboard
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.time.LocalDateTime

class NoteFragmentNew : Fragment(R.layout.fragment_note_new) {

    private val binding by viewBinding(FragmentNoteNewBinding::bind)
    private val viewModel: NoteViewModel by viewModel()
    private val args: NoteFragmentNewArgs by navArgs()

    private val movementMethod = AppLinkMovementMethod()

    private var isFavorite: Boolean = false

    private val savedNote: Note get() = viewModel.getNoteState.value

    private val storageContract = StorageContract(this) { granted ->
        if (granted) {

        } else {
            showSnackbar(getString(R.string.no_permissions), false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (args.id != 0L) viewModel.getNote(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        establishViewsColor()
        configureEditText()
        configureKeyboardFocus()
        configureNoteFontSize()
        configureFavoriteStatus()
        configureWorkWithLinks()
        installObserver()
        setupViewsListeners()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.editTextNote2.setLinkTextColor(color)
        binding.fabNote2.setBackgroundColorKtx(color)
        binding.btnFavNote2.setIconColor(color)
        binding.btnDelNote2.setIconColor(color)
        binding.btnMoreNote2.setIconColor(color)
    }

    private fun configureEditText() {
        if (savedNote.id != NEW_NOTE_ID) binding.editTextNote2.isFocusableInTouchMode = false
    }

    private fun configureKeyboardFocus() {
        if (savedNote.id == NEW_NOTE_ID) {
            binding.editTextNote2.showKeyboard()
        } else {
            binding.linLayoutNote2.setFocus()
        }
    }

    private fun configureNoteFontSize() {
        val fontSize = AppPreference.getNoteFontSize(requireContext())
        binding.editTextNote2.setTextSizeKtx(fontSize)
    }

    private fun configureFavoriteStatus() {
        isFavorite = savedNote.isFavorite
        changeIconFavBtn(isFavorite)
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

    private fun configureWorkWithLinks() {
        if (savedNote.id != NEW_NOTE_ID) {
            val activeLinks = LinksManager.getActiveLinks(requireContext())

            if (activeLinks == 0) {
                binding.editTextNote2.autoLinkMask = 0
            } else {
                binding.editTextNote2.autoLinkMask = activeLinks
                binding.editTextNote2.movementMethod = movementMethod
            }
        }
    }

    private fun installObserver() {
        viewModel.getNoteState.observe(viewLifecycleOwner) { note ->
            Timber.d("HI")
            binding.editTextNote2.setText(note.title)
            binding.editTextNote2.setEndSelection()
        }
    }

    private fun setupViewsListeners() {
        movementMethod.setOnLinkClickListener { url -> showNoteLinkDialogFragment(url) }
        movementMethod.setOnNoLinkClickListener { position -> handleEmptyAreaClick(position) }
        binding.linLayoutNote2.setOnDownClickListener { handleEmptyAreaClick() }
        binding.fabNote2.setDebounceClickListener { handleFabClick() }
        binding.btnFavNote2.setOnClickListener { handleFavoriteClick() }
        binding.btnDelNote2.setDebounceClickListener { handleDeleteClick() }
        binding.btnMoreNote2.setDebounceClickListener { handleMoreClick() }
        binding.editTextNote2.setOnFocusChangeListener { _, focus -> handleEditTextFocused(focus) }
    }

    private fun showNoteLinkDialogFragment(url: String) {
        NoteLinkDialogFragment.newInstance(url).apply {
            setOnInvalidLinkOpenListener {
                showSnackbar(getString(R.string.dg_open_link_invalid), success = false)
            }
            setOnLinkCopyListener {
                showSnackbar(getString(R.string.sb_note_link_copied), success = true)
            }
        }.show(childFragmentManager, NoteLinkDialogFragment.TAG)
    }

    private fun handleEmptyAreaClick(selectionPosition: Int? = null) {
        Timber.d("handleEmptyAreaClick")
        if (binding.editTextNote2.isFocusableInTouchMode.not()) {
            binding.editTextNote2.isFocusableInTouchMode = true
        }
        if (selectionPosition != null) {
            binding.editTextNote2.setSelection(selectionPosition)
        } else {
            binding.editTextNote2.setEndSelection()
        }
        binding.editTextNote2.showKeyboard()
    }

    private fun handleFabClick() {
        binding.editTextNote2.hideKeyboard()
        lifecycleScope.launchDelay(HIDE_KEYBOARD_DELAY) { requireActivity().onBackPressed() }
    }

    private fun handleFavoriteClick() {
        val note = savedNote

        changeIconFavBtn(note.isFavorite.not())
        changeNoteFavoriteStatus(note)
    }

    private fun changeNoteFavoriteStatus(note: Note) {
        note.isFavorite = note.isFavorite.not()
        viewModel.setNote(note)
    }

    private fun handleDeleteClick() {
        val note = savedNote

        if (note.id == NEW_NOTE_ID) {
            viewModel.delete(note)
        } else {
            checkShowUndoSnackbar(note)
            note.isFavorite = false
            note.isDeleted = true
            viewModel.update(note)
        }

        requireActivity().onBackPressed()
    }

    private fun checkShowUndoSnackbar(note: Note) {
        val hasShow = AppPreference.isBottomWidgetShow(requireContext())

        if (hasShow) {
            setFragmentResult(NoteListFragment.NOTE_TRASH_REQUEST_KEY, bundleOf(NoteListFragment.NOTE_TRASH_BUNDLE_KEY to note))
        }
    }

    private fun handleMoreClick() {
        binding.editTextNote2.hideKeyboard()
        lifecycleScope.launchDelay(HIDE_KEYBOARD_DELAY) { showNoteMoreSheetDialogFragment() }
    }

    private fun showNoteMoreSheetDialogFragment() {
        NoteMoreSheetDialogFragment.newInstance(savedNote.modifiedDate.formatedString()).apply {
            setOnPasteTextListener { pasteText() }
            setOnCopyTextListener { copyText() }
            setOnShareTextListener { shareText() }
            setOnDownloadTextListener { downloadText() }
        }.show(childFragmentManager, NoteMoreSheetDialogFragment.TAG)
    }

    private fun pasteText() {
        var pastedText = ClipboardHelper.getPastedText(requireContext())

        if (pastedText != null) {
            val text = binding.editTextNote2.text.toString()
            pastedText = getCombinedText(pastedText, text)
            binding.editTextNote2.setText(pastedText)
            binding.editTextNote2.setEndSelection()
            binding.scrollViewNote2.scrollBottom(binding.editTextNote2)
        } else {
            showSnackbar(getString(R.string.sb_note_need_copy_text), success = false)
        }
    }

    private fun getCombinedText(pastedText: String, text: String): String {
        return if (text.trim().isEmpty()) {
            pastedText
        } else {
            text + "\n\n" + pastedText
        }
    }

    private fun copyText() {
        val text = binding.editTextNote2.text.toString()

        if (text.trim().isNotEmpty()) {
            ClipboardHelper.copyText(requireContext(), text)
            showSnackbar(getString(R.string.sb_note_text_copied), success = true)
        } else {
            showSnackbar(getString(R.string.sb_note_is_empty), success = false)
        }
    }

    private fun shareText() {
        val text = binding.editTextNote2.text.toString().trim()

        if (text.isNotEmpty()) {
            IntentManager.launchShareText(requireContext(), text)
        } else {
            showSnackbar(getString(R.string.sb_note_is_empty), success = false)
        }
    }

    private fun downloadText() {
        val text = binding.editTextNote2.text.toString()

        if (text.trim().isNotEmpty()) {
            val accessStorage = storageContract.accessStorage(requireContext())

            if (accessStorage) {

            } else {
                storageContract.launch()
            }
        } else {
            showSnackbar(getString(R.string.sb_note_is_empty), success = false)
        }
    }

    private fun handleEditTextFocused(focus: Boolean) {
        Timber.d("OnFocusChange: " + focus)
        if (focus) {
            movementMethod.blockWorkLinks(true)
            binding.editTextNote2.setLinkTextColor(getColor(R.color.primary_text_color))
        }
    }

    override fun onPause() {
        super.onPause()

        val note = savedNote
        val title = binding.editTextNote2.text.toString()

        if (title.trim().isNotEmpty()) {
            if (note.id == NEW_NOTE_ID) {
                note.title = title
                note.modifiedDate = LocalDateTime.now()
                note.createdDate = LocalDateTime.now()
                note.id = viewModel.insertGetId(note)

                viewModel.setNote(note)
            } else {

                if (note.title.trim() != title.trim()) {
                    note.title = title
                    note.modifiedDate = LocalDateTime.now()

                    viewModel.update(note)
                } else if (note.isFavorite != isFavorite) {
                    viewModel.update(note)
                }
            }
        } else {
            if (note.id != NEW_NOTE_ID) viewModel.delete(note)
        }
    }

    fun showSnackbar(message: String, success: Boolean) {
        AppSnackbar.make(binding.root, message, success, binding.fabNote2)?.show()
    }

    companion object {
        private const val NEW_NOTE_ID = 0L
        private const val HIDE_KEYBOARD_DELAY = 300
    }
}