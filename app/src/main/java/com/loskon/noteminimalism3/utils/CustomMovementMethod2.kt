package com.loskon.noteminimalism3.utils

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

abstract class CustomMovementMethod2 : LinkMovementMethod() {

    override fun onTouchEvent(textView: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val offset = textView.getOffsetForPosition(event.x, event.y)
            val link = buffer.getSpans(offset, offset, URLSpan::class.java)

            if (link.isNotEmpty()) {
                onLinkClick(link[0].url)
            } else {
                onEmptyClick()
            }
            return true
        }
        return false
    }

    abstract fun onLinkClick(url: String?)
    abstract fun onEmptyClick()
}
