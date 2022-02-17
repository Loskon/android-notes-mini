package com.loskon.noteminimalism3.ui.dialogs

import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждение объединения выбранных заметок
 */

class UnificationDialog(private val activity: MainActivity) :
    BaseMaterialDialog(activity, R.layout.dialog_unification) {

    private val btnDelete: Button = view.findViewById(R.id.btn_delete_notes)
    private val btnLeave: Button = view.findViewById(R.id.btn_leave_notes)

    init {
        configureDialogParameters()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.dg_unification_title)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        btnDelete.setBackgroundColor(color)
        btnLeave.setBackgroundColor(color)
    }

    private fun setupViewsListeners() {
        btnDelete.setOnSingleClickListener { onDeleteBtnClick() }
        btnLeave.setOnSingleClickListener { onLeaveBtnClick() }
    }

    private fun onDeleteBtnClick() {
        dismiss()
        activity.performUnificationNotes(true)
    }

    private fun onLeaveBtnClick() {
        dismiss()
        activity.performUnificationNotes(false)
    }
}