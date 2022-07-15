package com.loskon.noteminimalism3.app.base.widget.preference

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.preference.Preference

/**
 * Preference
 */
class DebounceClickPreference constructor(
    context: Context,
    attrs: AttributeSet
) : Preference(context, attrs) {

    private val THRESHOLD_MILLIS = 1000L
    private var lastClickMillis: Long = 0

    override fun onClick() {
        val now = SystemClock.elapsedRealtime()

        if (now - lastClickMillis > THRESHOLD_MILLIS) {
            super.onClick()
        }

        lastClickMillis = now
    }
}