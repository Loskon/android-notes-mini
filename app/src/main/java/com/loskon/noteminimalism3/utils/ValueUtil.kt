package com.loskon.noteminimalism3.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import com.loskon.noteminimalism3.R

//
fun Context.getRadiusLinLay(): Int {
    return resources.getDimension(R.dimen.corner_radius).toInt()
}

fun Context.getStrokeLinLay(): Int {
    return resources.getDimension(R.dimen.border_stroke).toInt()
}


//
fun Context.getShortDrawable(@DrawableRes icon: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, icon, null)
}


//
fun Context.getShortFont(@FontRes font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}
