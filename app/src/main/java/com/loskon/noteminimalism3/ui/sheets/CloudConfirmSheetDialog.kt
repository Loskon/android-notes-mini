package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Подтверждение действия при облачном резервном копировании
 */

class CloudConfirmSheetDialog(context: Context, private val fragment: BackupFragment) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)

    init {
        dialog.setTextTitle(R.string.sheet_confirm_action)
        dialog.setTextBtnOk(R.string.continue_action)
        dialog.setTextBtnCancel(R.string.no)
        dialog.setContainerVisibility(false)
    }

    fun show(isBackup: Boolean) {
        dialog.buttonOk.setOnSingleClickListener {
            dialog.dismiss()

            if (isBackup) {
                fragment.performBackupCloud()
            } else {
                fragment.performRestoreCloud()
            }
        }

        dialog.show()
    }
}