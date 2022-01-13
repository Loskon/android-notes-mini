package com.loskon.noteminimalism3.utils

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

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

fun View.setLayoutParamsForInsertedView() {
    this.layoutParams = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}


fun TextView.changeTextSize(fontSize: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
}


fun EditText.scrollBottom(scrollView: ScrollView) {
    scrollView.post { scrollView.scrollTo(0, bottom) }
}