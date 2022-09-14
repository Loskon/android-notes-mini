package com.loskon.noteminimalism3.app.presentation.screens.settings.appearance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.dialogfragment.show
import com.loskon.noteminimalism3.app.base.extension.fragment.getColor
import com.loskon.noteminimalism3.app.base.extension.fragment.getInteger
import com.loskon.noteminimalism3.app.base.extension.fragment.setChildFragmentResultListener
import com.loskon.noteminimalism3.app.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortPreferenceChangeListener
import com.loskon.noteminimalism3.app.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.app.base.presentation.sheetdialogfragment.ConfirmSheetDialogFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setChildFragmentResultListener(ColorHexSheetDialogFragment.TAG) { bundle -> changeAppColor(bundle) }
    }

    private fun changeAppColor(bundle: Bundle) {
        val color = bundle.getInt(ColorHexSheetDialogFragment.TAG)
        AppPreference.setColor(requireContext(), color)
        listView.adapter?.notifyDataSetChanged()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey)

        findPreferences()
        setupPreferencesListeners()
    }

    private fun findPreferences() {
        noteCardView = findPreference(getString(R.string.note_card_view_key))
        resetFontSize = findPreference(getString(R.string.reset_font_size_title))
        selectColor = findPreference(getString(R.string.select_color_app_title))
        selectColorHex = findPreference(getString(R.string.select_color_app_hex_title))
        resetColor = findPreference(getString(R.string.reset_app_color))
        fontSizeSlider = findPreference(getString(R.string.font_size_key))
        numberLinesSlider = findPreference(getString(R.string.number_of_lines_key))
        oneSizeCardsSwitch = findPreference(getString(R.string.one_size_cards_key))
    }

    private fun setupPreferencesListeners() {
        resetFontSize?.setDebouncePreferenceClickListener { showConfirmResetFontSizeSheetDialog() }
        selectColor?.setDebouncePreferenceClickListener { showColorPickerSheetDialog() }
        selectColorHex?.setDebouncePreferenceClickListener { showColorHexSheetDialog() }
        resetColor?.setDebouncePreferenceClickListener { showConfirmResetColorSheetDialog() }
        fontSizeSlider?.setOnChangeListener { titleFontSize -> handleChangedFontSizeSlider(titleFontSize) }
        oneSizeCardsSwitch?.setShortPreferenceChangeListener { newValue -> }
        numberLinesSlider?.setOnChangeListener { newValue -> }
    }

    private fun showConfirmResetFontSizeSheetDialog() {
        ConfirmSheetDialogFragment.newInstance(
            title = getString(R.string.sheet_reset_font_size_title),
            btnOkText = getString(R.string.yes),
            btnCancelText = getString(R.string.no)
        ).apply {
            setOkClickListener {
                val titleFontSize = getInteger(R.integer.title_font_size_int)
                val dateFontSize = getInteger(R.integer.date_font_size_int)
                saveFontSizes(titleFontSize, dateFontSize)
                listView.adapter?.notifyDataSetChanged()
            }
        }.show(childFragmentManager, ConfirmSheetDialogFragment.TAG)
    }

    private fun saveFontSizes(titleFontSize: Int, dateFontSize: Int) {
        AppPreference.setTitleFontSize(requireContext(), titleFontSize)
        AppPreference.setDateFontSize(requireContext(), dateFontSize)
    }

    private fun showColorPickerSheetDialog() {
        ColorPickerSheetDialogFragment.newInstance().apply {
            setHandleSelectedColorListener { color ->
                AppPreference.setColor(requireContext(), color)
                listView.adapter?.notifyDataSetChanged()
            }
        }.show(childFragmentManager, ColorPickerSheetDialogFragment.TAG)
    }

    private fun showColorHexSheetDialog() {
        ColorHexSheetDialogFragment().show(childFragmentManager)
    }

    private fun showConfirmResetColorSheetDialog() {
        ConfirmSheetDialogFragment.newInstance(
            title = getString(R.string.sheet_reset_color_title),
            btnOkText = getString(R.string.yes),
            btnCancelText = getString(R.string.no)
        ).apply {
            setOkClickListener {
                val color = getColor(R.color.material_blue)
                AppPreference.setColor(requireContext(), color)
                listView.adapter?.notifyDataSetChanged()
            }
        }.show(childFragmentManager, ConfirmSheetDialogFragment.TAG)
    }

    private fun handleChangedFontSizeSlider(titleFontSize: Int) {
        val dateFontSize = getDateFontSize(titleFontSize)
        noteCardView?.setTextSizes(titleFontSize, dateFontSize)
        saveFontSizes(titleFontSize, dateFontSize)
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