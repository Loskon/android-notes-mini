package com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetAboutAppBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class AboutAppSheetDialogFragment: AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetAboutAppBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setDialogViewsParameters()
        configureViewsParameters()
    }

    private fun setDialogViewsParameters() {
        setDialogTitle(R.string.app_name)
        setTextBtnCancel(R.string.to_close)
        setBtnOkVisibility(false)
    }

    private fun configureViewsParameters() {
        binding.tvAboutAppVersion.text = getString(R.string.sheet_about_version, BuildConfig.VERSION_NAME)
    }
}