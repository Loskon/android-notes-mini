package com.loskon.noteminimalism3.utils

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun Context.getShortDrawable(@DrawableRes icon: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, icon, null)
}

fun Context.getShortFont(@FontRes font: Int): Typeface? {
    return ResourcesCompat.getFont(this, font)
}

fun Context.getShortColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getShortInt(@IntegerRes int: Int): Int {
    return resources.getInteger(int)
}

fun DialogFragment.onlyShow(fragmentManager: FragmentManager, tag: String) {
    if (fragmentManager.findFragmentByTag(tag) == null) {
        show(fragmentManager, tag)
    }
}