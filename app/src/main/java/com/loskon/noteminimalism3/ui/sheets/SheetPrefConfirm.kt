package com.loskon.noteminimalism3.ui.sheets

import android.widget.Button
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.prime.BpCloud
import com.loskon.noteminimalism3.ui.activities.BackupActivity
import com.loskon.noteminimalism3.utils.setMargins
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 *
 */

class SheetPrefConfirm(private val activity: BackupActivity) {

    private val sheetDialog: BaseSheetDialog = BaseSheetDialog(activity)

    private val btnOk: Button = sheetDialog.getButtonOk

    init {
        sheetDialog.setTextTitle(activity.getString(R.string.dialog_confirm_action))
        sheetDialog.setBtnCancelVisibility(false)
        sheetDialog.setContainerVisibility(false)
        btnOk.text = activity.getString(R.string.continue_action)
        btnOk.setMargins(16, 32, 16, 32)
    }

    fun build(isBackup: Boolean) {
        val bpCloud: BpCloud = activity.bpCloud

        btnOk.setOnSingleClickListener {
            bpCloud.backupAndRestore(isBackup)
            sheetDialog.dismiss()
        }

        sheetDialog.show()
    }
}