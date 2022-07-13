package com.loskon.noteminimalism3.ui.sheetdialogs

import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener

/**
 * Окно для подтверждение удаления гугл-аккаунта
 */

class DeleteGoogleAccountSheetDialog(private val fragment: BackupFragment) :
    BaseSheetDialog(fragment.requireContext(), R.layout.sheet_delete_account_warning) {

    private val btnYes: MaterialButton = view.findViewById(R.id.btn_data_yes)
    private val btnNo: MaterialButton = view.findViewById(R.id.btn_data_no)

    init {
        configureDialogParameters()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_delete_warnings)
        setBtnOkVisibility(false)
        setBtnCancelVisibility(false)
    }

    private fun establishViewsColor() {
        btnNo.setBackgroundColor(color)
    }

    private fun setupViewsListeners() {
        btnYes.setDebounceClickListener { onYesBtnClick() }
        btnNo.setDebounceClickListener { dismiss() }
    }

    private fun onYesBtnClick() {
        fragment.deleteUserAccount()
        dismiss()
    }
}