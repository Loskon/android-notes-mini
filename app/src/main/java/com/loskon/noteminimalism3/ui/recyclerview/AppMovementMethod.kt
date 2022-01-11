package com.loskon.noteminimalism3.ui.recyclerview

import android.graphics.RectF
import android.text.Layout
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

/**
 * Обработка нажатий по ссылке и определение области, в которой она находится
 */

abstract class AppMovementMethod : LinkMovementMethod() {

    private val rectF: RectF = RectF()

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            // Найти область, которая была нажата
            var x: Int = event.x.toInt()
            var y: Int = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            // Найдите URL-адрес в тексте
            val layout: Layout = widget.layout
            val line: Int = layout.getLineForVertical(y)
            val offset: Int = layout.getOffsetForHorizontal(line, x.toFloat())

            rectF.getCoordinatesInTouchArea(layout, line)

            if (rectF.contains(x.toFloat(), y.toFloat())) {
                // Найти область кликабельности, которая находится под затронутой областью
                val link: Array<URLSpan> = buffer.getSpans(offset, offset, URLSpan::class.java)

                if (link.isNotEmpty()) {
                    onLinkClick(link[0].url)
                } else {
                    onTextClick()
                }

            } else {
                onTextClick()
            }

            return true
        }

        return false
    }

    private fun RectF.getCoordinatesInTouchArea(layout: Layout, line: Int) {
        left = layout.getLineLeft(line)
        top = layout.getLineTop(line).toFloat()
        right = layout.getLineRight(line)
        bottom = layout.getLineBottom(line).toFloat()
    }

    abstract fun onLinkClick(url: String)
    abstract fun onTextClick()
}
