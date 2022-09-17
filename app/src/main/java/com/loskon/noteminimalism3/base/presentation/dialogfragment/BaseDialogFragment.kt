package com.loskon.noteminimalism3.base.presentation.dialogfragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.loskon.noteminimalism3.R

open class BaseDialogFragment : DialogFragment() {

    open val isDialogCancelable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = isDialogCancelable
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.RoundedMatchDialogStyle).apply {
            val dialogView = onCreateView(requireActivity().layoutInflater, null, savedInstanceState)
            dialogView?.let { setView(dialogView) }
        }.create()
    }
}