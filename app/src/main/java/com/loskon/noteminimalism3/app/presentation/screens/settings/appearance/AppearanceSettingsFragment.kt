package com.loskon.noteminimalism3.app.presentation.screens.settings.appearance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.fragment.getInteger
import com.loskon.noteminimalism3.app.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortPreferenceChangeListener
import com.loskon.noteminimalism3.app.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.app.base.widget.preference.NoteCardViewPreference
import com.loskon.noteminimalism3.app.base.widget.preference.SliderPreference
import com.loskon.noteminimalism3.sharedpref.AppPreference

@SuppressLint("NotifyDataSetChanged")
class AppearanceSettingsFragment : BasePreferenceFragment() {

    private var noteCardView: NoteCardViewPreference? = null
    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var selectColorHex: Preference? = null
    private var resetColor: Preference? = null
    private var fontSizeSlider: SliderPreference? = null
    private var numberLinesSlider: SliderPreference? = null
    private var oneSizeCardsSwitch: SwitchPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey)
        findPreferences()
        setupPreferencesListeners()
    }

    private fun findPreferences() {
        noteCardView = findPreference("one")
        resetFontSize = findPreference(getString(com.loskon.noteminimalism3.R.string.reset_font_size_title))
        selectColor = findPreference(getString(com.loskon.noteminimalism3.R.string.select_color_app_title))
        selectColorHex = findPreference(getString(com.loskon.noteminimalism3.R.string.select_color_app_hex_title))
        resetColor = findPreference(getString(com.loskon.noteminimalism3.R.string.reset_app_color))
        fontSizeSlider = findPreference("zero")
        numberLinesSlider = findPreference(getString(com.loskon.noteminimalism3.R.string.number_of_lines_key))
        oneSizeCardsSwitch = findPreference(getString(com.loskon.noteminimalism3.R.string.one_size_cards_key))
    }

    private fun setupPreferencesListeners() {
        resetFontSize?.setDebouncePreferenceClickListener {
            AppPreference.setTitleFontSize(requireContext(), getInteger(R.integer.title_font_size_int))
            AppPreference.setDateFontSize(requireContext(), getInteger(R.integer.date_font_size_int))
            listView.adapter?.notifyItemChanged(1)
        }
        selectColor?.setDebouncePreferenceClickListener {
            ColorPickerSheetDialogFragment.newInstance().apply {
                setHandleSelectedColorListener {
                    AppPreference.setAppColor(requireContext(), it)
                    listView.adapter?.notifyDataSetChanged()
                }
            }.show(childFragmentManager, ColorPickerSheetDialogFragment.TAG)
        }
        selectColorHex?.setDebouncePreferenceClickListener {
            // SelectColorHexSheetDialog(this).show()
        }
        resetColor?.setDebouncePreferenceClickListener {
            // ResetColorWarningSheetDialog(this).show()
        }
        fontSizeSlider?.setOnChangeListener { newValue ->
            noteCardView?.setTextSizes(newValue, getDateFontSize(newValue))
            AppPreference.setTitleFontSize(requireContext(), newValue)
            AppPreference.setDateFontSize(requireContext(), getDateFontSize(newValue))
        }
        oneSizeCardsSwitch?.setShortPreferenceChangeListener { newValue ->

        }
        numberLinesSlider?.setOnChangeListener { newValue ->

        }
    }

    private fun getDateFontSize(titleFontSize: Int): Int {
        return when {
            titleFontSize < 18 -> 12
            titleFontSize <= 22 -> 14
            titleFontSize <= 26 -> 16
            titleFontSize <= 30 -> 18
            titleFontSize <= 34 -> 20
            titleFontSize <= 38 -> 22
            titleFontSize <= 42 -> 24
            else -> 14
        }
    }
}