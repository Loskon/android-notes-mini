package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sharedpref.PrefHelper
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.sheetdialogs.ResetColorWarningSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.ResetFontSizeWarningSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.SelectColorHexSheetDialog
import com.loskon.noteminimalism3.ui.sheetdialogs.SelectColorPickerSheetDialog

/**
 * Форма настроек внешнего вида
 */

class SettingsAppFragment :
    BaseSettingsFragment(),
    Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {

    private lateinit var activity: SettingsActivity

    // PreferenceScreen Keys
    private var resetFontSizeKey: String = ""
    private var selectColorKey: String = ""
    private var selectColorHexKey: String = ""
    private var resetColorKey: String = ""
    private var oneSizeCardsKey: String = ""

    // Preferences and SwitchPreferences
    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var selectColorHex: Preference? = null
    private var resetColor: Preference? = null
    private var oneSizeCardsSwitch: SwitchPreference? = null

    private var lastClickTime = 0L

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivity
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
        resetColorKey = getString(R.string.reset_app_color)
        oneSizeCardsKey = getString(R.string.one_size_cards_key)
    }

    private fun findPreferences() {
        resetFontSize = findPreference(resetFontSizeKey)
        selectColor = findPreference(selectColorKey)
        selectColorHex = findPreference(selectColorHexKey)
        resetColor = findPreference(resetColorKey)
        oneSizeCardsSwitch = findPreference(oneSizeCardsKey)
    }

    private fun installPreferencesListener() {
        resetFontSize?.onPreferenceClickListener = this
        selectColor?.onPreferenceClickListener = this
        selectColorHex?.onPreferenceClickListener = this
        resetColor?.onPreferenceClickListener = this
        oneSizeCardsSwitch?.onPreferenceChangeListener = this // onChange
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if (SystemClock.elapsedRealtime() - lastClickTime < 600) {
            return false
        } else {
            lastClickTime = SystemClock.elapsedRealtime()
        }

        return when (preference?.key) {
            resetFontSizeKey -> {
                ResetFontSizeWarningSheetDialog(this).show()
                true
            }

            selectColorKey -> {
                SelectColorPickerSheetDialog(this).show()
                true
            }

            selectColorHexKey -> {
                SelectColorHexSheetDialog(this).show()
                true
            }

            resetColorKey -> {
                ResetColorWarningSheetDialog(this).show()
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

    //--- Внешние методы ---------------------------------------------------------------------------
    fun callingCallbacks(appColor: Int) {
        PrefHelper.setAppColor(activity, appColor)
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
        fun onChangeStatusSizeCards(hasOneSizeCards: Boolean)
    }

    companion object {
        private var resetFontSizeCallback: ResetFontSizeCallback? = null
        private var callbackColor: MainColorCallback? = null
        private var callbackSize: OneSizeCardsCallback? = null

        fun registerResetFontSizeCallback(resetFontSizeCallback: ResetFontSizeCallback?) {
            this.resetFontSizeCallback = resetFontSizeCallback
        }

        fun registerColorCallback(callbackColor: MainColorCallback) {
            this.callbackColor = callbackColor
        }

        fun registerOneSizeCardsCallback(callbackSize: OneSizeCardsCallback) {
            this.callbackSize = callbackSize
        }
    }
}