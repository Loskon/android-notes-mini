package com.loskon.noteminimalism3.base.extension.view

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton

fun MaterialButton.setIconColor(@ColorInt colorId: Int) {
    iconTint = ColorStateList.valueOf(colorId)
}