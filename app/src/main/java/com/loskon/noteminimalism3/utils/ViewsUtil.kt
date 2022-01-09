package com.loskon.noteminimalism3.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Утилиты для вьюшек
 */

fun View.setVisibleView(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}


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


fun View.setLayoutParamsForInsertedView() {
    this.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}


fun EditText.getLength(): Int {
    return text.toString().length
}


fun TextView.changeTextSize(fontSize: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
}


fun EditText.scrollBottom(scrollView: ScrollView) {
    scrollView.post { scrollView.scrollTo(0, bottom) }
}


fun View.enableSearchView(enabled: Boolean) {
    this.isEnabled = enabled
    if (this is ViewGroup) {
        val viewGroup = this
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            child.enableSearchView(enabled)
        }
    }
}

fun RecyclerView.changeLayoutManager(isLinear: Boolean) {
    layoutManager = if (isLinear) {
        LinearLayoutManager(context)
    } else {
        StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
    }
}


fun EditText.disableFocus() {
    isClickable = true
    isCursorVisible = false
    isFocusable = false
}