package com.loskon.noteminimalism3.app.base.presentation.dialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.fragment.getDimen
import com.loskon.noteminimalism3.app.base.extension.view.setColor
import com.loskon.noteminimalism3.sharedpref.AppPreference

class WaitingDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.RoundedWrapDialogStyle).create().apply {
            setView(
                CircularProgressIndicator(requireContext()).apply {
                    val color = AppPreference.getColor(requireContext())
                    val dimen = getDimen(R.dimen.margin_xmedium)
                    val width = ViewGroup.LayoutParams.WRAP_CONTENT
                    val height = ViewGroup.LayoutParams.WRAP_CONTENT

                    setColor(color)
                    updatePadding(dimen, dimen, dimen, dimen)
                    layoutParams = ViewGroup.LayoutParams(width, height)
                    isIndeterminate = true
                }
            )
        }
    }

    companion object {
        const val TAG = "WaitingDialogFragment"
        fun newInstance(): WaitingDialogFragment = WaitingDialogFragment()
    }
}