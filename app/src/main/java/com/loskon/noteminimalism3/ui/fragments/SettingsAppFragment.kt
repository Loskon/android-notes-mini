package com.loskon.noteminimalism3.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.SettingsActivity
import com.loskon.noteminimalism3.ui.prefscreen.PrefScreenResetColor
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColorHex

/**
 * Форма настроек внешнего вида
 */

class SettingsAppFragment :
    PreferenceFragmentCompat(),
    Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener,
    SheetPrefSelectColor.CallbackColorNotifyData,
    SheetPrefSelectColorHex.CallbackColorHexNotifyData,
    PrefScreenResetColor.CallbackColorResetNotifyData {

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
        setDivider(null)
        listView.isVerticalScrollBarEnabled = false

        configurationBottomBar()
        installCallbacks()
    }

    private fun configurationBottomBar() {
        activity.apply {
            bottomBar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
    }

    private fun installCallbacks() {
        SheetPrefSelectColor.listenerCallBackNotifyData(this)
        SheetPrefSelectColorHex.listenerCallBackNotifyData(this)
        PrefScreenResetColor.listenerCallBackNotifyData(this)
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
        oneSizeCardsKey = getString(R.string.one_size_cards_title)
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
                SheetPrefSelectColor(activity).show()
                true
            }

            selectColorHexKey -> {
                SheetPrefSelectColorHex(activity).show()
                true
            }

            else -> false
        }

    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        val key: String? = preference?.key

        if (key == oneSizeCardsKey) {
            //oneSizeCards?.isChecked = newValue as Boolean
            callbackSize?.onChangeStatusSizeCards(newValue as Boolean)
            return true
        }

        return false
    }

    interface CallbackOneSizeCards {
        fun onChangeStatusSizeCards(hasOneSizeCards: Boolean)
    }

    interface CallbackResetFontSize {
        fun onResetFontSize()
    }

    companion object {
        private var callbackSize: CallbackOneSizeCards? = null
        private var callbackReset: CallbackResetFontSize? = null

        fun listenerCallbackSize(callback: CallbackOneSizeCards) {
            callbackSize = callback
        }

        fun listenerCallbackReset(callback: CallbackResetFontSize) {
            callbackReset = callback
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onChangeColor() {
        listView.adapter?.notifyDataSetChanged()
    }
}