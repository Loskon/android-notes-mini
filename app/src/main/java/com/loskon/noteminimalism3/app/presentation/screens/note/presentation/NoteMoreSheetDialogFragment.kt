package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.fragment.putArgs
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.SheetNoteMoreBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteMoreSheetDialogFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(SheetNoteMoreBinding::inflate)

    // TODO
    private val txt by lazy { arguments?.getString(asdfa) ?: "" }

    private var onPasteTextClick: (() -> Unit)? = null
    private var onCopyTextClick: (() -> Unit)? = null
    private var onShareTextClick: (() -> Unit)? = null
    private var onDownloadTextClick: (() -> Unit)? = null

    override fun getTheme(): Int = R.style.SheetDialogStatusBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()
        setupViewsListeners()
    }

    private fun setTitle() {
        binding.tvSheetNoteTitle.text = txt
    }

    private fun setupViewsListeners() {
        binding.btnSheetPaste.setDebounceClickListener {
            onPasteTextClick?.invoke()
            dismiss()
        }
        binding.btnSheetCopyAllText.setDebounceClickListener {
            onCopyTextClick?.invoke()
            dismiss()
        }
        binding.btnSheetShare.setDebounceClickListener {
            onShareTextClick?.invoke()
            dismiss()
        }
        binding.btnSheetSaveTxt.setDebounceClickListener {
            onDownloadTextClick?.invoke()
            dismiss()
        }
        binding.btnSheetClose.setDebounceClickListener {
            dismiss()
        }
    }

    fun setOnPasteTextListener(onPasteTextClick: (() -> Unit)? = null) {
        this.onPasteTextClick = onPasteTextClick
    }

    fun setOnCopyTextListener(onCopyTextClick: (() -> Unit)? = null) {
        this.onCopyTextClick = onCopyTextClick
    }

    fun setOnShareTextListener(onShareTextClick: (() -> Unit)? = null) {
        this.onShareTextClick = onShareTextClick
    }

    fun setOnDownloadTextListener(onDownloadTextClick: (() -> Unit)? = null) {
        this.onDownloadTextClick = onDownloadTextClick
    }

    companion object {
        const val TAG = "NoteMoreSheetDialogFragment"
        private const val asdfa = "asdfa"

        fun newInstance(s: String): NoteMoreSheetDialogFragment {
            return NoteMoreSheetDialogFragment().putArgs {
                putString(asdfa, s)
            }
        }
    }
}