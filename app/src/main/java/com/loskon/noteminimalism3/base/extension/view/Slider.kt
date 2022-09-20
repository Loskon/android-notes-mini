package com.loskon.noteminimalism3.base.extension.view

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.google.android.material.slider.Slider

fun Slider.setColorKtx(@ColorInt color: Int) {
    thumbTintList = ColorStateList.valueOf(color)
    trackActiveTintList = ColorStateList.valueOf(color)
    tickTintList = ColorStateList.valueOf(color)
    haloTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70))
    trackInactiveTintList = ColorStateList.valueOf(ColorUtils.setAlphaComponent(color, 70))
}

fun Slider.setOnChangeListener(onChange: (Slider, Int, Boolean) -> Unit) {
    addOnChangeListener(
        Slider.OnChangeListener { slider, value, fromUser ->
            onChange(slider, value.toInt(), fromUser)
        }
    )
}