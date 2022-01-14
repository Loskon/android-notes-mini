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
        callbackColorNavIcon?.onChangeColor(appColor)
        callbackColorList?.onChangeColor(appColor)
        forceUpdatePreferenceList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun forceUpdatePreferenceList() {
        listView.adapter?.notifyDataSetChanged()
    }

    fun resetFontSize() {
        callbackReset?.onResetFontSize()
    }

    //--- interface --------------------------------------------------------------------------------
    interface ColorNavIconCallback2 {
        fun onChangeColor(color: Int)
    }

    interface MainColorCallback {
        fun onChangeColor(color: Int)
    }

    interface OneSizeCardsCallback {
        fun onChangeStatusSizeCards(hasOneSizeCards: Boolean)
    }

    interface ResetFontSizeCallback {
        fun onResetFontSize()
    }

    companion object {
        private var callbackReset: ResetFontSizeCallback? = null
        private var callbackColorNavIcon: ColorNavIconCallback2? = null
        private var callbackColorList: MainColorCallback? = null
        private var callbackSize: OneSizeCardsCallback? = null

        fun registerCallbackResetFontSize(callback: ResetFontSizeCallback) {
            callbackReset = callback
        }

        fun registerCallbackColorNavIcon(callbackColorNavIcon: ColorNavIconCallback2) {
            this.callbackColorNavIcon = callbackColorNavIcon
        }

        fun registerCallbackColorList(callbackColorList: MainColorCallback) {
            this.callbackColorList = callbackColorList
        }

        fun registerCallbackOneSizeCards(callback: OneSizeCardsCallback) {
            callbackSize = callback
        }
    }
}