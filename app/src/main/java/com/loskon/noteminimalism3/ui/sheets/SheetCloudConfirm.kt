package com.loskon.noteminimalism3.ui.sheets

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Потверждение действия при облачном резервном копировании
 */

class SheetCloudConfirm(context: Context, private val fragment: BackupFragment) {

    private val dialog: BaseSheetDialog = BaseSheetDialog(context)

    private val btnOk: Button = dialog.buttonOk

    init {
        dialog.setTextTitle(R.string.sheet_confirm_action)
        dialog.setTextBtnOk(R.string.continue_action)
        dialog.setTextBtnCancel(R.string.no)
        dialog.setContainerVisibility(false)
    }

    fun show(isBackup: Boolean) {
        btnOk.setOnSingleClickListener {
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