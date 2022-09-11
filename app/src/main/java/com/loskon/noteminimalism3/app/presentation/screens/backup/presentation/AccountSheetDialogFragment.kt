package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetAccountBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class AccountSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetAccountBinding::inflate)

    private var btnSignOutOnClick: (() -> Unit)? = null
    private var btnDeleteOnClick: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setContentView(binding.root)
        setupViewsParameters()
        establishViewsColor()
        setupViewsListeners()
    }

    private fun setupViewsParameters() {
        setDialogTitle(R.string.sheet_account_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        binding.btnAccountSignOut.setBackgroundColor(color)
        binding.btnAccountDelete.setBackgroundColor(color)
    }

    private fun setupViewsListeners() {
        binding.btnAccountSignOut.setDebounceClickListener {
            btnSignOutOnClick?.invoke()
            dismiss()
        }
        binding.btnAccountDelete.setDebounceClickListener {
            btnDeleteOnClick?.invoke()
            dismiss()
        }
    }

    fun setOnSignOutClickListener(btnSignOutOnClick: (() -> Unit)?) {
        this.btnSignOutOnClick = btnSignOutOnClick
    }

    fun setOnDeleteClickListener(btnDeleteOnClick: (() -> Unit)?) {
        this.btnDeleteOnClick = btnDeleteOnClick
    }

    companion object {
        const val TAG = "AccountSheetDialogFragment"
        fun newInstance() = AccountSheetDialogFragment()
    }
}