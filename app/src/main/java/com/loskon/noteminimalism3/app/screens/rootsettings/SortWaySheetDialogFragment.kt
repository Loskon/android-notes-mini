package com.loskon.noteminimalism3.app.screens.rootsettings

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.view.setBackgroundColorKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetSortWayBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class SortWaySheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetSortWayBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setupViewsParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun setupViewsParameters() {
        setDialogTitle(R.string.sort_title)
    }

    private fun establishViewsColor() {
        binding.rbSortCreation.setBackgroundColorKtx(color)
        binding.rbSortModification.setBackgroundColorKtx(color)
    }

    private fun configureInsertedViews() {
        val sortWay = AppPreference.getSortingWay(requireContext())

        if (sortWay == 1) {
            binding.rgSort.check(binding.rbSortModification.id)
        } else {
            binding.rgSort.check(binding.rbSortCreation.id)
        }
    }

    private fun setupViewsListeners() {
        setOkClickListener {
            saveSortingWay()
            dismiss()
        }
    }

    private fun saveSortingWay() {
        val sortWay = getSortWayNumber()
        AppPreference.setSortingWay(requireContext(), sortWay)
    }

    private fun getSortWayNumber(): Int {
        return if (binding.rgSort.checkedRadioButtonId == binding.rbSortModification.id) {
            1
        } else {
            0
        }
    }
}