package com.loskon.noteminimalism3.base.extension.view

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.widget.LinearLayout

@SuppressLint("ClickableViewAccessibility")
fun LinearLayout.setOnDownClickListener(onDownClick: () -> Unit) {
    setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) onDownClick()
        false
    }
}