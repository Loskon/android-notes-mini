package com.loskon.noteminimalism3.utils

import androidx.appcompat.app.AppCompatDelegate

object ColorUtil {

    fun toggleDarkMode(hasDarkMode: Boolean) {
        if (hasDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}