package com.loskon.noteminimalism3.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 *
 */

//
fun View.setVisibleView(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

//
fun View.settingsBehavior() {
    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(this.parent as View)
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    behavior.isDraggable = false
    behavior.isHideable = false
}

//
fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
        topMarginDp?.run { params.topMargin = this.dpToPx(context) }
        rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
        bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
        requestLayout()
    }
}

fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}

//
fun View.setLayoutParams() {
    this.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

//
fun Context.getShortColor(colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

//
fun EditText.getLength(): Int {
    return text.toString().length
}

//
fun TextView.setFontSize(fontSize: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
}