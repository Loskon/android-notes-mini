package com.loskon.noteminimalism3.ui.materialdialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждение объединения выбранных заметок
 */

class UnificationDialog(private val activity: MainActivity) : BaseMaterialDialog(activity) {

    init {
        configureDialogParameters()
        setupViewListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.dg_unification_title)
    }

    private fun setupViewListeners() {
        btnOk.setOnSingleClickListener { onOkBtnClick() }
    }

    private fun onOkBtnClick() {
        dismiss()
        activity.performUnificationNotes()
    }
}