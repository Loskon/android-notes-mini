package com.loskon.noteminimalism3.app.screens.note.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.notelist.presentation.NoteListFragment
import com.loskon.noteminimalism3.base.datetime.formattedString
import com.loskon.noteminimalism3.base.extension.bundle.getSerializableKtx
import com.loskon.noteminimalism3.base.extension.corutine.launchDelay
import com.loskon.noteminimalism3.base.extension.flow.observe
import com.loskon.noteminimalism3.base.extension.fragment.getColor
import com.loskon.noteminimalism3.base.extension.fragment.setOnBackPressedListener
import com.loskon.noteminimalism3.base.extension.fragment.showToast
import com.loskon.noteminimalism3.base.extension.view.scrollBottom
import com.loskon.noteminimalism3.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.extension.view.setEndSelection
import com.loskon.noteminimalism3.base.extension.view.setFocus
import com.loskon.noteminimalism3.base.extension.view.setIconColorKtx
import com.loskon.noteminimalism3.base.extension.view.setOnDownClickListener
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.base.linkmovementmethod.AppLinkMovementMethod
import com.loskon.noteminimalism3.base.widget.snackbar.AppSnackbar
import com.loskon.noteminimalism3.databinding.FragmentNoteNewBinding
import com.loskon.noteminimalism3.files.BackupFileHelper
import com.loskon.noteminimalism3.managers.IntentManager
import com.loskon.noteminimalism3.managers.LinksManager
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.LongConst
import com.loskon.noteminimalism3.utils.StringUtil
import com.loskon.noteminimalism3.utils.hideKeyboard
import com.loskon.noteminimalism3.utils.showKeyboard
import com.loskon.noteminimalism3.viewbinding.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.time.LocalDateTime

class NoteFragmentNew : Fragment(R.layout.fragment_note_new) {

    private val binding by viewBinding(FragmentNoteNewBinding::bind)
    private val viewModel: NoteViewModel by viewModel()
    private val args: NoteFragmentNewArgs by navArgs()

    private val storageContract = com.loskon.noteminimalism3.base.contracts.StorageContract(this)
    private val movementMethod = AppLinkMovementMethod()

    private var backupDate = LocalDateTime.now()
    private var isFavorite = false
    private var isNewNote = false
    private var toastShow = false

    private val getNote: Note get() = viewModel.getNoteState.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null && args.id != LongConst.ZERO) {
            viewModel.getNote(args.id)
        }
        setOnBackPressedListener {
            toastShow = true
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSavedArguments(savedInstanceState)
        establishViewsColor()
        configureKeyboardFocus()
        setNoteFontSize()
        configureFavoriteStatus()
        configureWorkWithLinks()
        installObserver()
        setupContractListener()
        setupViewsListeners()
    }

    private fun setSavedArguments(savedInstanceState: Bundle?) {
        backupDate = savedInstanceState?.getSerializableKtx(PUT_KEY_SAVE_DATE) ?: LocalDateTime.now()
    }

    private fun establishViewsColor() {
        val color = AppPreference.getColor(requireContext())
        binding.editTextNote2.setLinkTextColor(color)
        binding.fabNote2.setBackgroundColorKtx(color)
        binding.btnFavNote2.setIconColorKtx(color)
        binding.btnDelNote2.setIconColorKtx(color)
        binding.btnMoreNote2.setIconColorKtx(color)
    }

    private fun configureKeyboardFocus() {
        if (getNote.id == LongConst.ZERO) {
            binding.editTextNote2.showKeyboard()
        } else {
            binding.linLayoutNote2.setFocus()
            binding.editTextNote2.isCursorVisible = false
            binding.editTextNote2.showSoftInputOnFocus = false
        }
    }

    private fun setNoteFontSize() {
        val fontSize = AppPreference.getNoteFontSize(requireContext())
        binding.editTextNote2.setTextSizeKtx(fontSize)
    }

    private fun configureFavoriteStatus() {
        isFavorite = getNote.isFavorite
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
        if (getNote.id != LongConst.ZERO) {
            val activeLinks = LinksManager.getActiveLinks(requireContext())

            if (activeLinks != 0) {
                binding.editTextNote2.autoLinkMask = activeLinks
                binding.editTextNote2.movementMethod = movementMethod
            }
        }
    }

    private fun installObserver() {
        viewModel.getNoteState.observe(viewLifecycleOwner) { note ->
            binding.editTextNote2.setText(note.title)
            binding.editTextNote2.setEndSelection()
        }
    }

    private fun setupContractListener() {
        storageContract.setHandleGrantedListener { granted ->
            if (granted) {
                downloadTextFile()
            } else {
                showSnackbar(getString(R.string.no_permissions), false)
            }
        }
    }

    private fun setupViewsListeners() {
        movementMethod.setOnLinkClickListener { url -> showNoteLinkDialogFragment(url) }
        movementMethod.setOnNoLinkClickListener { handleEmptyAreaClick() }
        binding.linLayoutNote2.setOnDownClickListener { handleEmptyAreaClick() }
        binding.fabNote2.setDebounceClickListener { handleFabClick() }
        binding.btnFavNote2.setOnClickListener { handleFavoriteClick() }
        binding.btnDelNote2.setDebounceClickListener { handleDeleteClick() }
        binding.btnMoreNote2.setDebounceClickListener { handleMoreClick() }
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

    private fun handleEmptyAreaClick() {
        if (binding.editTextNote2.isCursorVisible.not()) turnTextEditingMode()
        binding.editTextNote2.setEndSelection()
        binding.editTextNote2.showKeyboard()
    }

    private fun turnTextEditingMode() {
        movementMethod.blockWorkLinks(true)
        binding.editTextNote2.isCursorVisible = true
        binding.editTextNote2.showSoftInputOnFocus = true
        binding.editTextNote2.setLinkTextColor(getColor(R.color.primary_text_color))
    }

    private fun handleFabClick() {
        toastShow = true
        binding.editTextNote2.hideKeyboard()
        lifecycleScope.launchDelay(HIDE_KEYBOARD_DELAY) { findNavController().popBackStack() }
    }

    private fun handleFavoriteClick() {
        val note = getNote

        changeIconFavBtn(note.isFavorite.not())
        changeNoteFavoriteStatus(note)
    }

    private fun changeNoteFavoriteStatus(note: Note) {
        note.isFavorite = note.isFavorite.not()
        viewModel.setNote(note)
    }

    private fun handleDeleteClick() {
        val note = getNote

        if (note.id == LongConst.ZERO) {
            viewModel.delete(note)
        } else {
            checkShowUndoSnackbar(note)
            note.isFavorite = false
            note.isDeleted = true
            viewModel.update(note)
        }

        findNavController().popBackStack()
    }

    private fun checkShowUndoSnackbar(note: Note) {
        val show = AppPreference.isBottomWidgetShow(requireContext())

        if (show) {
            setFragmentResult(NoteListFragment.NOTE_TRASH_REQUEST_KEY, bundleOf(NoteListFragment.NOTE_TRASH_BUNDLE_KEY to note))
        }
    }

    private fun handleMoreClick() {
        binding.editTextNote2.hideKeyboard()
        lifecycleScope.launchDelay(HIDE_KEYBOARD_DELAY) { showNoteMoreSheetDialogFragment() }
    }

    private fun showNoteMoreSheetDialogFragment() {
        NoteMoreSheetDialogFragment.newInstance(getNote.modifiedDate.formattedString()).apply {
            setOnPasteTextListener { pasteText() }
            setOnCopyTextListener { copyText() }
            setOnShareTextListener { shareText() }
            setOnDownloadTextListener { downloadTextFile() }
        }.show(childFragmentManager, NoteMoreSheetDialogFragment.TAG)
    }

    private fun pasteText() {
        var pastedText = com.loskon.noteminimalism3.base.clipboardmanager.ClipboardHelper.getPastedText(requireContext())

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
            com.loskon.noteminimalism3.base.clipboardmanager.ClipboardHelper.copyText(requireContext(), text)
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

    override fun onPause() {
        super.onPause()

        val note = getNote
        val title = binding.editTextNote2.text.toString()

        if (title.trim().isNotEmpty()) {
            if (note.id == LongConst.ZERO) {
                backupDate = LocalDateTime.now()
                isNewNote = true

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

            if (isNewNote) {
                val autoBackup = AppPreference.hasAutoBackup(requireContext())
                if (autoBackup && note.id % DIVISIBLE == LongConst.ZERO) createAutoBackup()
            }
        } else {
            if (note.id != LongConst.ZERO) viewModel.delete(note)
        }
    }

    private fun createAutoBackup() {
        val storageAccess = storageContract.storageAccess(requireContext())

        if (storageAccess) {
            val backupPath = AppPreference.getBackupPath(requireContext())
            val folderCreated = viewModel.backupFolderCreated(backupPath)

            if (folderCreated) {
                val backupName = StringUtil.replaceForbiddenCharacters(backupDate)
                val backupFilePath = "$backupPath$backupName.db"

                val databasePath = requireContext().getDatabasePath(NoteDatabaseSchema.DATABASE_NAME).toString()
                val backupSuccess = viewModel.performBackup(databasePath, backupFilePath)

                if (backupSuccess) {
                    val maxFilesCount = AppPreference.getBackupsCount(requireContext())
                    viewModel.deleteExtraFiles(backupPath, maxFilesCount)

                    val hasNotification = AppPreference.autoBackupNotification(requireContext())
                    if (hasNotification && toastShow) showToast(R.string.toast_auto_bp_completed)
                } else {
                    if (toastShow) showToast(R.string.toast_auto_bp_failed)
                }
            } else {
                if (toastShow) showToast(R.string.toast_auto_bp_failed)
            }
        } else {
            if (toastShow) showToast(R.string.toast_auto_bp_no_permissions)
        }
    }

    private fun downloadTextFile() {
        val text = binding.editTextNote2.text.toString()

        if (text.trim().isNotEmpty()) {
            val accessStorage = storageContract.storageAccess(requireContext())

            if (accessStorage) {
                val backupPath = AppPreference.getBackupPath(requireContext())
                val textFilesFolder = File(backupPath, getString(R.string.folder_text_files_name))
                val createdFolder = BackupFileHelper.folderCreated(textFilesFolder)

                if (createdFolder) {
                    val createSuccess = viewModel.createTextFile(textFilesFolder, getTextFileTitle(text), text)

                    if (createSuccess) {
                        showSnackbar(getString(R.string.sb_note_create_text_files_completed), success = true)
                    } else {
                        showSnackbar(getString(R.string.sb_note_create_text_file_failed), success = false)
                    }
                }
            } else {
                storageContract.launchAccessRequest()
            }
        } else {
            showSnackbar(getString(R.string.sb_note_is_empty), success = false)
        }
    }

    private fun getTextFileTitle(text: String): String {
        val textTitle = text.substring(0, 14.coerceAtMost(text.length))
        val timeTitle = DateUtil.getTimeNowWithBrackets()
        val textFileTitle = "$textTitle$timeTitle".trim()

        return "${StringUtil.replaceForbiddenCharacters(textFileTitle)}.txt"
    }

    fun showSnackbar(message: String, success: Boolean) {
        AppSnackbar().make(binding.root, message, success, binding.fabNote2).show()
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putSerializable(PUT_KEY_SAVE_DATE, backupDate)
    }

    companion object {
        private const val DIVISIBLE = 3
        private const val HIDE_KEYBOARD_DELAY = 200
        private const val PUT_KEY_SAVE_DATE = "PUT_KEY_SAVE_DATE"
    }
}