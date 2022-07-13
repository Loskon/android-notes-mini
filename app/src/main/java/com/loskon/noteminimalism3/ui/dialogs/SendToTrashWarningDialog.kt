package com.loskon.noteminimalism3.ui.dialogs

import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.MainActivity
import com.loskon.noteminimalism3.ui.basedialogs.BaseMaterialDialog
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener

/**
 * Окно для подтверждения очистки корзины
 */

class SendToTrashWarningDialog(private val activity: MainActivity) :
    BaseMaterialDialog(activity) {

    init {
        configureDialogParameters()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.dg_trash_title)
    }

    fun show(itemCount: Int) {
        setupViewsListeners(itemCount)
        super.show()
    }

    private fun setupViewsListeners(itemCount: Int) {
        btnOk.setDebounceClickListener { onOkBtnClick(itemCount) }
    }

    private fun onOkBtnClick(itemCount: Int) {
        if (itemCount != 0) {
            activity.cleanTrash()
        } else {
            activity.showSnackbar(WarningSnackbar.MSG_BUT_EMPTY_TRASH)
        }

        dismiss()
    }
}