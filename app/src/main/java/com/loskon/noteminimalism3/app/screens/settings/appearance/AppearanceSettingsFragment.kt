package com.loskon.noteminimalism3.app.screens.settings.appearance

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.base.extension.dialogfragment.show
import com.loskon.noteminimalism3.base.extension.fragment.getColor
import com.loskon.noteminimalism3.base.extension.fragment.getInteger
import com.loskon.noteminimalism3.base.extension.fragment.setChildFragmentClickListener
import com.loskon.noteminimalism3.base.extension.fragment.setChildFragmentResultListener
import com.loskon.noteminimalism3.base.extension.fragment.setFragmentResultListener
import com.loskon.noteminimalism3.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.base.extension.view.setPreferenceChangeListener
import com.loskon.noteminimalism3.base.presentation.fragment.BasePreferenceFragment
import com.loskon.noteminimalism3.base.presentation.sheetdialogfragment.ConfirmSheetDialogFragment
import com.loskon.noteminimalism3.base.widget.preference.NoteCardViewPreference
import com.loskon.noteminimalism3.base.widget.preference.SliderPreference
import com.loskon.noteminimalism3.sharedpref.AppPreference

@SuppressLint("NotifyDataSetChanged")
class AppearanceSettingsFragment : BasePreferenceFragment() {

    private var noteCardView: NoteCardViewPreference? = null
    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var selectColorHex: Preference? = null
    private var resetColor: Preference? = null
    private var fontSizeSlider: SliderPreference? = null
    private var oneSizeCardsSwitch: SwitchPreference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setChildFragmentClickListener(RESET_FONT_SIZE_KEY) { resetFontSize() }
        setFragmentResultListener(SET_COLOR_REQUEST_KEY) { bundle -> setAppColor(bundle) }
        setChildFragmentResultListener(SET_COLOR_REQUEST_KEY) { bundle -> setAppColor(bundle) }
        setChildFragmentClickListener(RESET_COLOR_REQUEST_KEY) { setDefaultColor() }
    }

    private fun resetFontSize() {
        val titleFontSize = getInteger(R.integer.title_font_size_int)
        val dateFontSize = getInteger(R.integer.date_font_size_int)
        saveFontSizes(titleFontSize, dateFontSize)
        listView.adapter?.notifyDataSetChanged()
    }

    private fun saveFontSizes(titleFontSize: Int, dateFontSize: Int) {
        AppPreference.setTitleFontSize(requireContext(), titleFontSize)
        AppPreference.setDateFontSize(requireContext(), dateFontSize)
    }

    private fun setAppColor(bundle: Bundle) {
        val color = bundle.getInt(SET_COLOR_REQUEST_KEY)
        setBottomBarNavigationIconColor(color)
        AppPreference.setColor(requireContext(), color)
        listView.adapter?.notifyDataSetChanged()
    }

    private fun setDefaultColor() {
        val color = getColor(R.color.material_blue)
        setBottomBarNavigationIconColor(color)
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
        oneSizeCardsSwitch = findPreference(getString(R.string.one_size_cards_key))
    }

    private fun setupPreferencesListeners() {
        resetFontSize?.setDebouncePreferenceClickListener { showConfirmResetFontSizeSheetDialog() }
        selectColor?.setDebouncePreferenceClickListener { ColorPickerSheetDialogFragment().show(parentFragmentManager) }
        selectColorHex?.setDebouncePreferenceClickListener { ColorHexSheetDialogFragment().show(childFragmentManager) }
        resetColor?.setDebouncePreferenceClickListener { showConfirmResetColorSheetDialog() }
        fontSizeSlider?.setOnChangeListener { titleFontSize -> handleChangedFontSizeSlider(titleFontSize) }
        oneSizeCardsSwitch?.setPreferenceChangeListener { value -> AppPreference.setLinearListType(requireContext(), value) }
    }

    private fun showConfirmResetFontSizeSheetDialog() {
        ConfirmSheetDialogFragment.newInstance(
            requestKey = RESET_FONT_SIZE_KEY,
            title = getString(R.string.sheet_reset_font_size_title),
            btnOkText = getString(R.string.yes),
            btnCancelText = getString(R.string.no)
        ).show(childFragmentManager)
    }

    private fun showConfirmResetColorSheetDialog() {
        ConfirmSheetDialogFragment.newInstance(
            requestKey = RESET_COLOR_REQUEST_KEY,
            title = getString(R.string.sheet_reset_color_title),
            btnOkText = getString(R.string.yes),
            btnCancelText = getString(R.string.no)
        ).show(childFragmentManager)
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

    companion object {
        const val SET_COLOR_REQUEST_KEY = "SET_COLOR_REQUEST_KEY"
        private const val RESET_COLOR_REQUEST_KEY = "RESET_COLOR_REQUEST_KEY"
        private const val RESET_FONT_SIZE_KEY = "RESET_FONT_SIZE_KEY"
    }
}