package com.loskon.noteminimalism3.ui.fragments.update

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.activities.update.SettingsActivityUpdate
import com.loskon.noteminimalism3.ui.sheets.SheetPrefSelectColor

/**
 * Форма настроек внешнего вида
 */

class SettingsAppFragmentUpdate :
    PreferenceFragmentCompat(),
    Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener,
    SheetPrefSelectColor.CallbackColorNotifyData {

    private lateinit var activity: SettingsActivityUpdate

    private var resetFontSizeKey: String = ""
    private var selectColorKey: String = ""
    private var oneSizeCardsKey: String = ""

    private var resetFontSize: Preference? = null
    private var selectColor: Preference? = null
    private var oneSizeCards: SwitchPreference? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as SettingsActivityUpdate
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
        SheetPrefSelectColor.regCallBackColorNotifyData(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_app_update, rootKey)
        getPreferenceScreenKeys()
        findPreferences()
        installPreferencesListener()
    }

    private fun getPreferenceScreenKeys() {
        resetFontSizeKey = getString(R.string.reset_font_size_title)
        selectColorKey = getString(R.string.seletct_color_app_title)
        oneSizeCardsKey = getString(R.string.one_size_cards_title)
    }

    private fun findPreferences() {
        resetFontSize = findPreference(resetFontSizeKey)
        selectColor = findPreference(selectColorKey)
        oneSizeCards = findPreference(oneSizeCardsKey)
    }

    private fun installPreferencesListener() {
        resetFontSize?.onPreferenceClickListener = this
        selectColor?.onPreferenceClickListener = this
        oneSizeCards?.onPreferenceChangeListener = this // Change
    }

    override fun onPreferenceClick(preference: Preference?): Boolean {
        val key: String? = preference?.key

        if (key == resetFontSizeKey) {
            callbackReset?.onResetFontSize()
            return true
        } else if (key == selectColorKey) {
            SheetPrefSelectColor(activity).show()
            return true
        }

        return false
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

    interface CallbackOneSizeCardsUpdate {
        fun onChangeStatusSizeCards(hasOneSizeCards: Boolean)
    }

    interface CallbackResetFontSize {
        fun onResetFontSize()
    }

    companion object {
        private var callbackSize: CallbackOneSizeCardsUpdate? = null
        private var callbackReset: CallbackResetFontSize? = null

        fun listenerCallbackSize(callback: CallbackOneSizeCardsUpdate) {
            this.callbackSize = callback
        }

        fun listenerCallbackReset(callback: CallbackResetFontSize) {
            this.callbackReset = callback
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onChangeColor() {
        listView.adapter?.notifyDataSetChanged()
    }
}