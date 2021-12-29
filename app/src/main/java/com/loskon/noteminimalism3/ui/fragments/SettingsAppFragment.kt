package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenResetColor
import com.loskon.noteminimalism3.ui.sheets.SelectColorHexSheetDialog
import com.loskon.noteminimalism3.ui.sheets.SelectColorPickerSheetDialog

/**
 * Форма настроек внешнего вида
 */

class SettingsAppFragment :
    AppBaseSettingsFragment(),
    Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener,
    SelectColorPickerSheetDialog.ColorNotifyDataCallback,
    SelectColorHexSheetDialog.ColorHexNotifyDataCallback,
    PrefScreenResetColor.ColorResetNotifyDataCallback {

    private lateinit var activity: SettingsActivity

    private var resetFontSizeKey: String = ""
    private var selectColorKey: String = ""
    private var selectColorHexKey: String = ""
    private var oneSizeCardsKey: String = ""

    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var selectColorHex: Preference? = null
    private var oneSizeCards: SwitchPreference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        installCallbacks()
    }

    private fun installCallbacks() {
        SelectColorPickerSheetDialog.registerCallbackNotifyData(this)
        SelectColorHexSheetDialog.registerCallbackNotifyData(this)
        PrefScreenResetColor.registerCallbackNotifyData(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_app, rootKey)
        getPreferenceScreenKeys()
        findPreferences()
        installPreferencesListener()
    }

    private fun getPreferenceScreenKeys() {
        resetFontSizeKey = getString(R.string.reset_font_size_title)
        selectColorKey = getString(R.string.select_color_app_title)
        selectColorHexKey = getString(R.string.select_color_app_hex_title)
        oneSizeCardsKey = getString(R.string.one_size_cards_key)
    }

    private fun findPreferences() {
        resetFontSize = findPreference(resetFontSizeKey)
        selectColor = findPreference(selectColorKey)
        selectColorHex = findPreference(selectColorHexKey)
        oneSizeCards = findPreference(oneSizeCardsKey)
    }

    private fun installPreferencesListener() {
        resetFontSize?.onPreferenceClickListener = this
        selectColor?.onPreferenceClickListener = this
        selectColorHex?.onPreferenceClickListener = this
        oneSizeCards?.onPreferenceChangeListener = this // Change
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        return when (preference?.key) {
            resetFontSizeKey -> {
                callbackReset?.onResetFontSize()
                true
            }

            selectColorKey -> {
                SelectColorPickerSheetDialog(activity).show()
                true
            }

            selectColorHexKey -> {
                SelectColorHexSheetDialog(activity).show()
                true
            }

            else -> false
        }

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val key: String? = preference?.key

        if (key == oneSizeCardsKey) {
            callbackSize?.onChangeStatusSizeCards(newValue as Boolean)
            return true
        }

        return false
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onChangeColor() {
        listView.adapter?.notifyDataSetChanged()
    }

    interface OneSizeCardsCallback {
        fun onChangeStatusSizeCards(hasOneSizeCards: Boolean)
    }

    interface ResetFontSizeCallback {
        fun onResetFontSize()
    }

    companion object {
        private var callbackSize: OneSizeCardsCallback? = null
        private var callbackReset: ResetFontSizeCallback? = null

        fun registerCallbackOneSizeCards(callback: OneSizeCardsCallback) {
            callbackSize = callback
        }

        fun registerCallbackResetFontSize(callback: ResetFontSizeCallback) {
            callbackReset = callback
        }
    }
}