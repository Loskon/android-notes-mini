package com.loskon.noteminimalism3.app.presentation.screens

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortPreferenceChangeListener
import com.loskon.noteminimalism3.app.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.app.base.widget.preference.SliderPreference

class AppearanceSettingsFragmentNew : BasePreferenceFragment() {

    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var selectColorHex: Preference? = null
    private var resetColor: Preference? = null
    private var numberLinesSlider: SliderPreference? = null
    private var oneSizeCardsSwitch: SwitchPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey)
        findPreferences()
        setupPreferencesListeners()
    }

    private fun findPreferences() {
        resetFontSize = findPreference(getString(com.loskon.noteminimalism3.R.string.reset_font_size_title))
        selectColor = findPreference(getString(com.loskon.noteminimalism3.R.string.select_color_app_title))
        selectColorHex = findPreference(getString(com.loskon.noteminimalism3.R.string.select_color_app_hex_title))
        resetColor = findPreference(getString(com.loskon.noteminimalism3.R.string.reset_app_color))
        numberLinesSlider = findPreference(getString(com.loskon.noteminimalism3.R.string.number_of_lines_key))
        oneSizeCardsSwitch = findPreference(getString(com.loskon.noteminimalism3.R.string.one_size_cards_key))
    }

    private fun setupPreferencesListeners() {
        resetFontSize?.setDebouncePreferenceClickListener {
            // ResetFontSizeWarningSheetDialog(this).show()
        }
        selectColor?.setDebouncePreferenceClickListener {
            // SelectColorPickerSheetDialog(this).show()
        }
        selectColorHex?.setDebouncePreferenceClickListener {
            // SelectColorHexSheetDialog(this).show()
        }
        resetColor?.setDebouncePreferenceClickListener {
            // ResetColorWarningSheetDialog(this).show()
        }
        oneSizeCardsSwitch?.setShortPreferenceChangeListener { newValue: Boolean ->

        }
        numberLinesSlider?.setOnChangeListener { newValue: Int ->

        }
    }
}