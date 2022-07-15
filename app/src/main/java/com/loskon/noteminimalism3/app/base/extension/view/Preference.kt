package com.loskon.noteminimalism3.app.base.extension.view

import android.os.SystemClock
import androidx.preference.Preference
import androidx.preference.SwitchPreference

fun SwitchPreference.setOnShortPreferenceChangeListener(onChange: (Boolean) -> Unit) {
    setOnPreferenceChangeListener { _, newValue ->
        onChange(newValue as Boolean)
        return@setOnPreferenceChangeListener true
    }
}

fun Preference.setDebouncePreferenceClickListener(debounceTime: Long = 600L, onClick: () -> Unit) {
    onPreferenceClickListener = object : Preference.OnPreferenceClickListener {

        private var lastClickTime: Long = 0

        override fun onPreferenceClick(preference: Preference): Boolean {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return false
            } else {
                onClick()
            }

            lastClickTime = SystemClock.elapsedRealtime()
            return true
        }
    }
}