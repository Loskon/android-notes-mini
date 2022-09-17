package com.loskon.noteminimalism3.ui.sheetdialogs

import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.BackupFragment

/**
 * Окно для действий с гугл-аккаунтом
 */

class GoogleAccountSheetDialog(private val fragment: BackupFragment
) : BaseSheetDialog(fragment.requireContext(), R.layout.sheet_account) {

    private val btnLogout: MaterialButton = view.findViewById(R.id.btn_account_sign_out)
    private val btnDelete: MaterialButton = view.findViewById(R.id.btn_account_delete)

    init {
        configureDialogParameters()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun configureDialogParameters() {
        setTitleDialog(R.string.sheet_account_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        btnLogout.setBackgroundColor(color)
        btnDelete.setBackgroundColor(color)
    }

    private fun setupViewsListeners() {
        btnLogout.setDebounceClickListener { onLogoutBtnClick() }
        btnDelete.setDebounceClickListener { onDeleteBtnClick() }
    }

    private fun onLogoutBtnClick() {
        dismiss()
        fragment.signOutFromGoogle()
    }

    private fun onDeleteBtnClick() {
        dismiss()
        DeleteGoogleAccountSheetDialog(fragment).show()
    }
}