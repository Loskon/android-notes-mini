package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.base.extension.view.setDebouncePreferenceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortPreferenceChangeListener
import com.loskon.noteminimalism3.app.base.widget.preference.SliderPreference
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.sheetdialogs.ResetColorWarningSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.ResetFontSizeWarningSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.SelectColorHexSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.SelectColorPickerSheetDialog

class AppearanceSettingsFragment : PreferenceFragmentCompat() {

    private lateinit var activity: SettingsActivity

    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var selectColorHex: Preference? = null
    private var resetColor: Preference? = null
    private var numberLinesSlider: SliderPreference? = null
    private var oneSizeCardsSwitch: SwitchPreference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView.isVerticalScrollBarEnabled = false
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appearance_preferences, rootKey)
        findPreferences()
        setupPreferencesListeners()
    }

    private fun findPreferences() {
        resetFontSize = findPreference(getString(R.string.reset_font_size_title))
        selectColor = findPreference(getString(R.string.select_color_app_title))
        selectColorHex = findPreference(getString(R.string.select_color_app_hex_title))
        resetColor = findPreference(getString(R.string.reset_app_color))
        numberLinesSlider = findPreference(getString(R.string.number_of_lines_key))
        oneSizeCardsSwitch = findPreference(getString(R.string.one_size_cards_key))
    }

    private fun setupPreferencesListeners() {
        resetFontSize?.setDebouncePreferenceClickListener {
            ResetFontSizeWarningSheetDialog(this).show()
        }
        selectColor?.setDebouncePreferenceClickListener {
            SelectColorPickerSheetDialog(this).show()
        }
        selectColorHex?.setDebouncePreferenceClickListener {
            SelectColorHexSheetDialog(this).show()
        }
        resetColor?.setDebouncePreferenceClickListener {
            ResetColorWarningSheetDialog(this).show()
        }
        oneSizeCardsSwitch?.setShortPreferenceChangeListener { newValue: Boolean ->
            callbackSize?.onToggleStatusSizeCards(newValue)
        }
        numberLinesSlider?.setOnChangeListener { newValue: Int ->
            callbackNumberLines?.onChangeNumberLines(newValue)
        }
    }

    //--- Внешние методы ---------------------------------------------------------------------------
    fun callingCallbacks(appColor: Int) {
        AppPreference.setColor(activity, appColor)
        activity.onChangeColor(appColor)
        callbackColor?.onChangeColor(appColor)
        forceUpdatePreferenceList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun forceUpdatePreferenceList() {
        listView.adapter?.notifyDataSetChanged()
    }

    fun resetFontSize() {
        resetFontSizeCallback?.onResetFontSize()
    }

    //--- interface --------------------------------------------------------------------------------
    interface ResetFontSizeCallback {
        fun onResetFontSize()
    }

    interface MainColorCallback {
        fun onChangeColor(color: Int)
    }

    interface OneSizeCardsCallback {
        fun onToggleStatusSizeCards(hasOneSizeCards: Boolean)
    }

    interface NumberLinesCallback {
        fun onChangeNumberLines(numberLines: Int)
    }

    companion object {
        private var resetFontSizeCallback: ResetFontSizeCallback? = null
        private var callbackColor: MainColorCallback? = null
        private var callbackSize: OneSizeCardsCallback? = null
        private var callbackNumberLines: NumberLinesCallback? = null

        fun registerResetFontSizeCallback(resetFontSizeCallback: ResetFontSizeCallback?) {
            this.resetFontSizeCallback = resetFontSizeCallback
        }

        fun registerColorCallback(callbackColor: MainColorCallback) {
            this.callbackColor = callbackColor
        }

        fun registerOneSizeCardsCallback(callbackSize: OneSizeCardsCallback) {
            this.callbackSize = callbackSize
        }

        fun registerNumberLinesCallback(callbackNumberLines: NumberLinesCallback) {
            this.callbackNumberLines = callbackNumberLines
        }
    }
}