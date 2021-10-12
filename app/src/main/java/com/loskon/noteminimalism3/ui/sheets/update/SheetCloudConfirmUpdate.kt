package com.loskon.noteminimalism3.ui.sheets.update

import android.content.Context
import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.update.BackupFragment
import com.loskon.noteminimalism3.ui.sheets.BaseSheetDialog
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Потверждение действия при облачном резервном копировании
 */

class SheetCloudConfirmUpdate(context: Context, private val fragment: BackupFragment) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(context)

    private val btnOk: Button = sheetDialog.buttonOk

    init {
        sheetDialog.setTextTitle(R.string.sheet_confirm_action)
        sheetDialog.setTextBtnOk(R.string.continue_action)
        sheetDialog.setTextBtnCancel(R.string.no)
        sheetDialog.setContainerVisibility(false)
    }

    fun show(isBackup: Boolean) {
        btnOk.setOnSingleClickListener {
            sheetDialog.dismiss()

            if (isBackup) {
                fragment.performBackupCloud()
            } else {
                fragment.performRestoreCloud()
            }
        }

        sheetDialog.show()
    }
}