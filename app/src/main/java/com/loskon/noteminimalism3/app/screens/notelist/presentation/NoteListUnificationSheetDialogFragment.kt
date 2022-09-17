package com.loskon.noteminimalism3.app.screens.notelist.presentation

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.DialogUnificationBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteListUnificationSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(DialogUnificationBinding::inflate)

    private var onDeleteClickListener: (() -> Unit)? = null
    private var onLeaveClickListener: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContentView(binding.root)
        setupDialogViewsParameters()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun setupDialogViewsParameters() {
        setDialogTitle(R.string.dg_unification_title)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        binding.btnDeleteNotes.setBackgroundColor(color)
        binding.btnLeaveNotes.setBackgroundColor(color)
    }

    private fun setupViewsListeners() {
        binding.btnDeleteNotes.setDebounceClickListener {
            onDeleteClickListener?.invoke()
            dismiss()
        }
        binding.btnLeaveNotes.setDebounceClickListener {
            onLeaveClickListener?.invoke()
            dismiss()
        }
    }

    fun setOnDeleteClickListener(onDeleteClickListener: (() -> Unit)?) {
        this.onDeleteClickListener = onDeleteClickListener
    }

    fun setOnLeaveClickListener(onLeaveClickListener: (() -> Unit)?) {
        this.onLeaveClickListener = onLeaveClickListener
    }

    companion object {
        const val TAG = "NoteListUnificationSheetDialog"

        fun newInstance() = NoteListUnificationSheetDialogFragment()
    }
}