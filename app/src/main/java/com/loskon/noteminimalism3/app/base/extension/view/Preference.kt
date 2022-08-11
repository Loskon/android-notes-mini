package com.loskon.noteminimalism3.app.base.extension.view

import android.os.SystemClock
import androidx.preference.Preference
import androidx.preference.SwitchPreference

fun SwitchPreference.setShortPreferenceChangeListener(onPreferenceChange: (Boolean) -> Unit) {
    setOnPreferenceChangeListener { _, newValue ->
        onPreferenceChange(newValue as Boolean)
        true
    }
}

fun Preference.setDebouncePreferenceClickListener(
    debounceTime: Long = 600L,
    onPreferenceClick: () -> Unit
) {
    onPreferenceClickListener = object : Preference.OnPreferenceClickListener {
        private var lastClickTime: Long = 0

        override fun onPreferenceClick(preference: Preference): Boolean {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return false
            } else {
                onPreferenceClick()
            }

            lastClickTime = SystemClock.elapsedRealtime()
            return true
        }
    }
}