package com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setButtonTintColorKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetSortWayBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.IntConst
import com.loskon.noteminimalism3.viewbinding.viewBinding

class SortWaySheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetSortWayBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setDialogViewsParameters()
        establishViewsColor()
        configureViewsParameters()
        setupViewsListeners()
    }

    private fun setDialogViewsParameters() {
        setDialogTitle(R.string.sort_title)
    }

    private fun establishViewsColor() {
        val color = getAppColor()
        binding.rbSortCreation.setButtonTintColorKtx(color)
        binding.rbSortModification.setButtonTintColorKtx(color)
    }

    private fun configureViewsParameters() {
        val sortWay = AppPreference.getSortingWay(requireContext())

        if (sortWay == IntConst.ZERO) {
            binding.rgSort.check(binding.rbSortModification.id)
        } else {
            binding.rgSort.check(binding.rbSortCreation.id)
        }
    }

    private fun setupViewsListeners() {
        setOkClickListener {
            val sortWay = getSortWayNumber()
            AppPreference.setSortingWay(requireContext(), sortWay)
        }
    }

    private fun getSortWayNumber(): Int {
        return if (binding.rgSort.checkedRadioButtonId == binding.rbSortModification.id) {
            IntConst.ONE
        } else {
            IntConst.ZERO
        }
    }
}