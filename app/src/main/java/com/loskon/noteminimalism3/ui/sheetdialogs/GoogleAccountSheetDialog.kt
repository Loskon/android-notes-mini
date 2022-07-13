package com.loskon.noteminimalism3.ui.sheetdialogs

import com.google.android.material.button.MaterialButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.basedialogs.BaseSheetDialog
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener

/**
 * Окно для действий с гугл-аккаунтом
 */

class GoogleAccountSheetDialog(private val fragment: BackupFragment
) : BaseSheetDialog(fragment.requireContext(), R.layout.sheet_google_account) {

    private val btnLogout: MaterialButton = view.findViewById(R.id.btn_data_account_logout)
    private val btnDelete: MaterialButton = view.findViewById(R.id.btn_data_account_delete)

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