package com.loskon.noteminimalism3.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * Утилиты
 */

// Получение ресурсов drawable
fun Context.getShortDrawable(@DrawableRes icon: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, icon, null)
}


// Получение ресурсов font
fun Context.getShortFont(@FontRes font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}


// Получение ресурсов color
fun Context.getShortColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}


