package com.loskon.noteminimalism3.base.extension.view

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.google.android.material.progressindicator.CircularProgressIndicator

fun CircularProgressIndicator.setColor(@ColorInt color: Int) {
    setIndicatorColor(color)
    trackColor = ColorUtils.setAlphaComponent(color, 70)
}