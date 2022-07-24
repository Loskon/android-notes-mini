package com.loskon.noteminimalism3.app.base.extension.view

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun FloatingActionButton.setColorKtx(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}