package com.loskon.noteminimalism3.app.screens.appearancesettings.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.appearancesettings.AppearanceSettingsFragment
import com.loskon.noteminimalism3.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetColorPickerBinding
import com.loskon.noteminimalism3.viewbinding.viewBinding

class ColorPickerSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetColorPickerBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setDialogViewsParameters()
        establishViewsColor(savedInstanceState)
        configureViewsParameters()
        setupViewsListeners()
    }

    private fun setDialogViewsParameters() {
        setDialogTitle(R.string.select_color_app_title)
    }

    private fun establishViewsColor(savedInstanceState: Bundle?) {
        binding.colorPicker.color = savedInstanceState?.getInt(PUT_KEY_SAVE_COLOR) ?: getAppColor()
    }

    private fun configureViewsParameters() {
        binding.colorPicker.addSVBar(binding.svColorBar)
        binding.colorPicker.showOldCenterColor = false
    }

    private fun setupViewsListeners() {
        binding.btnColorPickerReset.setDebounceClickListener {
            binding.colorPicker.color = getAppColor()
        }
        setOkClickListener {
            val bundle = bundleOf(AppearanceSettingsFragment.SET_COLOR_REQUEST_KEY to binding.colorPicker.color)
            setFragmentResult(AppearanceSettingsFragment.SET_COLOR_REQUEST_KEY, bundle)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(PUT_KEY_SAVE_COLOR, binding.colorPicker.color)
    }

    companion object {
        private const val PUT_KEY_SAVE_COLOR = "PUT_KEY_SAVE_COLOR"
    }
}