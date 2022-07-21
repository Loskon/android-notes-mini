package com.loskon.noteminimalism3.app.base.extension.view

import android.util.TypedValue
import android.widget.TextView

fun TextView.setTextSizeKtx(fontSize: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize.toFloat())
}