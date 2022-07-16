package com.loskon.noteminimalism3.app.base.extension.view

import android.os.SystemClock
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.bottomappbar.BottomAppBar

fun BottomAppBar.setDebounceNavigationClickListener(debounceTime: Long = 600L, onClick: () -> Unit) {
    setNavigationOnClickListener(object : View.OnClickListener {

        private var lastClickTime: Long = 0

        override fun onClick(view: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                onClick()
            }

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}