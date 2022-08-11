package com.loskon.noteminimalism3.app.base.extension.view

import android.content.res.ColorStateList
import android.content.res.Resources
import android.os.SystemClock
import android.view.View
import androidx.core.view.isVisible
import kotlin.math.roundToInt

fun View.setDebounceClickListener(
    debounceTime: Long = 600L,
    onClick: (View) -> Unit
) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(view: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                onClick(view)
            }

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.setShortLongClickListener(onLongClick: (View) -> Unit) {
    setOnLongClickListener { view ->
        onLongClick(view)
        true
    }
}

fun View.setBackgroundTintColor(color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

fun View.setSoftVisibleKtx(visible: Boolean) {
    visibility = if (isVisible == visible) {
        return
    } else {
        if (visible) View.VISIBLE else View.INVISIBLE
    }
}

fun View.setGoneVisibleKtx(visible: Boolean) {
    visibility = if (isVisible == visible) {
        return
    } else {
        if (visible) View.VISIBLE else View.GONE
    }
}

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

val Float.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()