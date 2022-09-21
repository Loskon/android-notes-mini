package com.loskon.noteminimalism3.app.screens.rootsettings.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.RootSettingsFragment
import com.loskon.noteminimalism3.base.extension.view.setColorKtx
import com.loskon.noteminimalism3.base.extension.view.setOnChangeListener
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.AppBaseSheetDialogFragment
import com.loskon.noteminimalism3.databinding.PreferenceSliderBinding
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class RetentionTimeSheetDialogFragment : AppBaseSheetDialogFragment() {

    private val binding by viewBinding(PreferenceSliderBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentView(binding.root)

        setDialogViewsParameters()
        establishViewsColor()
        configureViewsParameters()
        setupViewsListeners()
    }

    private fun setDialogViewsParameters() {
        setDialogTitle(R.string.retention_trash_key)
    }

    private fun establishViewsColor() {
        binding.sliderPreference.setColorKtx(getAppColor())
    }

    private fun configureViewsParameters() {
        val days = AppPreference.getRetentionDays(requireContext())
        binding.sliderPreference.valueFrom = MIN
        binding.sliderPreference.valueTo = MAX
        binding.sliderPreference.value = days.toFloat()
        binding.tvValueSliderPreference.text = days.toString()
    }

    private fun setupViewsListeners() {
        binding.sliderPreference.setOnChangeListener { _, value, _ ->
            binding.tvValueSliderPreference.text = value.toString()
        }
        setOkClickListener {
            val days = binding.sliderPreference.value.toInt()
            val bundle = bundleOf(RootSettingsFragment.RETENTION_TIME_REQUEST_KEY to days)

            AppPreference.setRetentionTime(requireContext(), days)
            setFragmentResult(RootSettingsFragment.RETENTION_TIME_REQUEST_KEY, bundle)
        }
    }

    companion object {
        private const val MIN = 1f
        private const val MAX = 30f
    }
}