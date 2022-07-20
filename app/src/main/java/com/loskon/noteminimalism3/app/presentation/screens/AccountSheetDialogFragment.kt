package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.BaseAppSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetAccountBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class AccountSheetDialogFragment : BaseAppSheetDialogFragment(R.layout.sheet_account) {

    private val binding by viewBinding(SheetAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    /*    private fun setupViewsListeners() {
            binding.btnAccountSignOut.setDebounceClickListener {
                setFragmentClick(SIGN_OUT_REQUEST_KEY)
                dismiss()
            }
            binding.btnAccountDelete.setDebounceClickListener {
                setFragmentClick(DELETE_ACCOUNT_REQUEST_KEY)
                dismiss()
            }
        }*/

/*    fun setOnClickListeners(
        btnSignOutOnClick: () -> Unit,
        btnDeleteOnClick: () -> Unit
    ): AccountSheetDialogFragment {
        binding.btnAccountSignOut.setDebounceClickListener {
            btnSignOutOnClick()
            dismiss()
        }
        binding.btnAccountDelete.setDebounceClickListener {
            btnDeleteOnClick()
            dismiss()
        }

        return this
    }*/

    companion object {
        const val TAG = "AccountSheetDialogFragment"
        fun newInstance() = AccountSheetDialogFragment()
    }
}