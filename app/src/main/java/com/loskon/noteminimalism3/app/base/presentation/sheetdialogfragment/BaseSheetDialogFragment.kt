package com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loskon.noteminimalism3.R

open class BaseSheetDialogFragment(
    @LayoutRes val layoutId: Int = 0
) : BottomSheetDialogFragment() {

    open val isDraggableStatus = true
    open val isHideableStatus = true

    override fun getTheme(): Int = R.style.RoundedSheetDialogStyle

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
            isDraggable = isDraggableStatus
            isHideable = isHideableStatus
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutId != 0) {
            inflater.inflate(layoutId, container, false)
        } else {
            null
        }
    }
}