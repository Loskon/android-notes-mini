package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно подтверждения облачного бэкапа/восстановления
 */

class CloudConfirmSheetDialog(
    sheetContext: Context,
    private val fragment: BackupFragment
) :
    BaseSheetDialog(sheetContext, null) {

    init {
        setTitleDialog(R.string.sheet_confirm_action)
        setTextBtnOk(R.string.continue_action)
        setTextBtnCancel(R.string.no)
        setContainerVisibility(false)
    }

    fun show(isBackup: Boolean) {
        btnOk.setOnSingleClickListener { onOkBtnClick(isBackup) }
        super.show()
    }

    private fun onOkBtnClick(isBackup: Boolean) {
        dismiss()
        chooseWorkWay(isBackup)
    }

    private fun chooseWorkWay(isBackup: Boolean) {
        if (isBackup) {
            fragment.performBackupCloud()
        } else {
            fragment.performRestoreCloud()
        }
    }
}