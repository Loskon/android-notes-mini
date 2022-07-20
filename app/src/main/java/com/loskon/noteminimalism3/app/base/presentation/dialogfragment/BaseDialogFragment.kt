package com.loskon.noteminimalism3.app.base.presentation.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.loskon.noteminimalism3.R

open class BaseDialogFragment : DialogFragment() {

    open val isCancel = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), R.style.RoundedDialogStyle).apply {
            val dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
            dialogView?.let { setView(dialogView) }
            isCancelable = isCancel
        }.create()
    }
}