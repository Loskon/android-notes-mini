package com.loskon.noteminimalism3.app.presentation.screens

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.presentation.sheetdialog.BaseCustomSheetDialog
import com.loskon.noteminimalism3.databinding.SheetAccountBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class AccountSheetDialog(context: Context) : BaseCustomSheetDialog(context) {

    private val binding by viewBinding(SheetAccountBinding::inflate)

    init {
        insertView(binding.root)
        setupViewsParameters()
        establishViewsColor()
    }

    private fun setupViewsParameters() {
        setTitleDialog(R.string.sheet_account_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        binding.btnAccountSignOut.setBackgroundColor(color)
        binding.btnAccountDelete.setBackgroundColor(color)
    }

    fun setOnClickListeners(
        btnSignOutOnClick: () -> Unit,
        btnDeleteOnClick: () -> Unit
    ): AccountSheetDialog {
        binding.btnAccountSignOut.setDebounceClickListener {
            btnSignOutOnClick()
            dismiss()
        }
        binding.btnAccountDelete.setDebounceClickListener {
            btnDeleteOnClick()
            dismiss()
        }

        return this
    }

    /*    fun setUp

        inline fun create(functions: AccountSheetDialog.() -> Unit): AccountSheetDialog {
            this.functions()
            return this
        }*/
}