package com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment

import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.loskon.noteminimalism3.R

open class BaseSheetDialogFragmentNew : BottomSheetDialogFragment() {

    protected open val isDraggableStatus = true
    protected open val isHideableStatus = true

    override fun getTheme(): Int = R.style.RoundedSheetDialogStyle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

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
}