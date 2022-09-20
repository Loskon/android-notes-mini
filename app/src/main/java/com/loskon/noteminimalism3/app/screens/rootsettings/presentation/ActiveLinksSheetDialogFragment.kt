package com.loskon.noteminimalism3.app.screens.rootsettings.presentation

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setButtonTintColorKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetLinksBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class ActiveLinksSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetLinksBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setupViewsParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun setupViewsParameters() {
        setDialogTitle(R.string.sheet_pref_links_title)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun establishViewsColor() {
        binding.checkBoxWeb.setButtonTintColorKtx(color)
        binding.checkBoxMail.setButtonTintColorKtx(color)
        binding.checkBoxPhone.setButtonTintColorKtx(color)
    }

    private fun configureInsertedViews() {
        binding.checkBoxWeb.isChecked = AppPreference.isWebLink(requireContext())
        binding.checkBoxMail.isChecked = AppPreference.isMailLink(requireContext())
        binding.checkBoxPhone.isChecked = AppPreference.isPhoneLink(requireContext())
    }

    private fun setupViewsListeners() {
        binding.checkBoxWeb.setOnCheckedChangeListener { _, value ->
            AppPreference.setResultWeb(requireContext(), value)
        }
        binding.checkBoxMail.setOnCheckedChangeListener { _, value ->
            AppPreference.setResultMail(requireContext(), value)
        }
        binding.checkBoxPhone.setOnCheckedChangeListener { _, value ->
            AppPreference.setResultPhone(requireContext(), value)
        }
    }
}