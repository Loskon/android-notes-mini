package com.loskon.noteminimalism3.app.base.presentation.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.dp
import com.loskon.noteminimalism3.app.base.extension.view.setColor
import com.loskon.noteminimalism3.sharedpref.AppPreference

class WaitingDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.RoundedWrapDialogStyle).create().apply {
            val progressIndicator = CircularProgressIndicator(requireContext()).apply {
                val color = AppPreference.getColor(requireContext())
                val width = ViewGroup.LayoutParams.WRAP_CONTENT
                val height = ViewGroup.LayoutParams.WRAP_CONTENT

                setColor(color)
                updatePadding(12.dp, 12.dp, 12.dp, 12.dp)
                layoutParams = ViewGroup.LayoutParams(width, height)
                isIndeterminate = true
            }
            setView(progressIndicator)
        }
    }

    companion object {
        const val TAG = "WaitingDialogFragment"
        fun newInstance(): WaitingDialogFragment = WaitingDialogFragment()
    }
}