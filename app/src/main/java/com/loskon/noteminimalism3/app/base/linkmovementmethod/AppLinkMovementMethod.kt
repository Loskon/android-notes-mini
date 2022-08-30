package com.loskon.noteminimalism3.app.base.linkmovementmethod

import android.graphics.RectF
import android.text.Layout
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

open class AppLinkMovementMethod : LinkMovementMethod() {

    private val rect = RectF()
    private var block = false

    private var onLinkClick: ((String) -> Unit)? = null
    private var onNoLinkClick: ((Int) -> Unit)? = null

    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        if (block) {
            return false
        } else if (event.action == MotionEvent.ACTION_UP) {
            var x = event.x.toInt()
            var y = event.y.toInt()

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y)
            val offset = layout.getOffsetForHorizontal(line, x.toFloat())

            if (rect.getCoordinatesInTouchArea(layout, line).contains(x.toFloat(), y.toFloat())) {
                val linkArray = buffer.getSpans(offset, offset, URLSpan::class.java)

                if (linkArray.isNotEmpty()) {
                    onLinkClick?.invoke(linkArray[0].url)
                } else {
                    onNoLinkClick?.invoke(offset)
                }
            } else {
                onNoLinkClick?.invoke(offset)
            }

            return true
        }

        return false
    }

    private fun RectF.getCoordinatesInTouchArea(layout: Layout, line: Int): RectF {
        left = layout.getLineLeft(line)
        top = layout.getLineTop(line).toFloat()
        right = layout.getLineRight(line)
        bottom = layout.getLineBottom(line).toFloat()
        return this
    }

    fun block(block: Boolean) {
        this.block = block
    }

    fun setOnLinkClickListener(onLinkClick: ((String) -> Unit)?) {
        this.onLinkClick = onLinkClick
    }

    fun setOnNoLinkClickListener(onNoLinkClick: ((Int) -> Unit)?) {
        this.onNoLinkClick = onNoLinkClick
    }
}