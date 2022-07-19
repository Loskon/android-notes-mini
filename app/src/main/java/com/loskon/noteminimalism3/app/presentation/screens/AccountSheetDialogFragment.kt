package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.fragment.setFragmentClick
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.presentation.dialogfragment.BaseCustomizedSheetDialogFragment
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.DELETE_ACCOUNT_REQUEST_KEY
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupNewFragment.Companion.SIGN_OUT_REQUEST_KEY
import com.loskon.noteminimalism3.databinding.SheetAccountBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class AccountSheetDialogFragment : BaseCustomizedSheetDialogFragment(R.layout.sheet_account) {

    private val binding by viewBinding(SheetAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewsParameters()
        establishViewsColor()
        setupViewListener()
    }

    private fun setupViewsParameters() {
        setTitleDialog(R.string.sheet_account_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        binding.btnDataAccountLogout.setBackgroundColor(color)
        binding.btnDataAccountDelete.setBackgroundColor(color)
    }

    private fun setupViewListener() {
        binding.btnDataAccountLogout.setDebounceClickListener {
            setFragmentClick(SIGN_OUT_REQUEST_KEY)
            dismiss()
        }
        binding.btnDataAccountDelete.setDebounceClickListener {
            setFragmentClick(DELETE_ACCOUNT_REQUEST_KEY)
            dismiss()
        }
    }
}