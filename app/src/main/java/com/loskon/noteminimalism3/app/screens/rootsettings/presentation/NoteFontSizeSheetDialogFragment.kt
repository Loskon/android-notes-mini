package com.loskon.noteminimalism3.app.screens.rootsettings.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.fragment.getInteger
import com.loskon.noteminimalism3.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.base.extension.view.setOnChangeListener
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetNoteFontSizeNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteFontSizeSheetDialogFragment: AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetNoteFontSizeNewBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setupViewsParameters()
        establishViewsColor()
        configureInsertedViews()
        setupViewsListeners()
    }

    private fun setupViewsParameters() {
        setDialogTitle(R.string.sheet_font_size_title)
    }

    private fun establishViewsColor() {
        binding.incSliderFontSize.sliderPreference.setColorKtx(color)
    }

    private fun configureInsertedViews() {
        val value = AppPreference.getNoteFontSize(requireContext())
        binding.incSliderFontSize.sliderPreference.valueFrom = MIN
        binding.incSliderFontSize.sliderPreference.valueTo = MAX
        binding.incSliderFontSize.sliderPreference.value = value.toFloat()
        binding.incSliderFontSize.tvValueSliderPreference.text = value.toString()
    }

    private fun setupViewsListeners() {
        binding.incSliderFontSize.sliderPreference.setOnChangeListener { _, value, _ ->
            binding.incSliderFontSize.tvValueSliderPreference.text = value.toString()
            binding.tvSheetFontSize.setTextSizeKtx(value)
        }
        binding.btnSheetFontSizeReset.setOnClickListener {
            val default = getInteger(R.integer.note_font_size_int)
            binding.incSliderFontSize.sliderPreference.value = default.toFloat()
            binding.incSliderFontSize.tvValueSliderPreference.text = default.toString()
            binding.tvSheetFontSize.setTextSizeKtx(default)
        }
        setOkClickListener {
            val value = binding.incSliderFontSize.sliderPreference.value.toInt()
            val bundle = bundleOf(RootSettingsFragment.NOTE_FONT_SIZE_REQUEST_KEY to value)

            setFragmentResult(RootSettingsFragment.NOTE_FONT_SIZE_REQUEST_KEY, bundle)
        }
    }

    companion object {
        private const val MIN = 14f
        private const val MAX = 40f
    }
}