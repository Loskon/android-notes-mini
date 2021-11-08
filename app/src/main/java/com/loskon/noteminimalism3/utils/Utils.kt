package com.loskon.noteminimalism3.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

/**
 * Разные утилиты
 */

// Тост 1
fun Context.showToast(stringId: Int) {
    var toast: Toast? = null

    toast?.cancel()
    toast = makeText(this, getString(stringId), Toast.LENGTH_SHORT)
    toast.show()
}

// Тост 2
fun Context.showToast(message: String?) {
    var toast: Toast? = null

    toast?.cancel()
    toast = makeText(this, "$message", Toast.LENGTH_SHORT)
    toast.show()
}


//
fun Context.getShortDrawable(@DrawableRes icon: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, icon, null)
}


//
fun Context.getShortFont(@FontRes font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}


//
fun Context.getShortColor(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}


