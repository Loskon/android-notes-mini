package com.loskon.noteminimalism3.base.extension.view

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.setBackgroundColorKtx(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}