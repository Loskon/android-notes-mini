package com.loskon.noteminimalism3.app.base.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceNavigationClickListener
import com.loskon.noteminimalism3.app.base.widget.snackbar.AppSnackbar
import com.loskon.noteminimalism3.databinding.FragmentSettingsBinding
import com.loskon.noteminimalism3.managers.setNavigationIconColor
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

open class BasePreferenceFragment : PreferenceFragmentCompat() {

    private val binding by viewBinding(FragmentSettingsBinding::bind)

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.isVerticalScrollBarEnabled = false

        val color = AppPreference.getColor(requireContext())
        binding.bottomBarSettings.setNavigationIconColor(color)
        binding.bottomBarSettings.setDebounceNavigationClickListener { findNavController().popBackStack() }
    }

    fun showSnackbar(message: String, success: Boolean) {
        AppSnackbar().make(binding.root, message, success, binding.bottomBarSettings)
    }
}