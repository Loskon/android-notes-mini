package com.loskon.noteminimalism3.ui.sheetdialogs

import android.content.Context
import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.managers.setStrokeBtnColor
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.utils.setOnSingleClickListener

/**
 * Окно для подтверждение удаления гугл-аккаунта
 */

class DeleteGoogleAccountSheetDialog(
    sheetContext: Context,
    private val fragment: BackupFragment
) : BaseSheetDialog(sheetContext, R.layout.sheet_delete_account_warning) {

    private val btnYes: MaterialButton = view.findViewById(R.id.btn_data_yes)
    private val btnNo: MaterialButton = view.findViewById(R.id.btn_data_no)

    init {
        configureDialogParameters()
        establishViewsColor()
        installHandlersForViews()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_delete_warnings)
        setBtnOkVisibility(false)
        setBtnCancelVisibility(false)
    }

    private fun establishViewsColor() {
        btnNo.setBackgroundColor(color)
        btnYes.setStrokeBtnColor(color)
        btnYes.setTextColor(color)
    }

    private fun installHandlersForViews() {
        btnYes.setOnSingleClickListener { onYesBtnClick() }
        btnNo.setOnSingleClickListener { dismiss() }
    }

    private fun onYesBtnClick() {
        fragment.deleteUserAccount()
        dismiss()
    }
}