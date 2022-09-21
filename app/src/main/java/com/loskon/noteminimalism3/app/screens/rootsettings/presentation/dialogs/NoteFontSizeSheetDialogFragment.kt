package com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs

import android.os.Bundle
import android.view.View
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.fragment.getInteger
import com.loskon.noteminimalism3.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.base.extension.view.setOnChangeListener
import com.loskon.noteminimalism3.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.SheetNoteFontSizeNewBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteFontSizeSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(SheetNoteFontSizeNewBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setDialogViewsParameters()
        establishViewsColor()
        configureViewsParameters()
        setupViewsListeners()
    }

    private fun setDialogViewsParameters() {
        setDialogTitle(R.string.sheet_font_size_title)
    }

    private fun establishViewsColor() {
        binding.incSliderFontSize.sliderPreference.setColorKtx(getAppColor())
    }

    private fun configureViewsParameters() {
        val fontSize = AppPreference.getNoteFontSize(requireContext())
        binding.incSliderFontSize.sliderPreference.valueFrom = MIN
        binding.incSliderFontSize.sliderPreference.valueTo = MAX
        binding.incSliderFontSize.sliderPreference.value = fontSize.toFloat()
        binding.incSliderFontSize.tvValueSliderPreference.text = fontSize.toString()
    }

    private fun setupViewsListeners() {
        binding.incSliderFontSize.sliderPreference.setOnChangeListener { _, value, _ ->
            binding.incSliderFontSize.tvValueSliderPreference.text = value.toString()
            binding.tvSheetFontSize.setTextSizeKtx(value)
        }
        binding.btnSheetFontSizeReset.setOnClickListener {
            val defaultFontSize = getInteger(R.integer.note_font_size_int)
            binding.incSliderFontSize.sliderPreference.value = defaultFontSize.toFloat()
            binding.incSliderFontSize.tvValueSliderPreference.text = defaultFontSize.toString()
            binding.tvSheetFontSize.setTextSizeKtx(defaultFontSize)
        }
        setOkClickListener {
            val fontSize = binding.incSliderFontSize.sliderPreference.value.toInt()
            AppPreference.setNoteFontSize(requireContext(), fontSize)
        }
    }

    companion object {
        private const val MIN = 14f
        private const val MAX = 40f
    }
}